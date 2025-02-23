package com.example.MediMart.repository;

import com.example.MediMart.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    Optional<Medicine> findByNameAndDosageAndFormAndBrandName(String name, String dosage, Medicine.Form form, String brandName);
}
