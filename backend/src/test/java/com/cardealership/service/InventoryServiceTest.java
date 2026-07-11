package com.cardealership.service;

import com.cardealership.dto.PurchaseResponse;
import com.cardealership.dto.VehicleResponse;
import com.cardealership.entity.Category;
import com.cardealership.entity.Purchase;
import com.cardealership.entity.Role;
import com.cardealership.entity.User;
import com.cardealership.entity.Vehicle;
import com.cardealership.exception.OutOfStockException;
import com.cardealership.exception.VehicleNotFoundException;
import com.cardealership.repository.PurchaseRepository;
import com.cardealership.repository.UserRepository;
import com.cardealership.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class InventoryServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private User buyer;
    private Vehicle mcqueen;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        buyer = new User();
        buyer.setId(1L);
        buyer.setEmail("buyer@route66.com");
        buyer.setName("Sally Carrera");
        buyer.setRole(Role.USER);

        mcqueen = new Vehicle();
        mcqueen.setId(1L);
        mcqueen.setMake("Lightning");
        mcqueen.setModel("McQueen Special");
        mcqueen.setCategory(Category.COUPE);
        mcqueen.setPrice(new BigDecimal("95000.00"));
        mcqueen.setQuantity(5);
        mcqueen.setYear(2024);
        mcqueen.setDescription("I am speed.");
    }

    @Test
    public void shouldPurchaseVehicleDecreaseQuantityAndCreatePurchaseRecord() {
        // Arrange
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(mcqueen));
        when(userRepository.findByEmail("buyer@route66.com")).thenReturn(Optional.of(buyer));
        
        Purchase savedPurchase = new Purchase();
        savedPurchase.setId(10L);
        savedPurchase.setUser(buyer);
        savedPurchase.setVehicle(mcqueen);
        savedPurchase.setQuantity(1);
        savedPurchase.setTotalPrice(mcqueen.getPrice());
        
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(savedPurchase);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(mcqueen);

        // Act
        PurchaseResponse response = inventoryService.purchase(1L, "buyer@route66.com");

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getPurchaseId()).isEqualTo(10L);
        assertThat(response.getBuyerEmail()).isEqualTo("buyer@route66.com");
        assertThat(response.getTotalPrice()).isEqualTo(new BigDecimal("95000.00"));
        
        // Verify quantity was decremented
        assertThat(mcqueen.getQuantity()).isEqualTo(4);
        verify(vehicleRepository).save(mcqueen);
        verify(purchaseRepository).save(any(Purchase.class));
    }

    @Test
    public void shouldThrowWhenPurchasingOutOfStockVehicle() {
        // Arrange
        mcqueen.setQuantity(0); // Out of stock
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(mcqueen));
        when(userRepository.findByEmail("buyer@route66.com")).thenReturn(Optional.of(buyer));

        // Act & Assert
        assertThrows(OutOfStockException.class, () -> {
            inventoryService.purchase(1L, "buyer@route66.com");
        });
        verify(vehicleRepository, never()).save(any(Vehicle.class));
        verify(purchaseRepository, never()).save(any(Purchase.class));
    }

    @Test
    public void shouldThrowWhenPurchasingNonExistentVehicle() {
        // Arrange
        when(vehicleRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(VehicleNotFoundException.class, () -> {
            inventoryService.purchase(99L, "buyer@route66.com");
        });
    }

    @Test
    public void shouldRestockVehicleAndIncreaseQuantity() {
        // Arrange
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(mcqueen));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(mcqueen);

        // Act
        VehicleResponse response = inventoryService.restock(1L, 10);

        // Assert
        assertThat(response).isNotNull();
        assertThat(mcqueen.getQuantity()).isEqualTo(15); // 5 + 10
        verify(vehicleRepository).save(mcqueen);
    }

    @Test
    public void shouldThrowWhenRestockingNonExistentVehicle() {
        // Arrange
        when(vehicleRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(VehicleNotFoundException.class, () -> {
            inventoryService.restock(99L, 5);
        });
    }

    @Test
    public void shouldThrowWhenRestockQuantityIsNegativeOrZero() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.restock(1L, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.restock(1L, -5);
        });
    }
}
