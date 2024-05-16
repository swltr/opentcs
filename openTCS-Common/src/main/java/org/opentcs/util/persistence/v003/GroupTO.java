/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.util.persistence.v003;

import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.requireNonNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {"name", "members", "properties"})
public class GroupTO
    extends PlantModelElementTO {

  private List<MemberTO> members = new ArrayList<>();

  /**
   * Creates a new instance.
   */
  public GroupTO() {
  }

  @XmlElement(name = "member")
  public List<MemberTO> getMembers() {
    return members;
  }

  public GroupTO setMembers(@Nonnull List<MemberTO> members) {
    requireNonNull(members, "members");
    this.members = members;
    return this;
  }
}
