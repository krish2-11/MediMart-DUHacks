package com.example.MediMart.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    private int quantity;
    private int reorderLevel;

    @Temporal(TemporalType.DATE)
    private LocalDate manufacturingDate;

    @Temporal(TemporalType.DATE)
    private LocalDate expDate;

    private double unitPrice;

    private boolean expiryNotified7Days = false;
    private boolean expiryNotified1Day = false;
    private boolean lowStockNotified = false;
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public LocalDate getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(LocalDate manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public void setExpDate(LocalDate expDate) {
        this.expDate = expDate;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public boolean isExpiryNotified7Days() { return expiryNotified7Days; }
    public void setExpiryNotified7Days(boolean expiryNotified7Days) { this.expiryNotified7Days = expiryNotified7Days; }

    public boolean isExpiryNotified1Day() { return expiryNotified1Day; }
    public void setExpiryNotified1Day(boolean expiryNotified1Day) { this.expiryNotified1Day = expiryNotified1Day; }

    public boolean isLowStockNotified() { return lowStockNotified; }
    public void setLowStockNotified(boolean lowStockNotified) { this.lowStockNotified = lowStockNotified; }

}
