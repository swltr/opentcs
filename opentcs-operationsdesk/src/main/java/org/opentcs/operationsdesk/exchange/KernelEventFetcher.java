// SPDX-FileCopyrightText: The openTCS Authors
// SPDX-License-Identifier: MIT
package org.opentcs.operationsdesk.exchange;

import static java.util.Objects.requireNonNull;
import static org.opentcs.util.Assertions.checkInRange;

import jakarta.inject.Inject;
import java.util.List;
import javax.swing.SwingUtilities;
import org.opentcs.access.Kernel;
import org.opentcs.access.KernelRuntimeException;
import org.opentcs.access.KernelServicePortal;
import org.opentcs.access.KernelStateTransitionEvent;
import org.opentcs.access.SharedKernelServicePortal;
import org.opentcs.access.SharedKernelServicePortalProvider;
import org.opentcs.common.KernelClientApplication;
import org.opentcs.components.Lifecycle;
import org.opentcs.customizations.ApplicationEventBus;
import org.opentcs.operationsdesk.event.KernelStateChangeEvent;
import org.opentcs.util.CyclicTask;
import org.opentcs.util.event.EventBus;
import org.opentcs.util.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Periodically fetches events from the kernel, if connected, and publishes them via the local event
 * bus.
 */
public class KernelEventFetcher
    implements
      Lifecycle,
      EventHandler {

  /**
   * This class' logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(KernelEventFetcher.class);
  /**
   * The time to wait between event fetches with the service portal (in ms).
   */
  private final long eventFetchInterval = 100;
  /**
   * The time to wait for events to arrive when fetching (in ms).
   */
  private final long eventFetchTimeout = 1000;
  /**
   * Where we send events and receive them from.
   */
  private final EventBus eventBus;
  /**
   * Provides a shared portal instance.
   */
  private final SharedKernelServicePortalProvider servicePortalProvider;
  /**
   * The kernel client application.
   */
  private final KernelClientApplication kernelClientApplication;
  /**
   * The shared portal
   */
  private SharedKernelServicePortal sharedServicePortal;
  /**
   * The portal.
   */
  private KernelServicePortal servicePortal;
  /**
   * The task fetching the service portal for new events.
   */
  private EventFetcherTask eventFetcherTask;
  /**
   * Whether this event hub is initialized or not.
   */
  private boolean initialized;

  /**
   * Creates a new instance.
   *
   * @param eventBus Where this instance sends events.
   * @param servicePortalProvider Provides a shared portal instance.
   * @param kernelClientApplication The kernel client application.
   */
  @Inject
  public KernelEventFetcher(
      @ApplicationEventBus
      EventBus eventBus,
      SharedKernelServicePortalProvider servicePortalProvider,
      KernelClientApplication kernelClientApplication
  ) {
    this.eventBus = requireNonNull(eventBus, "eventBus");
    this.servicePortalProvider = requireNonNull(servicePortalProvider, "servicePortalProvider");
    this.kernelClientApplication
        = requireNonNull(kernelClientApplication, "kernelClientApplication");
  }

  @Override
  public void initialize() {
    if (isInitialized()) {
      return;
    }

    LOG.info("Initializing...");
    eventBus.subscribe(this);

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

    LOG.info("Terminating...");
    eventBus.unsubscribe(this);

    initialized = false;
  }

  @Override
  public void onEvent(Object event) {
    if (event instanceof KernelStateChangeEvent kernelStateChangeEvent) {
      switch (kernelStateChangeEvent.getNewState()) {
        case LOGGED_IN:
          handleKernelConnect();
          break;
        case DISCONNECTED:
          handleKernelDisconnect();
          break;
        default:
          // Do nothing.
      }
    }
  }

  private void handleKernelConnect() {
    if (eventFetcherTask != null) {
      return;
    }
    sharedServicePortal = servicePortalProvider.register();
    servicePortal = sharedServicePortal.getPortal();

    eventFetcherTask = new EventFetcherTask(eventFetchInterval, eventFetchTimeout);
    Thread eventFetcherThread = new Thread(eventFetcherTask, "KernelEventFetcher");
    eventFetcherThread.start();
  }

  private void handleKernelDisconnect() {
    if (eventFetcherTask == null) {
      return;
    }
    // Stop polling for events.
    eventFetcherTask.terminate();
    eventFetcherTask = null;

    sharedServicePortal.close();
    servicePortal = null;
  }

  /**
   * A task fetching the service portal for events in regular intervals.
   */
  private class EventFetcherTask
      extends
        CyclicTask {

    /**
     * The poll timeout.
     */
    private final long timeout;

    /**
     * Creates a new instance.
     *
     * @param interval The time to wait between polls in ms.
     * @param timeout The timeout in ms for which to wait for events to arrive with each polling
     * call.
     */
    private EventFetcherTask(long interval, long timeout) {
      super(interval);
      this.timeout = checkInRange(timeout, 1, Long.MAX_VALUE, "timeout");
    }

    @Override
    protected void runActualTask() {
      boolean shutDown = false;
      try {
        LOG.debug("Fetching remote kernel for events");
        List<Object> events = servicePortal.fetchEvents(timeout);
        for (Object event : events) {
          LOG.debug("Processing fetched event: {}", event);
          // Forward received events to all registered listeners, but do that on the event
          // dispatcher thread. This ensures that GUI-/drawing-related code is executed on the
          // correct thread.
          SwingUtilities.invokeLater(() -> eventBus.onEvent(event));

          // Check if the kernel notifies us about a state change.
          if (event instanceof KernelStateTransitionEvent) {
            KernelStateTransitionEvent stateEvent = (KernelStateTransitionEvent) event;
            // If the kernel switches to SHUTDOWN, remember to shut down.
            shutDown = stateEvent.getEnteredState() == Kernel.State.SHUTDOWN;
          }
        }
      }
      catch (KernelRuntimeException exc) {
        LOG.error("Exception fetching events, logging out", exc);
        // Remember the connection problem by shutting it down properly.
        shutDown = true;
      }

      if (shutDown) {
        kernelClientApplication.offline();
      }
    }
  }
}
