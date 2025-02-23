package com.example.MediMart.service;

import com.example.MediMart.model.Inventory;
import com.example.MediMart.model.Medicine;
import com.example.MediMart.model.User;
import com.example.MediMart.repository.InventoryRepository;
import com.example.MediMart.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    public void addInventory(Inventory inventory) {
        System.out.println(inventory.getUnitPrice());
        Optional<Medicine> existingMedicine = medicineRepository.findByNameAndDosageAndFormAndBrandName(
                inventory.getMedicine().getName(),
                inventory.getMedicine().getDosage(),
                inventory.getMedicine().getForm(),
                inventory.getMedicine().getBrandName()
        );

        Medicine medicine = existingMedicine.orElseGet(() -> medicineRepository.save(inventory.getMedicine()));

        Optional<Inventory> existingInventory = inventoryRepository.findByMedicineAndEmailAndManufacturingDateAndExpDateAndReorderLevelAndUnitPrice(
                medicine,
                inventory.getEmail(),
                inventory.getManufacturingDate(),
                inventory.getExpDate(),
                inventory.getReorderLevel(),
                inventory.getUnitPrice()
        );

        if (existingInventory.isPresent()) {
            Inventory existing = existingInventory.get();
            System.out.println(existing.getUnitPrice());
            existing.setQuantity(existing.getQuantity() + inventory.getQuantity());
            inventoryRepository.save(existing);
        } else {
            System.out.println(inventory.getUnitPrice());
            inventory.setMedicine(medicine);
            inventoryRepository.save(inventory);
        }
    }
    public List<Map<String, Object>> getInventoryByEmail(String email) {
        List<Inventory> inventoryList = inventoryRepository.findByEmail(email);

        // Convert inventory entities to the required JSON format
        List<Map<String, Object>> result = new ArrayList<>();

        for (Inventory inventory : inventoryList) {
            Map<String, Object> inventoryData = new HashMap<>();
            inventoryData.put("id",inventory.getMedicine().getId());
            inventoryData.put("medicine_name", inventory.getMedicine().getName());
            inventoryData.put("dosage", inventory.getMedicine().getDosage());
            inventoryData.put("brand_name", inventory.getMedicine().getBrandName());
            inventoryData.put("form", inventory.getMedicine().getForm().name());
            inventoryData.put("exp_date", inventory.getExpDate().toString());
            inventoryData.put("manufacturing_date", inventory.getManufacturingDate().toString());
            inventoryData.put("quantity", inventory.getQuantity());
            inventoryData.put("reorder_level", inventory.getReorderLevel());
            inventoryData.put("unit_price", inventory.getUnitPrice());

            result.add(inventoryData);
        }

        return result;
    }

}
