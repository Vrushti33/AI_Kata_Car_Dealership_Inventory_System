package com.cardealership.controller;

import com.cardealership.dto.PurchaseResponse;
import com.cardealership.dto.RestockRequest;
import com.cardealership.dto.VehicleResponse;
import com.cardealership.exception.OutOfStockException;
import com.cardealership.security.CustomUserDetailsService;
import com.cardealership.security.JwtUtil;
import com.cardealership.service.InventoryService;
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
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryController.class)
@org.springframework.context.annotation.Import(com.cardealership.config.SecurityConfig.class)
public class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private PurchaseResponse purchaseResponse;
    private VehicleResponse vehicleResponse;

    @BeforeEach
    public void setUp() {
        purchaseResponse = new PurchaseResponse();
        purchaseResponse.setPurchaseId(10L);
        purchaseResponse.setBuyerEmail("buyer@route66.com");
        purchaseResponse.setBuyerName("Sally Carrera");
        purchaseResponse.setVehicleId(1L);
        purchaseResponse.setVehicleDetails("Lightning McQueen");
        purchaseResponse.setQuantity(1);
        purchaseResponse.setTotalPrice(new BigDecimal("95000.00"));
        purchaseResponse.setPurchasedAt(LocalDateTime.now());

        vehicleResponse = new VehicleResponse();
        vehicleResponse.setId(1L);
        vehicleResponse.setMake("Lightning");
        vehicleResponse.setModel("McQueen");
        vehicleResponse.setQuantity(15);
    }

    @Test
    @WithMockUser(username = "buyer@route66.com", roles = {"USER"})
    public void shouldPurchaseVehicle() throws Exception {
        when(inventoryService.purchase(eq(1L), eq("buyer@route66.com"))).thenReturn(purchaseResponse);

        mockMvc.perform(post("/api/vehicles/1/purchase")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purchaseId").value(10L))
                .andExpect(jsonPath("$.buyerEmail").value("buyer@route66.com"));
    }

    @Test
    @WithMockUser(username = "buyer@route66.com", roles = {"USER"})
    public void shouldReturnConflictWhenOutOfStock() throws Exception {
        when(inventoryService.purchase(eq(1L), eq("buyer@route66.com")))
                .thenThrow(new OutOfStockException("Vehicle is out of stock"));

        mockMvc.perform(post("/api/vehicles/1/purchase")
                .with(csrf()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Vehicle is out of stock"));
    }

    @Test
    @WithMockUser(username = "admin@route66.com", roles = {"ADMIN"})
    public void shouldRestockVehicleAsAdmin() throws Exception {
        RestockRequest request = new RestockRequest();
        request.setQuantity(10);

        when(inventoryService.restock(eq(1L), anyInt())).thenReturn(vehicleResponse);

        mockMvc.perform(post("/api/vehicles/1/restock")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(15));
    }

    @Test
    @WithMockUser(username = "user@route66.com", roles = {"USER"})
    public void shouldReturnForbiddenWhenUserRestocks() throws Exception {
        RestockRequest request = new RestockRequest();
        request.setQuantity(10);

        mockMvc.perform(post("/api/vehicles/1/restock")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "buyer@route66.com", roles = {"USER"})
    public void shouldGetMyPurchasesAsUser() throws Exception {
        when(inventoryService.getMyPurchases(eq("buyer@route66.com"))).thenReturn(java.util.List.of(purchaseResponse));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/vehicles/purchases/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].purchaseId").value(10L))
                .andExpect(jsonPath("$[0].buyerEmail").value("buyer@route66.com"));
    }

    @Test
    @WithMockUser(username = "admin@route66.com", roles = {"ADMIN"})
    public void shouldGetAllPurchasesAsAdmin() throws Exception {
        when(inventoryService.getAllPurchases()).thenReturn(java.util.List.of(purchaseResponse));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/vehicles/purchases"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].purchaseId").value(10L))
                .andExpect(jsonPath("$[0].buyerEmail").value("buyer@route66.com"));
    }

    @Test
    @WithMockUser(username = "user@route66.com", roles = {"USER"})
    public void shouldDenyGetAllPurchasesAsRegularUser() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/vehicles/purchases"))
                .andExpect(status().isForbidden());
    }
}
