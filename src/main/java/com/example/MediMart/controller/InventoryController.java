package com.example.MediMart.controller;

import com.example.MediMart.dto.RequestDTO;
import com.example.MediMart.model.Inventory;
import com.example.MediMart.model.Medicine;
import com.example.MediMart.repository.InventoryRepository;
import com.example.MediMart.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/inventory")
@CrossOrigin
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryRepository inventoryRepository;

    // Add a new inventory record
    @PostMapping("/add")
    public ResponseEntity<String> addInventory(@RequestBody Inventory inventory) {
        inventoryService.addInventory(inventory);
        return ResponseEntity.ok("Inventory record added successfully!");
    }

    @PostMapping("/upload/{email}")
    public ResponseEntity<String> addInventoryByExcel(@RequestBody List<RequestDTO> requestDTOS ,@PathVariable String email) {
        for(RequestDTO rdto : requestDTOS){
            Inventory i = new Inventory();
            //convert requestdto to inventory
            i.setEmail(email);
            i.setQuantity(rdto.getQuantity());
            i.setExpDate(rdto.getExpDate());
            i.setManufacturingDate(rdto.getManufacturingDate());
            i.setReorderLevel(rdto.getReorderLevel());
            i.setUnitPrice(rdto.getUnitPrice());
            Medicine m = new Medicine();
            m.setName(rdto.getName());
            m.setDosage(rdto.getDosage());
            m.setForm(rdto.getForm());
            m.setBrandName(rdto.getBrandName());
            i.setMedicine(m);
            inventoryService.addInventory(i);
        }
        return ResponseEntity.ok("Inventory record added successfully!");
    }

    @PostMapping("/getByEmail")
    public List<Map<String, Object>> getInventoryByEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        return inventoryService.getInventoryByEmail(email);
    }

//    // Get all inventory records
//    @GetMapping("/all")
//    public ResponseEntity<List<Inventory>> getAllInventory() {
//        List<Inventory> inventoryList = inventoryService.getAllInventory();
//        return ResponseEntity.ok(inventoryList);
//    }
@PutMapping("/updateQuantity")
public ResponseEntity<String> updateInventoryQuantity(@RequestBody Inventory request) {
    // Find inventory entry by email and medicine_id
    Optional<Inventory> inventoryOptional = inventoryRepository.findByEmailAndMedicineId(
            request.getEmail(), request.getMedicine().getId());

    if (inventoryOptional.isPresent()) {
        Inventory inventory = inventoryOptional.get();
        inventory.setQuantity(request.getQuantity());  // Update quantity
        inventoryRepository.save(inventory);

        // Reset lowStockNotified if quantity is above reorder level
        if (inventory.getQuantity() > inventory.getReorderLevel()) {
            inventory.setLowStockNotified(false);
        }
        return ResponseEntity.ok("Quantity updated successfully.");
    } else {
        return ResponseEntity.status(404).body("Inventory item not found.");
    }
}

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteInventory(@RequestBody Inventory request) {
        // Find inventory entry by email and medicine_id
        Optional<Inventory> inventoryOptional = inventoryRepository.findByEmailAndMedicineId(
                request.getEmail(), request.getMedicine().getId());

        if (inventoryOptional.isPresent()) {
            inventoryRepository.delete(inventoryOptional.get()); // Delete the record
            return ResponseEntity.ok("Inventory record deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Inventory item not found.");
        }
    }
}
