package com.cardealership.dto;

import com.cardealership.entity.Purchase;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PurchaseResponse {

    private Long purchaseId;
    private String buyerEmail;
    private String buyerName;
    private Long vehicleId;
    private String vehicleDetails;
    private Integer quantity;
    private BigDecimal totalPrice;
    private LocalDateTime purchasedAt;

    public PurchaseResponse() {
    }

    public PurchaseResponse(Purchase purchase) {
        this.purchaseId = purchase.getId();
        if (purchase.getUser() != null) {
            this.buyerEmail = purchase.getUser().getEmail();
            this.buyerName = purchase.getUser().getName();
        }
        if (purchase.getVehicle() != null) {
            this.vehicleId = purchase.getVehicle().getId();
            this.vehicleDetails = purchase.getVehicle().getMake() + " " + purchase.getVehicle().getModel();
        }
        this.quantity = purchase.getQuantity();
        this.totalPrice = purchase.getTotalPrice();
        this.purchasedAt = purchase.getPurchasedAt();
    }

    // Getters and Setters
    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(String vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(LocalDateTime purchasedAt) {
        this.purchasedAt = purchasedAt;
    }
}
