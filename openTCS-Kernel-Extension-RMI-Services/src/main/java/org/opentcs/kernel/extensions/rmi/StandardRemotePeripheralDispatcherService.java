/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.kernel.extensions.rmi;

import static java.util.Objects.requireNonNull;

import jakarta.inject.Inject;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import org.opentcs.access.rmi.ClientID;
import org.opentcs.access.rmi.factories.SocketFactoryProvider;
import org.opentcs.access.rmi.services.RegistrationName;
import org.opentcs.access.rmi.services.RemotePeripheralDispatcherService;
import org.opentcs.components.kernel.services.PeripheralDispatcherService;
import org.opentcs.customizations.kernel.KernelExecutor;
import org.opentcs.data.TCSObjectReference;
import org.opentcs.data.model.Location;
import org.opentcs.data.model.TCSResourceReference;
import org.opentcs.data.peripherals.PeripheralJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the standard implementation of the {@link RemotePeripheralDispatcherService}
 * interface.
 * <p>
 * Upon creation, an instance of this class registers itself with the RMI registry by the name
 * {@link RegistrationName#REMOTE_PERIPHERAL_DISPATCHER_SERVICE}.
 * </p>
 */
public class StandardRemotePeripheralDispatcherService
    extends
      KernelRemoteService
    implements
      RemotePeripheralDispatcherService {

  /**
   * This class's logger.
   */
  private static final Logger LOG
      = LoggerFactory.getLogger(StandardRemotePeripheralDispatcherService.class);
  /**
   * The peripheral dispatcher service to invoke methods on.
   */
  private final PeripheralDispatcherService dispatcherService;
  /**
   * The user manager.
   */
  private final UserManager userManager;
  /**
   * Provides configuration data.
   */
  private final RmiKernelInterfaceConfiguration configuration;
  /**
   * Provides socket factories used for RMI.
   */
  private final SocketFactoryProvider socketFactoryProvider;
  /**
   * Provides the registry with which this remote service registers.
   */
  private final RegistryProvider registryProvider;
  /**
   * Executes tasks modifying kernel data.
   */
  private final ExecutorService kernelExecutor;
  /**
   * The registry with which this remote service registers.
   */
  private Registry rmiRegistry;
  /**
   * Whether this remote service is initialized or not.
   */
  private boolean initialized;

  /**
   * Creates a new instance.
   *
   * @param dispatcherService The peripheral dispatcher service.
   * @param userManager The user manager.
   * @param configuration This class' configuration.
   * @param socketFactoryProvider The socket factory provider used for RMI.
   * @param registryProvider The provider for the registry with which this remote service registers.
   * @param kernelExecutor Executes tasks modifying kernel data.
   */
  @Inject
  public StandardRemotePeripheralDispatcherService(
      PeripheralDispatcherService dispatcherService,
      UserManager userManager,
      RmiKernelInterfaceConfiguration configuration,
      SocketFactoryProvider socketFactoryProvider,
      RegistryProvider registryProvider,
      @KernelExecutor
      ExecutorService kernelExecutor
  ) {
    this.dispatcherService = requireNonNull(dispatcherService, "dispatcherService");
    this.userManager = requireNonNull(userManager, "userManager");
    this.configuration = requireNonNull(configuration, "configuration");
    this.socketFactoryProvider = requireNonNull(socketFactoryProvider, "socketFactoryProvider");
    this.registryProvider = requireNonNull(registryProvider, "registryProvider");
    this.kernelExecutor = requireNonNull(kernelExecutor, "kernelExecutor");
  }

  @Override
  public void initialize() {
    if (isInitialized()) {
      return;
    }

    rmiRegistry = registryProvider.get();

    // Export this instance via RMI.
    try {
      LOG.debug("Exporting proxy...");
      UnicastRemoteObject.exportObject(
          this,
          configuration.remoteDispatcherServicePort(),
          socketFactoryProvider.getClientSocketFactory(),
          socketFactoryProvider.getServerSocketFactory()
      );
      LOG.debug("Binding instance with RMI registry...");
      rmiRegistry.rebind(RegistrationName.REMOTE_PERIPHERAL_DISPATCHER_SERVICE, this);
    }
    catch (RemoteException exc) {
      LOG.error("Could not export or bind with RMI registry", exc);
      return;
    }

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

    try {
      LOG.debug("Unbinding from RMI registry...");
      rmiRegistry.unbind(RegistrationName.REMOTE_PERIPHERAL_DISPATCHER_SERVICE);
      LOG.debug("Unexporting RMI interface...");
      UnicastRemoteObject.unexportObject(this, true);
    }
    catch (RemoteException | NotBoundException exc) {
      LOG.warn("Exception shutting down RMI interface", exc);
    }

    initialized = false;
  }

  @Override
  public void dispatch(ClientID clientId) {
    userManager.verifyCredentials(clientId, UserPermission.MODIFY_PERIPHERAL_JOBS);

    try {
      kernelExecutor.submit(() -> dispatcherService.dispatch()).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public void withdrawByLocation(ClientID clientId, TCSResourceReference<Location> ref) {
    userManager.verifyCredentials(clientId, UserPermission.MODIFY_PERIPHERAL_JOBS);

    try {
      kernelExecutor.submit(() -> dispatcherService.withdrawByLocation(ref)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public void withdrawByPeripheralJob(ClientID clientId, TCSObjectReference<PeripheralJob> ref) {
    userManager.verifyCredentials(clientId, UserPermission.MODIFY_PERIPHERAL_JOBS);

    try {
      kernelExecutor.submit(() -> dispatcherService.withdrawByPeripheralJob(ref)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }
}
