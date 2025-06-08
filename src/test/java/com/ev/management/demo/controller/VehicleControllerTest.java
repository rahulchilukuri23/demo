package com.ev.management.demo.controller;

import com.ev.management.demo.dto.VehicleDTO;
import com.ev.management.demo.entity.*;
import com.ev.management.demo.service.VehicleService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private VehicleController vehicleController;

    private Vehicle createVehicle(String vin) {
        Vehicle v = spy(new Vehicle());
        VehicleDTO dto = new VehicleDTO(vin, "Tesla", "Model S", 2021, 300, BigDecimal.valueOf(79999.99), "BEV",
                "Eligible", 370, "WA", "Seattle", "King", "98101", 53033021701L,
                "POINT(-122.3321 47.6062)", "PUGET UTILITY");
        when(v.getVin()).thenReturn(vin);
        when(v.getLegislativeDistrict()).thenReturn(370);
        when(v.getDolVehicleId()).thenReturn(370L);

        Location location = new Location();
        location.setStateCode("WA");
        location.setCounty("King");
        location.setCity("Seattle");
        location.setPostalCode("98101");
        location.setCensusTract(53033021701L);
        when(v.getLocation()).thenReturn(location);

        VehicleModel model = new VehicleModel();
        model.setModelYear(2021);
        model.setModel("Model S");
        model.setMake("Tesla");
        when(v.getModel()).thenReturn(model);

        VehicleType vehicleType = new VehicleType();
        vehicleType.setType("BEV");
        when(v.getType()).thenReturn(vehicleType);

        FuelEligibility fuelEligibility = new FuelEligibility();
        fuelEligibility.setDescription("Eligible");
        when(v.getFuelEligibility()).thenReturn(fuelEligibility);

        Utility ut = new Utility();
        ut.setName("PUGET UTILITY");
        List<Utility> utilities = List.of(ut);
        when(v.getUtilities()).thenReturn(utilities);

        return v;
    }

    @Test
    void addVehicle_ReturnsVehicleDTO() {
        VehicleDTO inputDto = new VehicleDTO("ABC123", "Tesla", "Model S", 2021, 300, BigDecimal.valueOf(79999.99),
                "BEV", "Eligible", 370, "WA", "Seattle", "King", "98101", 53033021701L,
                "POINT(-122.3321 47.6062)", "PUGET UTILITY");
        Vehicle savedVehicle = createVehicle("ABC123");

        when(vehicleService.addVehicle(inputDto)).thenReturn(savedVehicle);

        var response = vehicleController.addVehicle(inputDto);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("ABC123", response.getBody().getVin());
        verify(vehicleService).addVehicle(inputDto);
    }

    @Test
    void getAllVehicles_ReturnsPagedDTOs() {
        Vehicle v1 = createVehicle("ABC123");
        Vehicle v2 = createVehicle("DEF123");

        List<Vehicle> vehicles = List.of(v1, v2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Vehicle> vehiclePage = new PageImpl<>(vehicles, pageable, vehicles.size());

        when(vehicleService.getAllVehicles(0, 10, "id", "asc")).thenReturn(vehiclePage);

        Page<VehicleDTO> result = vehicleController.getAllVehicles(0, 10, "id", "asc");

        assertEquals(2, result.getTotalElements());
        assertEquals("ABC123", result.getContent().get(0).getVin());
        assertEquals("DEF123", result.getContent().get(1).getVin());
        verify(vehicleService).getAllVehicles(0, 10, "id", "asc");
    }

    @Test
    void getVehicle_ReturnsVehicleDTO() {
        Vehicle vehicle = createVehicle("ABC123");
        when(vehicleService.getVehicleByVin("ABC123")).thenReturn(vehicle);

        var response = vehicleController.getVehicle("ABC123");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ABC123", response.getBody().getVin());
        verify(vehicleService).getVehicleByVin("ABC123");
    }

    @Test
    void updateVehicle_ReturnsUpdatedDTO() {
        VehicleDTO inputDto = new VehicleDTO("ABC123", "Tesla", "Model S", 2021, 300, BigDecimal.valueOf(79999.99),
                "BEV", "Eligible", 370, "WA", "Seattle", "King", "98101", 53033021701L,
                "POINT(-122.3321 47.6062)", "PUGET UTILITY");
        Vehicle updatedVehicle = createVehicle("ABC123");
        when(vehicleService.updateVehicle(inputDto)).thenReturn(updatedVehicle);

        var response = vehicleController.updateVehicle(inputDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ABC123", response.getBody().getVin());
        verify(vehicleService).updateVehicle(inputDto);
    }

    @Test
    void updateVehicleMSRP_Success() {
        String model = "model1";
        VehicleDTO msrpDto = new VehicleDTO();
        msrpDto.setBaseMsrp(new BigDecimal("25000"));

        doNothing().when(vehicleService).updateVehicleMSRPByModelId(model, msrpDto.getBaseMsrp());

        var response = vehicleController.updateVehicleMSRP(model, msrpDto);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Vehicle MSRP updated"));
        verify(vehicleService).updateVehicleMSRPByModelId(model, msrpDto.getBaseMsrp());
    }

    @Test
    void updateVehicleMSRP_EntityNotFound() {
        String model = "nonexistent-model";
        VehicleDTO msrpDto = new VehicleDTO();
        msrpDto.setBaseMsrp(new BigDecimal("30000"));

        doThrow(new EntityNotFoundException("Model not found"))
                .when(vehicleService).updateVehicleMSRPByModelId(model, msrpDto.getBaseMsrp());

        var response = vehicleController.updateVehicleMSRP(model, msrpDto);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Model not found", response.getBody());
        verify(vehicleService).updateVehicleMSRPByModelId(model, msrpDto.getBaseMsrp());
    }

    @Test
    void deleteVehicle_ReturnsOk() {
        String vin = "ABC123";

        doNothing().when(vehicleService).deleteVehicleByVin(anyString());

        var response = vehicleController.deleteVehicle(vin);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Vehicle deleted", response.getBody());
        verify(vehicleService).deleteVehicleByVin(vin);
    }
}
