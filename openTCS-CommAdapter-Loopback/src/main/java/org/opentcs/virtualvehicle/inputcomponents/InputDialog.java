/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.virtualvehicle.inputcomponents;

import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;

/**
 * A generic dialog for user input. It has two or three buttons
 * (<i>Ok</i>, <i>Cancel</i>, <i>reset</i>(optional)) and acts as a frame for an
 * {@link InputPanel}, that must be provided when
 * creating the dialog. <code>InputDialog</code> listens to
 * <code>InputPanel</code>'s {@link ValidationEvent} to dynamically
 * enable/disable the <i>ok</i>-button if the panel validates it's input.
 */
public class InputDialog
    extends javax.swing.JDialog
    implements ValidationListener {

  /**
   * The panel that contains the content of the dialog.
   */
  private final InputPanel panel;
  /**
   * Return status of the dialog. Will be set when the dialog is beeing closed.
   */
  private ReturnStatus returnStatus;

  /**
   * Enum values to indicate if the dialog accepted input from the user or
   * if the input was canceled.
   * TODO: no enum needed here?
   */
  public enum ReturnStatus {

    /**
     * The Dialog was cancelled. No input available.
     */
    CANCELED,
    /**
     * The input was accepted and is available.
     */
    ACCEPTED
  }

  /**
   * Create a new instance of <code>InputDialog</code>.
   * @param panel the panel to be displayed in the dialog.
   */
  @SuppressWarnings("this-escape")
  public InputDialog(InputPanel panel) {
    super();
    initComponents();
    setLocationRelativeTo(null);
    // Set up embedded panel
    this.panel = panel;
    setTitle(panel.getTitle());
    panel.setBorder(new EmptyBorder(6, 6, 10, 6));
    // Setup dialog
    getContentPane().add(panel, BorderLayout.CENTER);
    getRootPane().setDefaultButton(okButton);
    if (!panel.isResetable()) {
      resetButton.setVisible(false);
    }
    pack();
    // Init validation value manually
    panel.addValidationListener(this);
  }

  @Override
  public void validityChanged(ValidationEvent e) {
    okButton.setEnabled(e.valid());
  }

  /**
   * Get the return status of the dialog that indicates if there is input
   * available via {@link #getInput()}.
   * If the return status is {@link ReturnStatus#ACCEPTED ACCEPTED},
   * the panels input was captured
   * or reset and is available through {@link #getInput()}.
   * If the return status is {@link ReturnStatus#CANCELED CANCELLED},
   * {@link #getInput()} should <b>not</b>
   * be called as the dialog was canceled and there is no valid input available.
   * If the dialog wasn't closed yet, <code>null</code> will be returned.
   * @return the return status
   */
  public ReturnStatus getReturnStatus() {
    return returnStatus;
  }

  /**
   * Get the input from the embedded panel. This is the same as calling
   * {@link InputPanel#getInput()} directly.
   * Prior to calling this method you should check if there even is any input
   * (see {@link #getReturnStatus()}).
   * @return The input from the panel
   */
  public Object getInput() {
    return panel.getInput();
  }

  // CHECKSTYLE:OFF
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    buttonPanel = new javax.swing.JPanel();
    okButton = new javax.swing.JButton();
    cancelButton = new javax.swing.JButton();
    resetButton = new javax.swing.JButton();

    setModal(true);
    setResizable(false);
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        dialogClosing(evt);
      }
    });

    buttonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

    okButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
    java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("i18n/org/opentcs/commadapter/loopback/Bundle"); // NOI18N
    okButton.setText(bundle.getString("inputDialog.button_ok.text")); // NOI18N
    okButton.setName("inputDialogOkButton"); // NOI18N
    okButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        okButtonActionPerformed(evt);
      }
    });
    buttonPanel.add(okButton);

    cancelButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
    cancelButton.setText(bundle.getString("inputDialog.button_cancel.text")); // NOI18N
    cancelButton.setName("inputDialogCancelButton"); // NOI18N
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelButtonActionPerformed(evt);
      }
    });
    buttonPanel.add(cancelButton);

    resetButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
    resetButton.setText(bundle.getString("inputDialog.button_reset.text")); // NOI18N
    resetButton.setName("inputDialogResetButton"); // NOI18N
    resetButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        resetButtonActionPerformed(evt);
      }
    });
    buttonPanel.add(resetButton);

    getContentPane().add(buttonPanel, java.awt.BorderLayout.PAGE_END);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
    doClose(ReturnStatus.CANCELED);
  }//GEN-LAST:event_cancelButtonActionPerformed

  private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
    panel.captureInput();
    doClose(ReturnStatus.ACCEPTED);
  }//GEN-LAST:event_okButtonActionPerformed

  private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
    panel.doReset();
    doClose(ReturnStatus.ACCEPTED);
  }//GEN-LAST:event_resetButtonActionPerformed

  /**
   * Handler for the WindowClosing event.
   * Called when the user closes the dialog via the X-Button or F4.
   * @param evt WindowEvent
   */
  private void dialogClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_dialogClosing
    doClose(ReturnStatus.CANCELED);
  }//GEN-LAST:event_dialogClosing

  /**
   * Close the dialog properly and set the return status to indicate
   * how/why it was closed.
   */
  private void doClose(ReturnStatus retStatus) {
    returnStatus = retStatus;
    setVisible(false);
    dispose();
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel buttonPanel;
  private javax.swing.JButton cancelButton;
  private javax.swing.JButton okButton;
  private javax.swing.JButton resetButton;
  // End of variables declaration//GEN-END:variables
  // CHECKSTYLE:ON
}
