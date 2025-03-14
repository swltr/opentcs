// SPDX-FileCopyrightText: The openTCS Authors
// SPDX-License-Identifier: MIT
package org.opentcs.strategies.basic.dispatching.phase.recharging;

import static java.util.Objects.requireNonNull;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.opentcs.access.to.order.DestinationCreationTO;
import org.opentcs.access.to.order.TransportOrderCreationTO;
import org.opentcs.components.kernel.services.InternalTransportOrderService;
import org.opentcs.data.model.Point;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.order.DriveOrder;
import org.opentcs.data.order.OrderConstants;
import org.opentcs.data.order.TransportOrder;
import org.opentcs.strategies.basic.dispatching.AssignmentCandidate;
import org.opentcs.strategies.basic.dispatching.DefaultDispatcherConfiguration;
import org.opentcs.strategies.basic.dispatching.DriveOrderRouteAssigner;
import org.opentcs.strategies.basic.dispatching.Phase;
import org.opentcs.strategies.basic.dispatching.TransportOrderUtil;
import org.opentcs.strategies.basic.dispatching.selection.candidates.CompositeAssignmentCandidateSelectionFilter;
import org.opentcs.strategies.basic.dispatching.selection.vehicles.CompositeRechargeVehicleSelectionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates recharging orders for any vehicles with a degraded energy level.
 */
public class RechargeIdleVehiclesPhase
    implements
      Phase {

  /**
   * This class's Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(RechargeIdleVehiclesPhase.class);
  /**
   * The transport order service.
   */
  private final InternalTransportOrderService orderService;
  /**
   * The strategy used for finding suitable recharge locations.
   */
  private final RechargePositionSupplier rechargePosSupplier;
  /**
   * A collection of predicates for filtering assignment candidates.
   */
  private final CompositeAssignmentCandidateSelectionFilter assignmentCandidateSelectionFilter;

  private final CompositeRechargeVehicleSelectionFilter vehicleSelectionFilter;

  private final TransportOrderUtil transportOrderUtil;
  /**
   * The dispatcher configuration.
   */
  private final DefaultDispatcherConfiguration configuration;
  /**
   * Assigns routes to drive orders.
   */
  private final DriveOrderRouteAssigner driveOrderRouteAssigner;
  /**
   * Indicates whether this component is initialized.
   */
  private boolean initialized;

  @Inject
  public RechargeIdleVehiclesPhase(
      InternalTransportOrderService orderService,
      RechargePositionSupplier rechargePosSupplier,
      CompositeAssignmentCandidateSelectionFilter assignmentCandidateSelectionFilter,
      CompositeRechargeVehicleSelectionFilter vehicleSelectionFilter,
      TransportOrderUtil transportOrderUtil,
      DefaultDispatcherConfiguration configuration,
      DriveOrderRouteAssigner driveOrderRouteAssigner
  ) {
    this.orderService = requireNonNull(orderService, "orderService");
    this.rechargePosSupplier = requireNonNull(rechargePosSupplier, "rechargePosSupplier");
    this.assignmentCandidateSelectionFilter = requireNonNull(
        assignmentCandidateSelectionFilter,
        "assignmentCandidateSelectionFilter"
    );
    this.vehicleSelectionFilter = requireNonNull(vehicleSelectionFilter, "vehicleSelectionFilter");
    this.transportOrderUtil = requireNonNull(transportOrderUtil, "transportOrderUtil");
    this.configuration = requireNonNull(configuration, "configuration");
    this.driveOrderRouteAssigner = requireNonNull(
        driveOrderRouteAssigner,
        "driveOrderRouteAssigner"
    );
  }

  @Override
  public void initialize() {
    if (isInitialized()) {
      return;
    }

    rechargePosSupplier.initialize();

    initialized = true;
  }

  @Override
  public boolean isInitialized() {
    return initialized;
  }

  @Override
  public void terminate() {
    if (!isInitialized()) {
      return;
    }

    rechargePosSupplier.terminate();

    initialized = false;
  }

  @Override
  public void run() {
    if (!configuration.rechargeIdleVehicles()) {
      return;
    }

    orderService.fetchObjects(Vehicle.class).stream()
        .filter(vehicle -> vehicleSelectionFilter.apply(vehicle).isEmpty())
        .forEach(vehicle -> createRechargeOrder(vehicle));
  }

  private void createRechargeOrder(Vehicle vehicle) {
    List<DriveOrder.Destination> rechargeDests = rechargePosSupplier.findRechargeSequence(vehicle);
    LOG.debug("Recharge sequence for {}: {}", vehicle, rechargeDests);

    if (rechargeDests.isEmpty()) {
      LOG.info("{}: Did not find a suitable recharge sequence.", vehicle.getName());
      return;
    }

    List<DestinationCreationTO> chargeDests = new ArrayList<>(rechargeDests.size());
    for (DriveOrder.Destination dest : rechargeDests) {
      chargeDests.add(
          new DestinationCreationTO(dest.getDestination().getName(), dest.getOperation())
              .withProperties(dest.getProperties())
      );
    }
    // Create a transport order for recharging and verify its processability.
    // The recharge order may be withdrawn unless its energy level is critical.
    TransportOrder rechargeOrder = orderService.createTransportOrder(
        new TransportOrderCreationTO("Recharge-", chargeDests)
            .withIncompleteName(true)
            .withIntendedVehicleName(vehicle.getName())
            .withDispensable(!vehicle.isEnergyLevelCritical())
            .withType(OrderConstants.TYPE_CHARGE)
    );

    Optional<AssignmentCandidate> candidate = computeCandidate(
        vehicle,
        orderService.fetchObject(Point.class, vehicle.getCurrentPosition()),
        rechargeOrder
    )
        .filter(c -> assignmentCandidateSelectionFilter.apply(c).isEmpty());
    // XXX Change this to Optional.ifPresentOrElse() once we're at Java 9+.
    if (candidate.isPresent()) {
      transportOrderUtil.assignTransportOrder(
          candidate.get().getVehicle(),
          candidate.get().getTransportOrder(),
          candidate.get().getDriveOrders()
      );
    }
    else {
      // Mark the order as failed, since the vehicle cannot execute it.
      orderService.updateTransportOrderState(
          rechargeOrder.getReference(),
          TransportOrder.State.FAILED
      );
    }
  }

  private Optional<AssignmentCandidate> computeCandidate(
      Vehicle vehicle,
      Point vehiclePosition,
      TransportOrder order
  ) {
    return driveOrderRouteAssigner.tryAssignRoutes(order, vehicle, vehiclePosition)
        .map(driveOrders -> new AssignmentCandidate(vehicle, order, driveOrders));
  }
}
