// SPDX-FileCopyrightText: The openTCS Authors
// SPDX-License-Identifier: MIT
package org.opentcs.guing.base.components.properties.type;

/**
 * Interface for properties.
 * Wraps a type into a property to be able to change a value without creating a new object.
 * The property object stays the same while the value changes.
 */
public interface Property
    extends
      ModelAttribute,
      Cloneable {

  /**
   * Copies the value of the property into this property.
   *
   * @param property The property.
   */
  void copyFrom(Property property);

  /**
   * Returns a comparable represantation of the value of this property.
   *
   * @return A represantation to compare this property to other ones.
   */
  Object getComparableValue();

  /**
   * Creates a copy of this property.
   *
   * @return The cloned property.
   */
  Object clone();
}
