package com.ev.management.demo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "location", schema = "ev_management")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String county;

    @Column(length = 50)
    private String city;

    @Column(name = "state_code", length = 2)
    private String stateCode;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(name = "census_tract")
    private Long censusTract;

    @Column(columnDefinition = "geography(Point, 4326)")
    private Point coordinates;

    // Constructors
    public Location() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Long getCensusTract() {
        return censusTract;
    }

    public void setCensusTract(Long censusTract) {
        this.censusTract = censusTract;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }
}
