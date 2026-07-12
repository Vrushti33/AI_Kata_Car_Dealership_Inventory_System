package com.cardealership.service;

import com.cardealership.dto.PurchaseResponse;
import com.cardealership.dto.VehicleResponse;
import com.cardealership.entity.Purchase;
import com.cardealership.entity.User;
import com.cardealership.entity.Vehicle;
import com.cardealership.exception.OutOfStockException;
import com.cardealership.exception.VehicleNotFoundException;
import com.cardealership.repository.PurchaseRepository;
import com.cardealership.repository.UserRepository;
import com.cardealership.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;

    @Autowired
    public InventoryService(VehicleRepository vehicleRepository, UserRepository userRepository, PurchaseRepository purchaseRepository) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @Transactional
    public PurchaseResponse purchase(Long vehicleId, String buyerEmail) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + vehicleId));

        User user = userRepository.findByEmail(buyerEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + buyerEmail));

        if (vehicle.getQuantity() <= 0) {
            throw new OutOfStockException("Vehicle is out of stock: " + vehicle.getMake() + " " + vehicle.getModel());
        }

        // Decrement stock
        vehicle.setQuantity(vehicle.getQuantity() - 1);
        vehicleRepository.save(vehicle);

        // Record transaction
        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setVehicle(vehicle);
        purchase.setQuantity(1);
        purchase.setTotalPrice(vehicle.getPrice());

        Purchase savedPurchase = purchaseRepository.save(purchase);
        return new PurchaseResponse(savedPurchase);
    }

    @Transactional
    public VehicleResponse restock(Long vehicleId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Restock quantity must be positive");
        }

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + vehicleId));

        // Increment stock
        vehicle.setQuantity(vehicle.getQuantity() + quantity);
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return new VehicleResponse(updatedVehicle);
    }

    @Transactional(readOnly = true)
    public List<PurchaseResponse> getMyPurchases(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return purchaseRepository.findByUserId(user.getId()).stream()
                .map(PurchaseResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PurchaseResponse> getAllPurchases() {
        return purchaseRepository.findAll().stream()
                .map(PurchaseResponse::new)
                .collect(Collectors.toList());
    }
}
