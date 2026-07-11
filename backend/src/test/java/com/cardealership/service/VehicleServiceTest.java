package com.cardealership.service;

import com.cardealership.dto.VehicleRequest;
import com.cardealership.dto.VehicleResponse;
import com.cardealership.entity.Category;
import com.cardealership.entity.Vehicle;
import com.cardealership.exception.VehicleNotFoundException;
import com.cardealership.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    private Vehicle mcqueen;
    private Vehicle hornet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mcqueen = new Vehicle();
        mcqueen.setId(1L);
        mcqueen.setMake("Lightning");
        mcqueen.setModel("McQueen Special");
        mcqueen.setCategory(Category.COUPE);
        mcqueen.setPrice(new BigDecimal("95000.00"));
        mcqueen.setQuantity(5);
        mcqueen.setYear(2024);
        mcqueen.setDescription("Ka-Chow!");

        hornet = new Vehicle();
        hornet.setId(2L);
        hornet.setMake("Hudson");
        hornet.setModel("Hornet Classic");
        hornet.setCategory(Category.SEDAN);
        hornet.setPrice(new BigDecimal("45000.00"));
        hornet.setQuantity(3);
        hornet.setYear(1951);
        hornet.setDescription("Fabulous!");
    }

    @Test
    public void shouldCreateVehicle() {
        // Arrange
        VehicleRequest request = new VehicleRequest();
        request.setMake("Lightning");
        request.setModel("McQueen Special");
        request.setCategory(Category.COUPE);
        request.setPrice(new BigDecimal("95000.00"));
        request.setQuantity(5);
        request.setYear(2024);
        request.setDescription("Ka-Chow!");

        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(mcqueen);

        // Act
        VehicleResponse response = vehicleService.createVehicle(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getModel()).isEqualTo("McQueen Special");
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    public void shouldGetAllVehicles() {
        // Arrange
        when(vehicleRepository.findAll()).thenReturn(Arrays.asList(mcqueen, hornet));

        // Act
        List<VehicleResponse> responses = vehicleService.getAllVehicles();

        // Assert
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getMake()).isEqualTo("Lightning");
        assertThat(responses.get(1).getMake()).isEqualTo("Hudson");
    }

    @Test
    public void shouldGetVehicleById() {
        // Arrange
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(mcqueen));

        // Act
        VehicleResponse response = vehicleService.getVehicleById(1L);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getModel()).isEqualTo("McQueen Special");
    }

    @Test
    public void shouldThrowWhenVehicleNotFound() {
        // Arrange
        when(vehicleRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(VehicleNotFoundException.class, () -> {
            vehicleService.getVehicleById(99L);
        });
    }

    @Test
    public void shouldUpdateVehicle() {
        // Arrange
        VehicleRequest request = new VehicleRequest();
        request.setMake("Lightning");
        request.setModel("McQueen Gold Edition");
        request.setCategory(Category.COUPE);
        request.setPrice(new BigDecimal("120000.00"));
        request.setQuantity(4);
        request.setYear(2024);
        request.setDescription("Gold color!");

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(mcqueen));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(mcqueen);

        // Act
        VehicleResponse response = vehicleService.updateVehicle(1L, request);

        // Assert
        assertThat(response).isNotNull();
        verify(vehicleRepository).save(mcqueen);
        assertThat(mcqueen.getModel()).isEqualTo("McQueen Gold Edition");
        assertThat(mcqueen.getPrice()).isEqualTo(new BigDecimal("120000.00"));
    }

    @Test
    public void shouldThrowWhenUpdatingNonExistentVehicle() {
        // Arrange
        VehicleRequest request = new VehicleRequest();
        when(vehicleRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(VehicleNotFoundException.class, () -> {
            vehicleService.updateVehicle(99L, request);
        });
    }

    @Test
    public void shouldDeleteVehicle() {
        // Arrange
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(mcqueen));
        doNothing().when(vehicleRepository).delete(mcqueen);

        // Act
        vehicleService.deleteVehicle(1L);

        // Assert
        verify(vehicleRepository, times(1)).delete(mcqueen);
    }

    @Test
    public void shouldThrowWhenDeletingNonExistentVehicle() {
        // Arrange
        when(vehicleRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(VehicleNotFoundException.class, () -> {
            vehicleService.deleteVehicle(99L);
        });
    }

    @Test
    public void shouldSearchByMake() {
        // Arrange
        when(vehicleRepository.search("Light", null, null, null, null))
                .thenReturn(Collections.singletonList(mcqueen));

        // Act
        List<VehicleResponse> results = vehicleService.searchVehicles("Light", null, null, null, null);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getMake()).isEqualTo("Lightning");
    }

    @Test
    public void shouldSearchByCategory() {
        // Arrange
        when(vehicleRepository.search(null, null, Category.SEDAN, null, null))
                .thenReturn(Collections.singletonList(hornet));

        // Act
        List<VehicleResponse> results = vehicleService.searchVehicles(null, null, Category.SEDAN, null, null);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getMake()).isEqualTo("Hudson");
    }

    @Test
    public void shouldSearchByPriceRange() {
        // Arrange
        when(vehicleRepository.search(null, null, null, new BigDecimal("30000.00"), new BigDecimal("50000.00")))
                .thenReturn(Collections.singletonList(hornet));

        // Act
        List<VehicleResponse> results = vehicleService.searchVehicles(null, null, null, new BigDecimal("30000.00"), new BigDecimal("50000.00"));

        // Assert
        assertThat(results).hasSize(1);
    }

    @Test
    public void shouldSearchByMultipleCriteria() {
        // Arrange
        when(vehicleRepository.search("Light", "Special", Category.COUPE, new BigDecimal("80000.00"), new BigDecimal("100000.00")))
                .thenReturn(Collections.singletonList(mcqueen));

        // Act
        List<VehicleResponse> results = vehicleService.searchVehicles("Light", "Special", Category.COUPE, new BigDecimal("80000.00"), new BigDecimal("100000.00"));

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getModel()).isEqualTo("McQueen Special");
    }

    @Test
    public void shouldReturnEmptyWhenNoMatchFound() {
        // Arrange
        when(vehicleRepository.search("NonExistent", null, null, null, null))
                .thenReturn(Collections.emptyList());

        // Act
        List<VehicleResponse> results = vehicleService.searchVehicles("NonExistent", null, null, null, null);

        // Assert
        assertThat(results).isEmpty();
    }
}
