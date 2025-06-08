package com.ev.management.demo.service;

import com.ev.management.demo.dto.VehicleDTO;
import com.ev.management.demo.entity.*;
import com.ev.management.demo.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private FuelEligibilityRepository fuelEligibilityRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private UtilityRepository utilityRepository;

    @Mock
    private VehicleModelRepository vehicleModelRepository;

    @Mock
    private VehicleTypeRepository vehicleTypeRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void addVehicle_shouldReturnVehicle() {
        VehicleDTO dto = new VehicleDTO("ABC123", "Tesla", "Model S", 2021, 300, BigDecimal.valueOf(79999.99), "BEV", "Eligible", 370, "WA", "Seattle", "King", "98101", 53033021701L, "POINT(-122.3321 47.6062)", "PUGET UTILITY");
        Vehicle vehicle = new Vehicle();
        vehicle.setVin("ABC123");

        Mockito.when(vehicleRepository.findByVin("ABC123")).thenReturn(Optional.empty());
        when(fuelEligibilityRepository.findByDescription("Eligible")).thenReturn(Optional.of(new FuelEligibility()));
        when(locationRepository.findByPostalCodeAndCityAndCountyAndStateCode("98101","Seattle", "King", "WA" )).thenReturn(Optional.of(new Location()));
        when(utilityRepository.findByName("PUGET UTILITY")).thenReturn(Optional.of(new Utility()));
        when(vehicleModelRepository.findByMakeIgnoreCaseAndModelIgnoreCaseAndModelYear("Tesla", "Model S", 2021)).thenReturn(Optional.of(new VehicleModel()));
        when(vehicleTypeRepository.findByType("BEV")).thenReturn(Optional.of(new VehicleType()));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        Vehicle result = vehicleService.addVehicle(dto);

        Assertions.assertEquals("ABC123", result.getVin());
    }

    @Test
    void getVehicleByVin_shouldReturnVehicle() {
        Vehicle v1 = new Vehicle();
        v1.setVin("ABC123");
        when(vehicleRepository.findByVin("ABC123")).thenReturn(Optional.of(v1));

        Vehicle result = vehicleService.getVehicleByVin("ABC123");

        assertEquals("ABC123", result.getVin());
    }

    @Test
    void getAllVehicles_shouldReturnPageOfVehicles() {
        // Create vehicles with different VINs
        Vehicle v1 = new Vehicle(); v1.setVin("ABC123");
        Vehicle v2 = new Vehicle(); v2.setVin("DEF456");

        // Create a pageable with sort vin desc
        Pageable pageable = PageRequest.of(0, 10, Sort.by("vin").ascending());
        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(v1, v2), pageable, 2);

        // Stub findAll with any Pageable - because service creates its own Pageable
        when(vehicleRepository.findAll(any(Pageable.class))).thenReturn(vehiclePage);

        // Call service method
        Page<Vehicle> result = vehicleService.getAllVehicles(0, 10, "vin", "asc");

        // Assertions
        assertEquals(2, result.getContent().size());
        assertEquals("ABC123", result.getContent().get(0).getVin());
        assertEquals("DEF456", result.getContent().get(1).getVin());
    }



    @Test
    void updateVehicle_shouldUpdateAndReturnVehicle() {
        // Arrange
        VehicleDTO dto = new VehicleDTO();
        dto.setVin("ABC123");
        dto.setElectricRange(350);
        dto.setBaseMsrp(BigDecimal.valueOf(69999.99));
        dto.setFuelEligibility("Eligible");
        dto.setPostalCode("98052");
        dto.setUtility("PSE");
        dto.setMake("Tesla");
        dto.setModel("Model S");
        dto.setModelYear(2021);
        dto.setVehicleType("BEV");
        dto.setState("WA");
        dto.setCity("Seattle");
        dto.setCounty("King");
        dto.setPostalCode("98101");

        Vehicle existingVehicle = new Vehicle();
        existingVehicle.setVin("ABC123");

        FuelEligibility fuelEligibility = new FuelEligibility();
        Utility utility = new Utility();
        VehicleModel model = new VehicleModel();
        VehicleType type = new VehicleType();

        Location location = new Location();
        location.setStateCode("WA");
        location.setCity("Seattle");
        location.setCounty("King");
        location.setPostalCode("98101");

        when(vehicleRepository.findByVin("ABC123")).thenReturn(Optional.of(existingVehicle));
        when(fuelEligibilityRepository.findByDescription("Eligible")).thenReturn(Optional.of(fuelEligibility));
        when(locationRepository.findByPostalCodeAndCityAndCountyAndStateCode("98101","Seattle", "King", "WA" )).thenReturn(Optional.of(location));
        when(utilityRepository.findByName("PSE")).thenReturn(Optional.of(utility));
        when(vehicleModelRepository.findByMakeIgnoreCaseAndModelIgnoreCaseAndModelYear("Tesla", "Model S", 2021))
                .thenReturn(Optional.of(model));
        when(vehicleTypeRepository.findByType("BEV")).thenReturn(Optional.of(type));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Vehicle updatedVehicle = vehicleService.updateVehicle(dto);

        // Assert
        assertEquals("ABC123", updatedVehicle.getVin());
        assertEquals(350, updatedVehicle.getElectricRange());
        assertEquals(69999.99, updatedVehicle.getBaseMsrp().doubleValue());

        verify(vehicleRepository).save(existingVehicle);
    }

    @Test
    void updateVehicle_shouldThrowIfVehicleNotFound() {
        // Arrange
        VehicleDTO dto = new VehicleDTO();
        dto.setVin("NOTFOUND");

        when(vehicleRepository.findByVin("NOTFOUND")).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> vehicleService.updateVehicle(dto));
        assertEquals("Vehicle with VIN NOTFOUND not found", ex.getMessage());
    }
}