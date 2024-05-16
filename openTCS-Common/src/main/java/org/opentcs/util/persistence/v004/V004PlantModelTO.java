/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.util.persistence.v004;

import jakarta.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.requireNonNull;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.opentcs.util.persistence.BasePlantModelTO;
import org.xml.sax.SAXException;

/**
 */
@XmlRootElement(name = "model")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {"version", "name", "points", "paths", "vehicles", "locationTypes",
                      "locations", "blocks", "visualLayout", "properties"})
public class V004PlantModelTO
    extends BasePlantModelTO {

  private String name = "";
  private List<PointTO> points = new ArrayList<>();
  private List<PathTO> paths = new ArrayList<>();
  private List<VehicleTO> vehicles = new ArrayList<>();
  private List<LocationTypeTO> locationTypes = new ArrayList<>();
  private List<LocationTO> locations = new ArrayList<>();
  private List<BlockTO> blocks = new ArrayList<>();
  private VisualLayoutTO visualLayout = new VisualLayoutTO();
  private List<PropertyTO> properties = new ArrayList<>();

  /**
   * Creates a new instance.
   */
  public V004PlantModelTO() {
  }

  @XmlAttribute(required = true)
  public String getName() {
    return name;
  }

  public V004PlantModelTO setName(@Nonnull String name) {
    requireNonNull(name, "name");
    this.name = name;
    return this;
  }

  @XmlElement(name = "point")
  public List<PointTO> getPoints() {
    return points;
  }

  public V004PlantModelTO setPoints(@Nonnull List<PointTO> points) {
    requireNonNull(points, "points");
    this.points = points;
    return this;
  }

  @XmlElement(name = "path")
  public List<PathTO> getPaths() {
    return paths;
  }

  public V004PlantModelTO setPaths(@Nonnull List<PathTO> paths) {
    requireNonNull(paths, "paths");
    this.paths = paths;
    return this;
  }

  @XmlElement(name = "vehicle")
  public List<VehicleTO> getVehicles() {
    return vehicles;
  }

  public V004PlantModelTO setVehicles(@Nonnull List<VehicleTO> vehicles) {
    requireNonNull(vehicles, "vehicles");
    this.vehicles = vehicles;
    return this;
  }

  @XmlElement(name = "locationType")
  public List<LocationTypeTO> getLocationTypes() {
    return locationTypes;
  }

  public V004PlantModelTO setLocationTypes(@Nonnull List<LocationTypeTO> locationTypes) {
    requireNonNull(locationTypes, "locationTypes");
    this.locationTypes = locationTypes;
    return this;
  }

  @XmlElement(name = "location")
  public List<LocationTO> getLocations() {
    return locations;
  }

  public V004PlantModelTO setLocations(@Nonnull List<LocationTO> locations) {
    requireNonNull(locations, "locations");
    this.locations = locations;
    return this;
  }

  @XmlElement(name = "block")
  public List<BlockTO> getBlocks() {
    return blocks;
  }

  public V004PlantModelTO setBlocks(@Nonnull List<BlockTO> blocks) {
    requireNonNull(blocks, "blocks");
    this.blocks = blocks;
    return this;
  }

  @XmlElement
  public VisualLayoutTO getVisualLayout() {
    return visualLayout;
  }

  public V004PlantModelTO setVisualLayout(@Nonnull VisualLayoutTO visualLayout) {
    this.visualLayout = requireNonNull(visualLayout, "visualLayout");
    return this;
  }

  @XmlElement(name = "property")
  public List<PropertyTO> getProperties() {
    return properties;
  }

  public V004PlantModelTO setProperties(@Nonnull List<PropertyTO> properties) {
    requireNonNull(properties, "properties");
    this.properties = properties;
    return this;
  }

  /**
   * Marshals this instance to its XML representation and writes it to the given writer.
   *
   * @param writer The writer to write this instance's XML representation to.
   * @throws IOException If there was a problem marshalling this instance.
   */
  public void toXml(@Nonnull Writer writer)
      throws IOException {
    requireNonNull(writer, "writer");

    try {
      createMarshaller().marshal(this, writer);
    }
    catch (JAXBException | SAXException exc) {
      throw new IOException("Exception marshalling data", exc);
    }
  }

  /**
   * Unmarshals an instance of this class from the given XML representation.
   *
   * @param reader Provides the XML representation to parse to an instance.
   * @return The instance unmarshalled from the given reader.
   * @throws IOException If there was a problem unmarshalling the given string.
   */
  public static V004PlantModelTO fromXml(@Nonnull Reader reader)
      throws IOException {
    requireNonNull(reader, "reader");

    try {
      return (V004PlantModelTO) createUnmarshaller().unmarshal(reader);
    }
    catch (JAXBException | SAXException exc) {
      throw new IOException("Exception unmarshalling data", exc);
    }
  }

  private static Marshaller createMarshaller()
      throws JAXBException, SAXException {
    Marshaller marshaller = createContext().createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    marshaller.setSchema(createSchema());
    return marshaller;
  }

  private static Unmarshaller createUnmarshaller()
      throws JAXBException, SAXException {
    Unmarshaller unmarshaller = createContext().createUnmarshaller();
    unmarshaller.setSchema(createSchema());
    return unmarshaller;
  }

  private static JAXBContext createContext()
      throws JAXBException {
    return JAXBContext.newInstance(V004PlantModelTO.class);
  }

  private static Schema createSchema()
      throws SAXException {
    URL schemaUrl
        = V004PlantModelTO.class.getResource("/org/opentcs/util/persistence/model-0.0.4.xsd");
    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    return schemaFactory.newSchema(schemaUrl);
  }
}
