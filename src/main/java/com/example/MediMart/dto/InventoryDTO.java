package com.example.MediMart.dto;

import java.util.Date;

public class InventoryDTO {
    private String email;
    private int quantity;
    private int reorderLevel;
    private Date manufacturingDate;
    private Date expDate;
    private double unitPrice;
    private MedicineDTO medicine;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Date getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(Date manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public MedicineDTO getMedicine() {
        return medicine;
    }

    public void setMedicine(MedicineDTO medicine) {
        this.medicine = medicine;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    // Getters and Setters
}
