// SPDX-FileCopyrightText: The openTCS Authors
// SPDX-License-Identifier: MIT
package org.opentcs.kernelcontrolcenter.vehicles;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.opentcs.drivers.vehicle.VehicleCommAdapterDescription;

/**
 * A {@link TableCellRenderer} for {@link VehicleCommAdapterDescription} instances.
 * This class provides a representation of any VehicleCommAdapterDescription instance by writing
 * its actual description on a JLabel.
 */
class VehicleCommAdapterFactoryTableCellRenderer
    extends
      DefaultTableCellRenderer {

  VehicleCommAdapterFactoryTableCellRenderer() {
  }

  @Override
  public Component getTableCellRendererComponent(
      JTable table,
      Object value,
      boolean isSelected,
      boolean hasFocus,
      int row,
      int column
  )
      throws IllegalArgumentException {

    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

    if (value == null) {
      setText("");
    }
    else if (value instanceof VehicleCommAdapterDescription) {
      setText(((VehicleCommAdapterDescription) value).getDescription());
    }
    else {
      throw new IllegalArgumentException("value");
    }
    return this;
  }

}
