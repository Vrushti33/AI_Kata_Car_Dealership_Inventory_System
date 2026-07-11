package com.cardealership.integration;

import com.cardealership.dto.LoginRequest;
import com.cardealership.dto.RegisterRequest;
import com.cardealership.dto.RestockRequest;
import com.cardealership.dto.VehicleRequest;
import com.cardealership.entity.Category;
import com.cardealership.entity.Role;
import com.cardealership.entity.User;
import com.cardealership.entity.Vehicle;
import com.cardealership.repository.PurchaseRepository;
import com.cardealership.repository.UserRepository;
import com.cardealership.repository.VehicleRepository;
import com.cardealership.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FullIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private User adminUser;
    private String adminToken;
    private Cookie adminCookie;

    @BeforeEach
    public void setUp() {
        purchaseRepository.deleteAll();
        userRepository.deleteAll();
        vehicleRepository.deleteAll();

        // Seed an admin user for admin-only flow verification
        adminUser = new User();
        adminUser.setEmail("admin@cardealership.com");
        adminUser.setPassword(passwordEncoder.encode("Admin@123"));
        adminUser.setName("System Admin");
        adminUser.setRole(Role.ADMIN);
        adminUser = userRepository.save(adminUser);

        adminToken = jwtUtil.generateToken(adminUser.getEmail(), adminUser.getRole().name());
        adminCookie = new Cookie("jwt", adminToken);
    }

    @Test
    public void fullRegistrationAndLoginFlow() throws Exception {
        RegisterRequest registerReq = new RegisterRequest();
        registerReq.setEmail("customer@route66.com");
        registerReq.setPassword("customerPass");
        registerReq.setName("Sally Carrera");

        // 1. Register User
        MvcResult regResult = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isCreated())
                .andExpect(cookie().exists("jwt"))
                .andReturn();

        Cookie authCookie = regResult.getResponse().getCookie("jwt");
        assertThat(authCookie).isNotNull();

        // 2. Fetch current profile (/me) using registration cookie
        mockMvc.perform(get("/api/auth/me")
                .cookie(authCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("customer@route66.com"))
                .andExpect(jsonPath("$.role").value("USER"));

        // 3. Login User
        LoginRequest loginReq = new LoginRequest();
        loginReq.setEmail("customer@route66.com");
        loginReq.setPassword("customerPass");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("jwt"));

        // 4. Logout User
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(cookie().maxAge("jwt", 0));
    }

    @Test
    public void fullVehicleCRUDFlow() throws Exception {
        // 1. Create a Vehicle (As Admin)
        VehicleRequest request = new VehicleRequest();
        request.setMake("Lightning");
        request.setModel("McQueen Special");
        request.setCategory(Category.COUPE);
        request.setPrice(new BigDecimal("95000.00"));
        request.setQuantity(5);
        request.setYear(2024);
        request.setDescription("I am speed.");

        MvcResult createResult = mockMvc.perform(post("/api/vehicles")
                .cookie(adminCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        Long vehicleId = objectMapper.readTree(responseBody).get("id").asLong();
        assertThat(vehicleId).isNotNull();

        // 2. Read Vehicle
        mockMvc.perform(get("/api/vehicles/" + vehicleId)
                .cookie(adminCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("McQueen Special"));

        // 3. Update Vehicle (As Admin)
        request.setModel("McQueen Gold Edition");
        mockMvc.perform(put("/api/vehicles/" + vehicleId)
                .cookie(adminCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("McQueen Gold Edition"));

        // 4. Delete Vehicle (As Admin)
        mockMvc.perform(delete("/api/vehicles/" + vehicleId)
                .cookie(adminCookie))
                .andExpect(status().isNoContent());

        // 5. Read Deleted Vehicle (Should return 404)
        mockMvc.perform(get("/api/vehicles/" + vehicleId)
                .cookie(adminCookie))
                .andExpect(status().isNotFound());
    }

    @Test
    public void fullPurchaseFlow() throws Exception {
        // Create user
        User buyer = new User();
        buyer.setEmail("buyer@route66.com");
        buyer.setPassword(passwordEncoder.encode("buyer123"));
        buyer.setName("Sally Carrera");
        buyer.setRole(Role.USER);
        userRepository.save(buyer);

        String buyerToken = jwtUtil.generateToken(buyer.getEmail(), buyer.getRole().name());
        Cookie buyerCookie = new Cookie("jwt", buyerToken);

        // Create vehicle with stock = 1
        Vehicle car = new Vehicle();
        car.setMake("Hudson");
        car.setModel("Hornet Classic");
        car.setCategory(Category.SEDAN);
        car.setPrice(new BigDecimal("45000.00"));
        car.setQuantity(1);
        car.setYear(1951);
        car.setDescription("Fabulous!");
        car = vehicleRepository.save(car);

        // 1. Purchase vehicle successfully
        mockMvc.perform(post("/api/vehicles/" + car.getId() + "/purchase")
                .cookie(buyerCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purchaseId").exists())
                .andExpect(jsonPath("$.buyerEmail").value("buyer@route66.com"))
                .andExpect(jsonPath("$.totalPrice").value("45000.0"));

        // Verify stock is now 0
        Vehicle dbCar = vehicleRepository.findById(car.getId()).orElseThrow();
        assertThat(dbCar.getQuantity()).isEqualTo(0);

        // 2. Try purchasing again (Should return 409 conflict out of stock)
        mockMvc.perform(post("/api/vehicles/" + car.getId() + "/purchase")
                .cookie(buyerCookie))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Vehicle is out of stock: Hudson Hornet Classic"));
    }

    @Test
    public void adminOnlyOperations() throws Exception {
        // Create normal user
        User regularUser = new User();
        regularUser.setEmail("user@route66.com");
        regularUser.setPassword(passwordEncoder.encode("userPass"));
        regularUser.setName("Tow Mater");
        regularUser.setRole(Role.USER);
        userRepository.save(regularUser);

        String userToken = jwtUtil.generateToken(regularUser.getEmail(), regularUser.getRole().name());
        Cookie userCookie = new Cookie("jwt", userToken);

        // Create vehicle
        Vehicle car = new Vehicle();
        car.setMake("Hudson");
        car.setModel("Hornet Classic");
        car.setCategory(Category.SEDAN);
        car.setPrice(new BigDecimal("45000.00"));
        car.setQuantity(3);
        car.setYear(1951);
        car.setDescription("Fabulous!");
        car = vehicleRepository.save(car);

        // 1. Try to restock vehicle as normal user (Should return 403)
        RestockRequest restockReq = new RestockRequest();
        restockReq.setQuantity(5);
        mockMvc.perform(post("/api/vehicles/" + car.getId() + "/restock")
                .cookie(userCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restockReq)))
                .andExpect(status().isForbidden());

        // 2. Try to delete vehicle as normal user (Should return 403)
        mockMvc.perform(delete("/api/vehicles/" + car.getId())
                .cookie(userCookie))
                .andExpect(status().isForbidden());

        // 3. Perform restock as Admin (Should succeed)
        mockMvc.perform(post("/api/vehicles/" + car.getId() + "/restock")
                .cookie(adminCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restockReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(8)); // 3 + 5
    }

    @Test
    public void searchAndFilterFlow() throws Exception {
        // Seed test vehicles
        Vehicle v1 = new Vehicle();
        v1.setMake("Lightning");
        v1.setModel("McQueen Special");
        v1.setCategory(Category.COUPE);
        v1.setPrice(new BigDecimal("95000.00"));
        v1.setQuantity(5);
        v1.setYear(2024);
        vehicleRepository.save(v1);

        Vehicle v2 = new Vehicle();
        v2.setMake("Hudson");
        v2.setModel("Hornet Classic");
        v2.setCategory(Category.SEDAN);
        v2.setPrice(new BigDecimal("45000.00"));
        v2.setQuantity(3);
        v2.setYear(1951);
        vehicleRepository.save(v2);

        Vehicle v3 = new Vehicle();
        v3.setMake("Mater");
        v3.setModel("Tow Truck Deluxe");
        v3.setCategory(Category.TRUCK);
        v3.setPrice(new BigDecimal("32000.00"));
        v3.setQuantity(7);
        v3.setYear(2006);
        vehicleRepository.save(v3);

        // 1. Search by make
        mockMvc.perform(get("/api/vehicles/search")
                .cookie(adminCookie)
                .param("make", "Light"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].model").value("McQueen Special"));

        // 2. Search by category
        mockMvc.perform(get("/api/vehicles/search")
                .cookie(adminCookie)
                .param("category", "TRUCK"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].make").value("Mater"));

        // 3. Search by price range
        mockMvc.perform(get("/api/vehicles/search")
                .cookie(adminCookie)
                .param("minPrice", "30000.00")
                .param("maxPrice", "50000.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2)); // Hudson and Mater
    }
}
