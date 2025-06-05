package com.ev.management.demo.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(schema = "ev_management", name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String vin;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private VehicleModel model;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private VehicleType type;

    @ManyToOne
    @JoinColumn(name = "fuel_eligibility_id")
    private FuelEligibility fuelEligibility;

    @Column(name = "electric_range")
    private Integer electricRange;

    @Column(name = "base_msrp")
    private BigDecimal baseMsrp;

    @Column(name = "dol_vehicle_id")
    private Long dolVehicleId;

    @Column(name = "legislative_district")
    private String legislativeDistrict;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "utility_id")
    private Utility utility;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public VehicleModel getModel() {
        return model;
    }

    public void setModel(VehicleModel model) {
        this.model = model;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public FuelEligibility getFuelEligibility() {
        return fuelEligibility;
    }

    public void setFuelEligibility(FuelEligibility fuelEligibility) {
        this.fuelEligibility = fuelEligibility;
    }

    public Integer getElectricRange() {
        return electricRange;
    }

    public void setElectricRange(Integer electricRange) {
        this.electricRange = electricRange;
    }

    public BigDecimal getBaseMsrp() {
        return baseMsrp;
    }

    public void setBaseMsrp(BigDecimal baseMsrp) {
        this.baseMsrp = baseMsrp;
    }

    public Long getDolVehicleId() {
        return dolVehicleId;
    }

    public void setDolVehicleId(Long dolVehicleId) {
        this.dolVehicleId = dolVehicleId;
    }

    public String getLegislativeDistrict() {
        return legislativeDistrict;
    }

    public void setLegislativeDistrict(String legislativeDistrict) {
        this.legislativeDistrict = legislativeDistrict;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Utility getUtility() {
        return utility;
    }

    public void setUtility(Utility utility) {
        this.utility = utility;
    }
}
