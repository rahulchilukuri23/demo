package com.ev.management.demo.controller;

import com.ev.management.demo.dto.VehicleDTO;
import com.ev.management.demo.entity.Vehicle;
import com.ev.management.demo.service.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vehicle")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/add")
    public ResponseEntity<VehicleDTO> addVehicle(@RequestBody VehicleDTO dto) {
        Vehicle vehicle = vehicleService.addVehicle(dto);
        return ResponseEntity.ok(vehicle.toDto());
    }

    @GetMapping("/all")
    public Page<VehicleDTO> getAllVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<Vehicle> vehiclePage = vehicleService.getAllVehicles(page, size, sortBy, direction);
        return vehiclePage.map(Vehicle::toDto);
    }

    @GetMapping("/{vin}")
    public ResponseEntity<VehicleDTO> getVehicle(@PathVariable String vin) {
        Vehicle vehicle = vehicleService.getVehicleByVin(vin);
        return ResponseEntity.ok(vehicle.toDto());
    }

    @PutMapping("/update/{vin}")
    public ResponseEntity<VehicleDTO> updateVehicle(@RequestBody VehicleDTO dto) {
        Vehicle vehicle = vehicleService.updateVehicle(dto);
        return ResponseEntity.ok(vehicle.toDto());
    }

    @DeleteMapping("/{vin}")
    public ResponseEntity<String> deleteVehicle(@PathVariable String vin) {
        vehicleService.deleteVehicleByVin(vin);
        return ResponseEntity.ok("Vehicle deleted");
    }
}
