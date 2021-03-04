/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.gradle.plugin.integration.builder;

import java.nio.file.Path;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import static org.opentcs.gradle.plugin.integration.Constants.RESOURCE_FOLDER_COMM_ADAPTER_VEHICLE;
import org.opentcs.gradle.plugin.integration.util.PluginFileUtils;

/**
 * Builds the vehicle comm adapter project.
 * 
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class VehicleCommAdapterProjectBuilder
    extends AbstractProjectBuilder {

  /**
   * This class' logger.
   */
  private static final Logger LOG = Logging.getLogger(VehicleCommAdapterProjectBuilder.class);
  
  public VehicleCommAdapterProjectBuilder(Path outputPath, Project projectBase) {
    super(outputPath, projectBase);
  }

  @Override
  public void build()
      throws Exception {
      PluginFileUtils.copyFileFromResources(RESOURCE_FOLDER_COMM_ADAPTER_VEHICLE,
                                             "build.gradle.tmpl",
                                             getOutputPath());
      PluginFileUtils.copyFileFromResources(RESOURCE_FOLDER_COMM_ADAPTER_VEHICLE,
                                             "src/main/java/de/fraunhofer/iml/opentcs/FOLDER_PACKAGE_NAME/vehicle/CommAdapterFactory.java.tmpl",
                                             getOutputPath());
      LOG.quiet("Vehicle comm adapter project built at '{}'.", getOutputPath());
  }
}
