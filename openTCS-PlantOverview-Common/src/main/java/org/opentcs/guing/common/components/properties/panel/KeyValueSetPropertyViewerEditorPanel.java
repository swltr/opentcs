/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.guing.common.components.properties.panel;

import com.google.inject.Inject;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import org.opentcs.guing.base.components.properties.type.KeyValueSetProperty;
import org.opentcs.guing.base.components.properties.type.Property;
import org.opentcs.guing.common.components.dialogs.DetailsDialogContent;
import org.opentcs.guing.common.util.I18nPlantOverview;
import org.opentcs.thirdparty.guing.common.jhotdraw.util.ResourceBundleUtil;

/**
 * UI for viewing a key value property without being able to edit the property.
 *
 * @see KeyValueSetProperty
 */
public class KeyValueSetPropertyViewerEditorPanel
    extends
      JPanel
    implements
      DetailsDialogContent {

  /**
   * A resource bundle.
   */
  private final ResourceBundleUtil resBundle
      = ResourceBundleUtil.getBundle(I18nPlantOverview.PROPERTIES_PATH);
  /**
   * The property edited.
   */
  private KeyValueSetProperty fProperty;

  /**
   * Creates a new instance.
   */
  @Inject
  @SuppressWarnings("this-escape")
  public KeyValueSetPropertyViewerEditorPanel() {
    initComponents();

    itemsTable.setModel(new ItemsTableModel());

    setPreferredSize(new Dimension(350, 200));
  }

  @Override
  public void setProperty(Property property) {
    fProperty = (KeyValueSetProperty) property;

    ItemsTableModel model = (ItemsTableModel) itemsTable.getModel();

    model.setRowCount(0);

    getProperty().getItems().stream()
        .sorted((p1, p2) -> p1.getKey().compareTo(p2.getKey()))
        .forEach(
            keyValueProperty -> model.addRow(
                new String[]{keyValueProperty.getKey(), keyValueProperty.getValue()}
            )
        );

    sortItems();
  }

  @Override
  public void updateValues() {
  }

  @Override
  public String getTitle() {
    return resBundle.getString("keyValueSetPropertyViewerEditorPanel.title");
  }

  @Override
  public KeyValueSetProperty getProperty() {
    return fProperty;
  }

  /**
   * Sorts the entries based on their keys.
   */
  protected void sortItems() {
    Map<String, String> items = new HashMap<>();

    for (int i = 0; i < itemsTable.getRowCount(); i++) {
      items.put((String) itemsTable.getValueAt(i, 0), (String) itemsTable.getValueAt(i, 1));
    }

    int index = 0;

    for (String key : new TreeSet<>(items.keySet())) {
      String value = items.get(key);

      itemsTable.setValueAt(key, index, 0);
      itemsTable.setValueAt(value, index, 1);

      index++;
    }
  }

  // FORMATTER:OFF
  // CHECKSTYLE:OFF
  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    itemsScrollPane = new javax.swing.JScrollPane();
    itemsTable = new javax.swing.JTable();
    controlPanel = new javax.swing.JPanel();

    setLayout(new java.awt.BorderLayout());

    itemsTable.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    itemsScrollPane.setViewportView(itemsTable);

    add(itemsScrollPane, java.awt.BorderLayout.CENTER);

    controlPanel.setLayout(new java.awt.GridBagLayout());
    add(controlPanel, java.awt.BorderLayout.EAST);
  }// </editor-fold>//GEN-END:initComponents
  // CHECKSTYLE:ON
  // FORMATTER:ON

  // FORMATTER:OFF
  // CHECKSTYLE:OFF
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel controlPanel;
  private javax.swing.JScrollPane itemsScrollPane;
  private javax.swing.JTable itemsTable;
  // End of variables declaration//GEN-END:variables
  // CHECKSTYLE:ON
  // FORMATTER:ON

  protected class ItemsTableModel
      extends
        DefaultTableModel {

    private final Class<?>[] types = new Class<?>[]{
        String.class, String.class
    };

    public ItemsTableModel() {
      super(
          new Object[][]{},
          new String[]{
              resBundle.getString(
                  "keyValueSetPropertyViewerEditorPanel.table_properties.column_key.headerText"
              ),
              resBundle.getString(
                  "keyValueSetPropertyViewerEditorPanel.table_properties.column_value.headerText"
              )
          }
      );
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
      return types[columnIndex];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return false;
    }
  }

}
