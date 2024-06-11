/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.virtualvehicle.inputcomponents;

import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * An input panel with a text field for user input as well as a list of predefined
 * inputs to select from.
 */
public final class TextListInputPanel
    extends
      TextInputPanel {

  /**
   * Creates a new instance TextListInputPanel.
   *
   * @param title the title of the panel
   */
  private TextListInputPanel(String title) {
    super(title);
    resetable = false;
    initComponents();
    list.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
          Object selection = list.getSelectedValue();
          if (selection != null) {
            inputField.setText((String) selection);
          }
        }
      }
    });
  }

  @Override
  protected void captureInput() {
    input = inputField.getText();
  }

  /**
   * Enable input validation against the given regular expression.
   *
   * @see InputPanel#addValidationListener
   * @param format A regular expression.
   */
  private void enableInputValidation(String format) {
    if (format != null) {
      inputField.getDocument().addDocumentListener(new TextInputValidator(format));
    }
  }

  // FORMATTER:OFF
  // CHECKSTYLE:OFF
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    messageLabel = new javax.swing.JLabel();
    inputField = new javax.swing.JTextField();
    jScrollPane1 = new javax.swing.JScrollPane();
    list = new javax.swing.JList<String>();

    setLayout(new java.awt.GridBagLayout());

    messageLabel.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
    messageLabel.setText("Message");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
    add(messageLabel, gridBagConstraints);

    inputField.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
    inputField.setPreferredSize(new java.awt.Dimension(70, 20));
    inputField.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(java.awt.event.FocusEvent evt) {
        inputFieldFocusGained(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
    add(inputField, gridBagConstraints);

    list.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
    list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    jScrollPane1.setViewportView(list);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    add(jScrollPane1, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents
  // CHECKSTYLE:ON
  // FORMATTER:ON

  private void inputFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inputFieldFocusGained
    inputField.selectAll();
  }//GEN-LAST:event_inputFieldFocusGained

  // CHECKSTYLE:OFF
  // FORMATTER:OFF
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextField inputField;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JList<String> list;
  private javax.swing.JLabel messageLabel;
  // End of variables declaration//GEN-END:variables
  // CHECKSTYLE:ON
  // FORMATTER:ON

  /**
   * See {@link InputPanel.Builder}.
   */
  public static class Builder
      implements
        InputPanel.Builder {

    /**
     * The panel's title.
     */
    private final String title;
    /**
     * The optional message.
     */
    private String message;
    /**
     * Content for the dropdown list.
     */
    private final List<String> content;
    /**
     * Regex for validation of the text field's content.
     */
    private String format;
    /**
     * Initially selected index of the list.
     * Default is 0.
     */
    private int initialIndex;

    /**
     * Create a new <code>Builder</code>.
     *
     * @param title the title of the panel
     * @param content Predefined items to display in the panel's list
     */
    public Builder(String title, List<String> content) {
      this.title = title;
      this.content = content;
    }

    /**
     * Set the message of the panel.
     * The user of this method must take care for the line breaks in the message,
     * as it is not wrapped automatically!
     *
     * @param message the message
     * @return the instance of this <code>Builder</code>
     */
    public Builder setMessage(String message) {
      this.message = message;
      return this;
    }

    /**
     * Make the panel validate it's input.
     *
     * @param format The regular expression that will be used for validation.
     * @return the instance of this <code>Builder</code>
     */
    public Builder enableValidation(String format) {
      this.format = format;
      return this;
    }

    /**
     * Set the initial selected list entry.
     *
     * @param index must be &gt; 0, will have no effect otherwise
     * @return the instance of this <code>Builder</code>
     */
    public Builder setInitialSelection(int index) {
      if (index >= 0) {
        initialIndex = index;
      }
      return this;
    }

    /**
     * Set the initial selected list entry.
     *
     * @param element Element to select. Selection remains unchanged if
     * element ist not in the list or element is
     * <code>null</code>.
     * @return the instance fo this <code>Builder</code>
     */
    public Builder setInitialSelection(String element) {
      int index;
      try {
        index = content.indexOf(element);
      }
      catch (NullPointerException e) {
        index = -1;
      }
      return setInitialSelection(index);
    }

    @Override
    public TextListInputPanel build() {
      TextListInputPanel panel = new TextListInputPanel(title);
      panel.enableInputValidation(format);
      panel.messageLabel.setText(message);
      panel.list.setListData(content.toArray(new String[content.size()]));
      panel.list.setSelectedIndex(initialIndex);
      return panel;
    }
  }
}
