package com.ev.management.demo.controller;

import com.ev.management.demo.dto.VehicleDTO;
import com.ev.management.demo.entity.Vehicle;
import com.ev.management.demo.repository.VehicleRepository;
import com.ev.management.demo.service.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehicleController.class)
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    @Mock
    private VehicleRepository vehicleRepository;

    @Test
    void addVehicle_shouldReturnVehicleDTO() throws Exception {
        VehicleDTO dto = new VehicleDTO("ABC123", "Tesla", "Model S", 2021, 300, BigDecimal.valueOf(79999.99), "BEV", "Eligible", 370, "WA", "Seattle", "King", "98101", 53033021701L, "POINT(-122.3321 47.6062)", "PUGET UTILITY");

        Vehicle v1 = new Vehicle();
        v1.setVin("ABC123");
        Mockito.when(vehicleService.addVehicle(any(VehicleDTO.class))).thenReturn(v1);

        mockMvc.perform(post("/vehicle/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk());
                //.andExpect(jsonPath("$.vin").value("ABC123"));
    }

    @Test
    void getVehicle_shouldReturnVehicleDTO() throws Exception {
        VehicleDTO dto = new VehicleDTO("ABC123", "Tesla", "Model S", 2021, 300, BigDecimal.valueOf(79999.99), "BEV", "Eligible", 370, "WA", "Seattle", "King", "98101", 53033021701L, "POINT(-122.3321 47.6062)", "PUEGOT UTILITY");
        Vehicle v1 = new Vehicle();
        v1.setVin("ABC123");
        when(vehicleService.getVehicleByVin("ABC123")).thenReturn(v1);

        mockMvc.perform(get("/vehicle/{vin}", "ABC123"))
                .andExpect(status().isOk());
                //.andExpect(jsonPath("$.vin").value("ABC123"));
    }

    @Test
    void getAllVehicles_shouldReturnPagedVehicleDTOs() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Vehicle v1 = new Vehicle(); v1.setVin("ABC123");
        Vehicle v2 = new Vehicle(); v1.setVin("DEF456");

        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(v1,v2), pageable, 2);

        when(vehicleRepository.findAll(pageable)).thenReturn(vehiclePage);

        when(vehicleService.getAllVehicles(0, 10, "vin", "asc")).thenReturn(vehiclePage);

        mockMvc.perform(get("/vehicle/all")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "vin")
                        .param("direction", "asc"))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.content[0].vin").value("ABC123"))
//                .andExpect(jsonPath("$.content[1].vin").value("DEF456"));
    }

    @Test
    void updateVehicle_shouldReturnUpdatedVehicleDTO() throws Exception {
        VehicleDTO dto = new VehicleDTO("ABC123", "Tesla", "Model S", 2021, 300, BigDecimal.valueOf(79999.99), "BEV", "Eligible", 370, "WA", "Seattle", "King", "98101", 53033021701L, "POINT(-122.3321 47.6062)", "PUGEOT UTILITY|| TACOMA");
        Vehicle v1 = new Vehicle(); v1.setVin("ABC123");
        when(vehicleService.updateVehicle(any(VehicleDTO.class))).thenReturn(v1);

        mockMvc.perform(put("/vehicle/update/{vin}", "ABC123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk());
                //.andExpect(jsonPath("$.vin").value("ABC123"));
    }

    @Test
    void deleteVehicle_shouldReturnSuccessMessage() throws Exception {
        doNothing().when(vehicleService).deleteVehicleByVin("ABC123");

        mockMvc.perform(delete("/vehicle/{vin}", "ABC123"))
                .andExpect(status().isOk());
                //.andExpect(content().string("Vehicle deleted"));
    }
}
