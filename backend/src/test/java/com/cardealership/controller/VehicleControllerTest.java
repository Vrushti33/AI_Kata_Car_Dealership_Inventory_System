package com.cardealership.controller;

import com.cardealership.dto.VehicleRequest;
import com.cardealership.dto.VehicleResponse;
import com.cardealership.entity.Category;
import com.cardealership.security.CustomUserDetailsService;
import com.cardealership.security.JwtUtil;
import com.cardealership.service.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehicleController.class)
@org.springframework.context.annotation.Import(com.cardealership.config.SecurityConfig.class)
public class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private VehicleRequest vehicleRequest;
    private VehicleResponse vehicleResponse;

    @BeforeEach
    public void setUp() {
        vehicleRequest = new VehicleRequest();
        vehicleRequest.setMake("Lightning");
        vehicleRequest.setModel("McQueen Special");
        vehicleRequest.setCategory(Category.COUPE);
        vehicleRequest.setPrice(new BigDecimal("95000.00"));
        vehicleRequest.setQuantity(5);
        vehicleRequest.setYear(2024);
        vehicleRequest.setDescription("Speed.");

        vehicleResponse = new VehicleResponse();
        vehicleResponse.setId(1L);
        vehicleResponse.setMake("Lightning");
        vehicleResponse.setModel("McQueen Special");
        vehicleResponse.setCategory(Category.COUPE);
        vehicleResponse.setPrice(new BigDecimal("95000.00"));
        vehicleResponse.setQuantity(5);
        vehicleResponse.setYear(2024);
        vehicleResponse.setDescription("Speed.");
    }

    @Test
    @WithMockUser(username = "user@route66.com", roles = {"USER"})
    public void shouldGetAllVehicles() throws Exception {
        when(vehicleService.getAllVehicles()).thenReturn(Collections.singletonList(vehicleResponse));

        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].make").value("Lightning"));
    }

    @Test
    @WithMockUser(username = "user@route66.com", roles = {"USER"})
    public void shouldSearchVehicles() throws Exception {
        when(vehicleService.searchVehicles("Lightning", null, null, null, null))
                .thenReturn(Collections.singletonList(vehicleResponse));

        mockMvc.perform(get("/api/vehicles/search")
                .param("make", "Lightning"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].make").value("Lightning"));
    }

    @Test
    @WithMockUser(username = "user@route66.com", roles = {"USER"})
    public void shouldCreateVehicle() throws Exception {
        when(vehicleService.createVehicle(any(VehicleRequest.class))).thenReturn(vehicleResponse);

        mockMvc.perform(post("/api/vehicles")
                .with(csrf()) // Require csrf post process mock for Spring Security
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void shouldReturnUnauthorizedWhenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user@route66.com", roles = {"USER"})
    public void shouldUpdateVehicle() throws Exception {
        when(vehicleService.updateVehicle(eq(1L), any(VehicleRequest.class))).thenReturn(vehicleResponse);

        mockMvc.perform(put("/api/vehicles/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(username = "admin@route66.com", roles = {"ADMIN"})
    public void shouldDeleteVehicleAsAdmin() throws Exception {
        doNothing().when(vehicleService).deleteVehicle(1L);

        mockMvc.perform(delete("/api/vehicles/1")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user@route66.com", roles = {"USER"})
    public void shouldReturnForbiddenWhenUserDeletesVehicle() throws Exception {
        mockMvc.perform(delete("/api/vehicles/1")
                .with(csrf()))
                .andExpect(status().isForbidden());
    }
}
