package com.ev.management.demo.mapper;

import com.ev.management.demo.dto.VehicleDTO;
import com.ev.management.demo.entity.Location;
import com.ev.management.demo.entity.Vehicle;

public class VehicleMapper {
    public static VehicleDTO toDto(Vehicle vehicle) {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setVin(vehicle.getVin());

        Location location = vehicle.getLocation();
        vehicleDTO.setState(location.getStateCode());
        vehicleDTO.setCounty(location.getCounty());
        vehicleDTO.setCity(location.getCounty());
        vehicleDTO.setPostalCode(location.getPostalCode());
        vehicleDTO.setCensusTract(location.getCensusTract());
        vehicleDTO.setVehicleLocation(location.getCoordinates());

        return vehicleDTO;
    }
}
