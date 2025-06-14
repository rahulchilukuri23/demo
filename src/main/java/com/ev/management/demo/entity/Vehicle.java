package com.ev.management.demo.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ev.management.demo.dto.VehicleDTO;
import com.ev.management.demo.util.LocationUtil;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "vehicle", schema = "ev_management")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vin", nullable = false, unique = true, length = 50)
    private String vin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false, referencedColumnName = "id")
    private VehicleModel model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false, referencedColumnName = "id")
    private VehicleType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fuel_eligibility_id", nullable = false, referencedColumnName = "id")
    private FuelEligibility fuelEligibility;

    @Column(name = "electric_range")
    private Integer electricRange;

    @Column(name = "base_msrp")
    private BigDecimal baseMsrp;

    @Column(name = "dol_vehicle_id")
    private Long dolVehicleId;

    @Column(name = "legislative_district")
    private Integer legislativeDistrict;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false, referencedColumnName = "id")
    private Location location;

    @Cascade(CascadeType.REMOVE)
    @ManyToMany
    @JoinTable(
            name = "vehicle_utility",
            schema = "ev_management",
            joinColumns = @JoinColumn(name = "vehicle_id"),
            inverseJoinColumns = @JoinColumn(name = "utility_id")
    )
    private List<Utility> utilities;

    // Constructors
    public Vehicle() {}

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

    public Integer getLegislativeDistrict() {
        return legislativeDistrict;
    }

    public void setLegislativeDistrict(Integer legislativeDistrict) {
        this.legislativeDistrict = legislativeDistrict;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Utility> getUtilities() {
        return utilities;
    }

    public void setUtilities(List<Utility> utilities) {
        this.utilities = utilities;
    }

    public VehicleDTO toDto() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setVin(this.getVin());
        vehicleDTO.setBaseMsrp(this.getBaseMsrp());
        vehicleDTO.setLegislativeDistrict(this.getLegislativeDistrict());
        vehicleDTO.setDolVehicleId(this.getDolVehicleId());

        Location location = this.getLocation();
        vehicleDTO.setState(location.getStateCode());
        vehicleDTO.setCounty(location.getCounty());
        vehicleDTO.setCity(location.getCity());
        vehicleDTO.setPostalCode(location.getPostalCode());
        vehicleDTO.setCensusTract(location.getCensusTract());
        vehicleDTO.setVehicleLocation(LocationUtil.getLocation(location.getCoordinates()));

        VehicleModel model = this.getModel();
        vehicleDTO.setMake(model.getMake());
        vehicleDTO.setModel(model.getModel());
        vehicleDTO.setModelYear(model.getModelYear());

        VehicleType vehicleType = this.getType();
        vehicleDTO.setVehicleType(vehicleType.getType());

        FuelEligibility fuelEligibility = this.getFuelEligibility();
        vehicleDTO.setFuelEligibility(fuelEligibility.getDescription());

        StringBuilder builder = new StringBuilder();
        Set<String> utilitySet = new HashSet<>();
        for(Utility utility: this.getUtilities()) {
            if(utilitySet.contains(utility.getName())) continue;
            builder.append(utility.getName()).append("|");
            utilitySet.add(utility.getName());
        }
        utilitySet.clear();

        if(builder.length() > 1)
            builder.setLength(builder.length()-1);
        vehicleDTO.setUtility(builder.toString());
        return vehicleDTO;
    }
}
