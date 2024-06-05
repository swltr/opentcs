/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.modeleditor.components.dialog;

import org.opentcs.guing.base.model.elements.PathModel;
import static org.opentcs.guing.base.model.elements.PathModel.Type.BEZIER;
import static org.opentcs.guing.base.model.elements.PathModel.Type.BEZIER_3;
import static org.opentcs.guing.base.model.elements.PathModel.Type.DIRECT;
import static org.opentcs.guing.base.model.elements.PathModel.Type.ELBOW;
import static org.opentcs.guing.base.model.elements.PathModel.Type.POLYPATH;
import static org.opentcs.guing.base.model.elements.PathModel.Type.SLANTED;
import org.opentcs.guing.common.components.dialogs.DialogContent;
import org.opentcs.modeleditor.util.I18nPlantOverviewModeling;
import org.opentcs.thirdparty.guing.common.jhotdraw.util.ResourceBundleUtil;

/**
 * A dialog content to select a set of path types.
 */
public class PathTypeSelectionPanel
    extends DialogContent {

  /**
   * Creates new form PathTypeSelection.
   */
  @SuppressWarnings("this-escape")
  public PathTypeSelectionPanel() {
    setDialogTitle(ResourceBundleUtil.getBundle(I18nPlantOverviewModeling.MISC_PATH)
        .getString("pathTypeSelection.title"));
    initComponents();
  }

  @Override
  public void initFields() {
  }

  @Override
  public void update() {
  }

  /**
   * Test if the given path type is selected.
   *
   * @param type The path type to test.
   * @return True if the path type is selected.
   */
  public boolean isPathTypeSelected(PathModel.Type type) {
    switch (type) {
      case DIRECT:
        return directCheckBox.isSelected();
      case ELBOW:
        return elbowCheckBox.isSelected();
      case SLANTED:
        return slantedCheckBox.isSelected();
      case POLYPATH:
        return polypathCheckBox.isSelected();
      case BEZIER:
        return bezierCheckBox.isSelected();
      case BEZIER_3:
        return bezier3CheckBox.isSelected();
      default:
        return false;
    }
  }

  private void updateAllTypesCheckBox() {
    allTypesCheckBox.setSelected(
        directCheckBox.isSelected()
        && elbowCheckBox.isSelected()
        && slantedCheckBox.isSelected()
        && polypathCheckBox.isSelected()
        && bezierCheckBox.isSelected()
        && bezier3CheckBox.isSelected()
    );
  }

  // CHECKSTYLE:OFF
  /**
   * This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    allTypesCheckBox = new javax.swing.JCheckBox();
    directCheckBox = new javax.swing.JCheckBox();
    elbowCheckBox = new javax.swing.JCheckBox();
    slantedCheckBox = new javax.swing.JCheckBox();
    polypathCheckBox = new javax.swing.JCheckBox();
    bezierCheckBox = new javax.swing.JCheckBox();
    bezier3CheckBox = new javax.swing.JCheckBox();

    setLayout(new java.awt.GridBagLayout());

    java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("i18n/org/opentcs/plantoverview/modeling/miscellaneous"); // NOI18N
    allTypesCheckBox.setText(bundle.getString("pathTypeSelection.allTypes_label.text")); // NOI18N
    allTypesCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    allTypesCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
    allTypesCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        allTypesCheckBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    add(allTypesCheckBox, gridBagConstraints);

    directCheckBox.setText("Direct:");
    directCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    directCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
    directCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        directCheckBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
    add(directCheckBox, gridBagConstraints);

    elbowCheckBox.setText("Elbow:");
    elbowCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    elbowCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
    elbowCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        elbowCheckBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
    add(elbowCheckBox, gridBagConstraints);

    slantedCheckBox.setText("Slanted:");
    slantedCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    slantedCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
    slantedCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        slantedCheckBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
    add(slantedCheckBox, gridBagConstraints);

    polypathCheckBox.setText("Polypath:");
    polypathCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    polypathCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
    polypathCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        polypathCheckBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
    add(polypathCheckBox, gridBagConstraints);

    bezierCheckBox.setText("Bezier:");
    bezierCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    bezierCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
    bezierCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bezierCheckBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
    add(bezierCheckBox, gridBagConstraints);

    bezier3CheckBox.setText("Bezier 3:");
    bezier3CheckBox.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    bezier3CheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
    bezier3CheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bezier3CheckBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
    add(bezier3CheckBox, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents
  // CHECKSTYLE:ON

  private void allTypesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allTypesCheckBoxActionPerformed
    directCheckBox.setSelected(allTypesCheckBox.isSelected());
    elbowCheckBox.setSelected(allTypesCheckBox.isSelected());
    slantedCheckBox.setSelected(allTypesCheckBox.isSelected());
    polypathCheckBox.setSelected(allTypesCheckBox.isSelected());
    bezierCheckBox.setSelected(allTypesCheckBox.isSelected());
    bezier3CheckBox.setSelected(allTypesCheckBox.isSelected());
  }//GEN-LAST:event_allTypesCheckBoxActionPerformed

  private void directCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_directCheckBoxActionPerformed
    updateAllTypesCheckBox();
  }//GEN-LAST:event_directCheckBoxActionPerformed

  private void elbowCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elbowCheckBoxActionPerformed
    updateAllTypesCheckBox();
  }//GEN-LAST:event_elbowCheckBoxActionPerformed

  private void slantedCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_slantedCheckBoxActionPerformed
    updateAllTypesCheckBox();
  }//GEN-LAST:event_slantedCheckBoxActionPerformed

  private void polypathCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_polypathCheckBoxActionPerformed
    updateAllTypesCheckBox();
  }//GEN-LAST:event_polypathCheckBoxActionPerformed

  private void bezierCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bezierCheckBoxActionPerformed
    updateAllTypesCheckBox();
  }//GEN-LAST:event_bezierCheckBoxActionPerformed

  private void bezier3CheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bezier3CheckBoxActionPerformed
    updateAllTypesCheckBox();
  }//GEN-LAST:event_bezier3CheckBoxActionPerformed

  // CHECKSTYLE:OFF
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JCheckBox allTypesCheckBox;
  private javax.swing.JCheckBox bezier3CheckBox;
  private javax.swing.JCheckBox bezierCheckBox;
  private javax.swing.JCheckBox directCheckBox;
  private javax.swing.JCheckBox elbowCheckBox;
  private javax.swing.JCheckBox polypathCheckBox;
  private javax.swing.JCheckBox slantedCheckBox;
  // End of variables declaration//GEN-END:variables
  // CHECKSTYLE:ON
}
