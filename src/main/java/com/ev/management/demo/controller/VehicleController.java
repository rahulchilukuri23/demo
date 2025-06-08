package com.ev.management.demo.controller;

import com.ev.management.demo.dto.VehicleDTO;
import com.ev.management.demo.entity.Vehicle;
import com.ev.management.demo.entity.VehicleModel;
import com.ev.management.demo.service.VehicleService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {
    Logger logger = LoggerFactory.getLogger(VehicleController.class);
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/add")
    public ResponseEntity<VehicleDTO> addVehicle(@RequestBody VehicleDTO dto) {
        logger.debug("Adding a vehicle of model {}, make {}, year {}", dto.getModel(), dto.getMake(), dto.getModelYear());
        Vehicle vehicle = vehicleService.addVehicle(dto);
        logger.debug("Adding successfully vehicle of model {}, make {}, year {}", dto.getModel(), dto.getMake(), dto.getModelYear());
        return ResponseEntity.ok(vehicle.toDto());
    }

    @GetMapping("/all")
    public Page<VehicleDTO> getAllVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        logger.debug("Fetching vehicles with page {}, size {}, sortBy {}", page, size, sortBy);
        Page<Vehicle> vehiclePage = vehicleService.getAllVehicles(page, size, sortBy, direction);
        return vehiclePage.map(Vehicle::toDto);
    }

    @GetMapping("/{vin}")
    public ResponseEntity<VehicleDTO> getVehicle(@PathVariable String vin) {
        logger.debug("Fetching a vehicle for a vin");
        Vehicle vehicle = vehicleService.getVehicleByVin(vin);
        VehicleModel model = vehicle.getModel();
        logger.debug("Fetched successfully vehicle of model {}, make {}, year {}", model.getModel(), model.getMake(), model.getModelYear());
        return ResponseEntity.ok(vehicle.toDto());
    }

    @PutMapping("/update/{vin}")
    public ResponseEntity<VehicleDTO> updateVehicle(@RequestBody VehicleDTO dto) {
        logger.debug("Updating a vehicle of model {}, make {}, year {}", dto.getModel(), dto.getMake(), dto.getModelYear());
        Vehicle vehicle = vehicleService.updateVehicle(dto);
        logger.debug("Updated successfully vehicle of model {}, make {}, year {}", dto.getModel(), dto.getMake(), dto.getModelYear());
        return ResponseEntity.ok(vehicle.toDto());
    }

    @PutMapping("/update/msrp/{model}")
    public ResponseEntity<String> updateVehicleMSRP(@PathVariable String model, @RequestBody VehicleDTO msrpdto) {
        try {
            logger.debug("Updating a vehicle of model {} with msrp {}", msrpdto.getModel(), msrpdto.getBaseMsrp());
            vehicleService.updateVehicleMSRPByModelId(model, msrpdto.getBaseMsrp());
            logger.debug("Update successful vehicle of model {} with msrp {}", msrpdto.getModel(), msrpdto.getBaseMsrp());
            return ResponseEntity.ok("Vehicle MSRP updated for all vehicles matching model " + model);
        } catch(EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{vin}")
    public ResponseEntity<String> deleteVehicle(@PathVariable String vin) {
        logger.debug("Deleting a vehicle for a vin");
        vehicleService.deleteVehicleByVin(vin);
        logger.debug("Deleted a vehicle for a vin");
        return ResponseEntity.ok("Vehicle deleted");
    }
}
