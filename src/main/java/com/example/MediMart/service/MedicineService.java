package com.example.MediMart.service;

import com.example.MediMart.model.Medicine;
import com.example.MediMart.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicineService {
    @Autowired
    MedicineRepository medicineRepository;

    public Medicine addMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

}
