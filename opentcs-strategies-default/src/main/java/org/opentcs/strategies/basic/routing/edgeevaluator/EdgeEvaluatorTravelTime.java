// SPDX-FileCopyrightText: The openTCS Authors
// SPDX-License-Identifier: MIT
package org.opentcs.strategies.basic.routing.edgeevaluator;

import static org.opentcs.strategies.basic.routing.PointRouter.INFINITE_COSTS;

import org.opentcs.components.kernel.routing.Edge;
import org.opentcs.components.kernel.routing.EdgeEvaluator;
import org.opentcs.data.model.Vehicle;

/**
 * Uses the estimated travel time (length/maximum velocity) for an edge as its weight.
 */
public class EdgeEvaluatorTravelTime
    implements
      EdgeEvaluator {

  /**
   * A key used for selecting this evaluator in a configuration setting.
   * Should be unique among all keys.
   */
  public static final String CONFIGURATION_KEY = "TRAVELTIME";

  public EdgeEvaluatorTravelTime() {
  }

  @Override
  public void onGraphComputationStart(Vehicle vehicle) {
  }

  @Override
  public void onGraphComputationEnd(Vehicle vehicle) {
  }

  @Override
  public double computeWeight(Edge edge, Vehicle vehicle) {
    int maxVelocity;
    if (edge.isTravellingReverse()) {
      maxVelocity = Math.min(
          vehicle.getMaxReverseVelocity(),
          edge.getPath().getMaxReverseVelocity()
      );
    }
    else {
      maxVelocity = Math.min(vehicle.getMaxVelocity(), edge.getPath().getMaxVelocity());
    }
    return (maxVelocity == 0) ? INFINITE_COSTS : edge.getPath().getLength() / maxVelocity;
  }
}
