package com.ev.management.demo.service;

import com.ev.management.demo.dto.VehicleDTO;
import com.ev.management.demo.entity.*;
import com.ev.management.demo.repository.*;
import com.ev.management.demo.util.LocationUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private FuelEligibilityRepository fuelEligibilityRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UtilityRepository utilityRepository;

    @Autowired
    private VehicleModelRepository vehicleModelRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Transactional
    public Vehicle addVehicle(VehicleDTO dto) {
        if (vehicleRepository.findByVin(dto.getVin()).isPresent()) {
            throw new IllegalArgumentException("Vehicle with VIN already exists.");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setVin(dto.getVin());
        vehicle.setElectricRange(dto.getElectricRange());
        vehicle.setBaseMsrp(dto.getBaseMsrp());
        vehicle.setLegislativeDistrict(dto.getLegislativeDistrict());
        vehicle.setDolVehicleId(dto.getDolVehicleId());

        // FuelEligibility
        FuelEligibility fuelEligibility = fuelEligibilityRepository
                .findByDescription(dto.getFuelEligibility())
                .orElseGet(() -> {
                    FuelEligibility fe = new FuelEligibility();
                    fe.setDescription(dto.getFuelEligibility());
                    return fuelEligibilityRepository.save(fe);
                });
        vehicle.setFuelEligibility(fuelEligibility);

        if (dto.getPostalCode() != null && dto.getCity()!= null && dto.getCounty() != null && dto.getState() != null) {
            Location location = locationRepository.findByPostalCodeAndCityAndCountyAndStateCode(dto.getPostalCode(), dto.getCity(), dto.getCounty(), dto.getState())
                    .orElseGet(() -> {
                        Location loc = new Location();
                        loc.setCity(dto.getCity());
                        loc.setCounty(dto.getCounty());
                        loc.setStateCode(dto.getState());
                        loc.setPostalCode(dto.getPostalCode());
                        loc.setCoordinates(LocationUtil.getLocation(dto.getVehicleLocation()));
                        loc.setCensusTract(dto.getCensusTract());
                        return locationRepository.save(loc);
                    });
            vehicle.setLocation(location);
        }

        // Split the CSV into utility names
        String[] utilityNames = dto.getUtility().split("\\|");

        // Get or create utilities
        List<Utility> utilities = new ArrayList<>();
        for (String utilityName : utilityNames) {
            Optional<Utility> utility = utilityRepository.findByName(utilityName.trim());
            if (utility.isEmpty()) {
                Utility ut = new Utility();
                ut.setName(utilityName.trim());
                utilities.add(utilityRepository.save(ut));
            } else {
                utilities.add(utility.get());
            }
        }

        // Set the utilities for the vehicle
        vehicle.setUtilities(utilities);

        // VehicleModel
        VehicleModel model = vehicleModelRepository
                .findByMakeIgnoreCaseAndModelIgnoreCaseAndModelYear(dto.getMake(), dto.getModel(), dto.getModelYear())
                .orElseGet(() -> {
                    VehicleModel vm = new VehicleModel();
                    vm.setMake(dto.getMake());
                    vm.setModel(dto.getModel());
                    vm.setModelYear(dto.getModelYear());
                    return vehicleModelRepository.save(vm);
                });
        vehicle.setModel(model);

        // VehicleType
        VehicleType vehicleType = vehicleTypeRepository.findByType(dto.getVehicleType())
                .orElseGet(() -> {
                    VehicleType vt = new VehicleType();
                    vt.setType(dto.getVehicleType());
                    return vehicleTypeRepository.save(vt);
                });
        vehicle.setType(vehicleType);
        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public Vehicle updateVehicle(VehicleDTO dto) {
        // Find existing vehicle by VIN
        Vehicle vehicle = vehicleRepository.findByVin(dto.getVin())
                .orElseThrow(() -> new EntityNotFoundException("Vehicle with VIN " + dto.getVin() + " not found"));

        // Update fields
        vehicle.setVin(dto.getVin());
        vehicle.setElectricRange(dto.getElectricRange());
        vehicle.setBaseMsrp(dto.getBaseMsrp());
        vehicle.setLegislativeDistrict(dto.getLegislativeDistrict());
        vehicle.setDolVehicleId(dto.getDolVehicleId());

        vehicle.setFuelEligibility(
                fuelEligibilityRepository.findByDescription(dto.getFuelEligibility())
                        .orElseThrow(() -> new EntityNotFoundException("Fuel eligibility not found: " + dto.getFuelEligibility()))
        );
        vehicle.getFuelEligibility().setDescription(dto.getFuelEligibility());

        vehicle.setLocation(
                locationRepository.findByPostalCodeAndCityAndCountyAndStateCode(dto.getPostalCode(), dto.getCity(), dto.getCounty(), dto.getState())
                        .orElseThrow(() -> new EntityNotFoundException("Location not found: " + dto.getPostalCode()))
        );
        Location loc = vehicle.getLocation();
        loc.setCity(dto.getCity());
        loc.setCounty(dto.getCounty());
        loc.setStateCode(dto.getState());
        loc.setPostalCode(dto.getPostalCode());
        loc.setCoordinates(LocationUtil.getLocation(dto.getVehicleLocation()));
        loc.setCensusTract(dto.getCensusTract());

        // Split the CSV into utility names
        String[] utilityNames = dto.getUtility().split("\\|");

        // Get or create utilities
        List<Utility> utilities = new ArrayList<>();
        for (String utilityName : utilityNames) {
            Optional<Utility> utility = utilityRepository.findByName(utilityName.trim());
            if (utility.isEmpty()) {
                Utility ut = new Utility();
                ut.setName(utilityName.trim());
                utilities.add(utilityRepository.save(ut));
            } else {
                utilities.add(utility.get());
            }
        }

        // Set the utilities for the vehicle
        vehicle.setUtilities(utilities);

        vehicle.setModel(
                vehicleModelRepository.findByMakeIgnoreCaseAndModelIgnoreCaseAndModelYear(dto.getMake(), dto.getModel(), dto.getModelYear())
                        .orElseThrow(() -> new EntityNotFoundException("Vehicle model not found: " + dto.getMake() + " " + dto.getModel()))
        );
        VehicleModel vm = vehicle.getModel();
        vm.setMake(dto.getMake());
        vm.setModel(dto.getModel());
        vm.setModelYear(dto.getModelYear());

        vehicle.setType(
                vehicleTypeRepository.findByType(dto.getVehicleType())
                        .orElseThrow(() -> new EntityNotFoundException("Vehicle type not found: " + dto.getVehicleType()))
        );
        vehicle.getType().setType(dto.getVehicleType());

        // Save updated entity
        return vehicleRepository.save(vehicle);
    }


    public Vehicle getVehicleByVin(String vin) {
        return vehicleRepository.findByVin(vin)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with VIN: " + vin));
    }

    public Page<Vehicle> getAllVehicles(int page, int size, String sortBy, String direction) {
        if(sortBy == null) {
            sortBy = "id";
        }
        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return vehicleRepository.findAll(pageable);
    }

    @Transactional
    public void deleteVehicleByVin(String vin) {
        if (!vehicleRepository.existsByVin(vin)) {
            throw new EntityNotFoundException("Vehicle with VIN " + vin + " not found");
        }
        vehicleRepository.deleteByVin(vin);
    }

    // Service method to fetch vehicles by modelId
    @Transactional
    public void updateVehicleMSRPByModelId(String model, BigDecimal newMsrp) {
        Optional<List<VehicleModel>> vehicleModelOptional = vehicleModelRepository.findByModelIgnoreCase(model);
        if(vehicleModelOptional.isEmpty()) {
            throw new EntityNotFoundException("Vehicle Model " + model + " not found");
        }

        List<Vehicle> allMatchingVehicles = new ArrayList<>();
        for(VehicleModel vehicleModel: vehicleModelOptional.get()) {
            List<Vehicle> vehicles = vehicleRepository.findByModelId(vehicleModel.getId());
            if(!vehicles.isEmpty()) {
                for(Vehicle vehicle: vehicles) {
                    vehicle.setBaseMsrp(newMsrp);
                }
            }
            allMatchingVehicles.addAll(vehicles);
        }
        vehicleRepository.saveAll(allMatchingVehicles);
    }
}
