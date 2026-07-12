package com.cardealership.controller;

import com.cardealership.dto.PurchaseResponse;
import com.cardealership.dto.RestockRequest;
import com.cardealership.dto.VehicleResponse;
import com.cardealership.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/{id}/purchase")
    public ResponseEntity<PurchaseResponse> purchaseVehicle(
            @PathVariable Long id,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        // userDetails contains the currently logged-in user context
        PurchaseResponse response = inventoryService.purchase(id, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/restock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VehicleResponse> restockVehicle(
            @PathVariable Long id,
            @Valid @RequestBody RestockRequest request) {
        VehicleResponse response = inventoryService.restock(id, request.getQuantity());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/purchases/my")
    public ResponseEntity<List<PurchaseResponse>> getMyPurchases(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        List<PurchaseResponse> response = inventoryService.getMyPurchases(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/purchases")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PurchaseResponse>> getAllPurchases() {
        List<PurchaseResponse> response = inventoryService.getAllPurchases();
        return ResponseEntity.ok(response);
    }
}
