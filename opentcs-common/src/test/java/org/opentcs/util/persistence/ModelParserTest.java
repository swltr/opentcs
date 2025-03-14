// SPDX-FileCopyrightText: The openTCS Authors
// SPDX-License-Identifier: MIT
package org.opentcs.util.persistence;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;
import org.opentcs.access.to.model.PlantModelCreationTO;

/**
 * Test for {@link ModelParser}.
 */
public class ModelParserTest {

  private static final String WRITE_PATH = "src/test/java/org/opentcs/util/persistence/";
  private final ModelParser modelParser = new ModelParser();

  @Test
  public void readModelV6AndWriteLatestVersion()
      throws URISyntaxException,
        IOException {
    PlantModelCreationTO parsedModel = modelParser.readModel(
        new File(
            Thread.currentThread().getContextClassLoader()
                .getResource("org/opentcs/util/persistence/PlantModelV6.sample.xml").toURI()
        )
    );

    File writtenModel = new File(
        WRITE_PATH + "ModelParserTest.readModelV6AndWriteLatestVersion.received.xml"
    );
    modelParser.writeModel(parsedModel, writtenModel);

    Approvals.verify(writtenModel);
  }

  @Test
  public void readModelV005AndWriteLatestVersion()
      throws IOException,
        URISyntaxException {
    PlantModelCreationTO parsedModel = modelParser.readModel(
        new File(
            Thread.currentThread().getContextClassLoader()
                .getResource("org/opentcs/util/persistence/PlantModelV005.sample.xml").toURI()
        )
    );

    File writtenModel = new File(
        WRITE_PATH + "ModelParserTest.readModelV005AndWriteLatestVersion.received.xml"
    );
    modelParser.writeModel(parsedModel, writtenModel);

    Approvals.verify(writtenModel);
  }

  @Test
  public void readModelV004AndWriteLatestVersion()
      throws IOException,
        URISyntaxException {
    PlantModelCreationTO parsedModel = modelParser.readModel(
        new File(
            Thread.currentThread().getContextClassLoader()
                .getResource("org/opentcs/util/persistence/PlantModelV004.sample.xml").toURI()
        )
    );

    File writtenModel = new File(
        WRITE_PATH + "ModelParserTest.readModelV004AndWriteLatestVersion.received.xml"
    );
    modelParser.writeModel(parsedModel, writtenModel);

    Approvals.verify(writtenModel);
  }
}
