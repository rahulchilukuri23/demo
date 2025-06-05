package com.ev.management.demo.dto;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonRootName(value = "vehicle")
public class VehicleDTO {

    @JsonProperty("vin")
    public String vin;

    @JsonProperty("county")
    public String county;

    @JsonProperty("city")
    public String city;

    @JsonProperty("state")
    public String state;

    @JsonProperty("postal_code")
    public String postalCode;

    @JsonProperty("model_year")
    public int modelYear;

    @JsonProperty("make")
    public String make;

    @JsonProperty("model")
    public String model;

    @JsonProperty("vehicle_type")
    public String vehicleType;

    @JsonProperty("fuel_eligibility")
    public String fuelEligibility;

    @JsonProperty("electric_range")
    public int electricRange;

    @JsonProperty("base_msrp")
    public BigDecimal baseMsrp;

    @JsonProperty("legislative_district")
    public String legislativeDistrict;

    @JsonProperty("dol_vehicle_id")
    public long dolVehicleId;

    @JsonProperty("vehicle_location")
    public String vehicleLocation; // "POINT (lng lat)"

    @JsonProperty("utility")
    public String utility;

    @JsonProperty("census_tract")
    public String censusTract;

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getFuelEligibility() {
        return fuelEligibility;
    }

    public void setFuelEligibility(String fuelEligibility) {
        this.fuelEligibility = fuelEligibility;
    }

    public int getElectricRange() {
        return electricRange;
    }

    public void setElectricRange(int electricRange) {
        this.electricRange = electricRange;
    }

    public BigDecimal getBaseMsrp() {
        return baseMsrp;
    }

    public void setBaseMsrp(BigDecimal baseMsrp) {
        this.baseMsrp = baseMsrp;
    }

    public String getLegislativeDistrict() {
        return legislativeDistrict;
    }

    public void setLegislativeDistrict(String legislativeDistrict) {
        this.legislativeDistrict = legislativeDistrict;
    }

    public long getDolVehicleId() {
        return dolVehicleId;
    }

    public void setDolVehicleId(long dolVehicleId) {
        this.dolVehicleId = dolVehicleId;
    }

    public String getVehicleLocation() {
        return vehicleLocation;
    }

    public void setVehicleLocation(String vehicleLocation) {
        this.vehicleLocation = vehicleLocation;
    }

    public String getUtility() {
        return utility;
    }

    public void setUtility(String utility) {
        this.utility = utility;
    }

    public String getCensusTract() {
        return censusTract;
    }

    public void setCensusTract(String censusTract) {
        this.censusTract = censusTract;
    }
}
