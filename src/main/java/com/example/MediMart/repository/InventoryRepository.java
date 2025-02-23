package com.example.MediMart.repository;

import com.example.MediMart.model.Inventory;
import com.example.MediMart.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByMedicineAndEmailAndManufacturingDateAndExpDateAndReorderLevelAndUnitPrice(
            Medicine medicine,
            String email,
            LocalDate manufacturingDate,
            LocalDate expDate,
            int reorderLevel,
            double unitPrice
    );

    List<Inventory> findByEmail(String email);
    Optional<Inventory> findByEmailAndMedicineId(String email, Long medicineId);

}
