package com.ev.management.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "ev_management", name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String county;
    private String city;

    @Column(name = "state_code", length = 2)
    private String stateCode;

    @Column(name = "postal_code", length = 5)
    private String postalCode;

    @Column(name = "census_tract")
    private String censusTract;

    // Optional: you can store the WKT string, or use PostGIS support with Hibernate Spatial
    @Column(columnDefinition = "geography(POINT,4326)")
    private String coordinates;

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

    public String getCensusTract() {
        return censusTract;
    }

    public void setCensusTract(String censusTract) {
        this.censusTract = censusTract;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}
