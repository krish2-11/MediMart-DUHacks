package com.example.MediMart.controller;
import com.example.MediMart.model.Inventory;
import com.example.MediMart.model.Medicine;
import com.example.MediMart.model.MedicineRequest;
import com.example.MediMart.repository.MedicineRepository;
import com.example.MediMart.repository.UserRepository;
import com.example.MediMart.service.InventoryService;
import com.example.MediMart.service.MedicineService;
import com.example.MediMart.service.UserService;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/medicines")
@CrossOrigin // Adjust for production
public class MedicineController {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InventoryService inventoryService;

    private Medicine findOrCreateMedicine(Medicine medicineRequest) {
        // Check if medicine already exists
        Optional<Medicine> existingMedicine = medicineRepository.findByNameAndDosageAndFormAndBrandName(
                medicineRequest.getName(),
                medicineRequest.getDosage(),
                medicineRequest.getForm(),
                medicineRequest.getBrandName()
        );

        // Return existing medicine or create a new one
        return existingMedicine.orElseGet(() -> medicineRepository.save(medicineRequest));
    }
    @Autowired
    private MedicineService medicineService;

    @PostMapping
    public ResponseEntity<Medicine> addMedicine(@RequestBody Medicine medicine) {
        Medicine savedMedicine = medicineService.addMedicine(medicine);
        return ResponseEntity.ok(savedMedicine);
    }
    private static final String[] EXPECTED_HEADERS = {
            "name", "manufacturingDate", "expDate", "dosage",
            "form", "unitPrice", "brandName", "quantity", "reorderLevel"
    };

    @GetMapping("/template")
    public ResponseEntity<InputStreamResource> downloadTemplate() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Medicines");

        // Create header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < EXPECTED_HEADERS.length; i++) {
            headerRow.createCell(i).setCellValue(EXPECTED_HEADERS[i]);
        }

        // Add data validation for form field
        DataValidationHelper validationHelper = sheet.getDataValidationHelper();
        CellRangeAddressList addressList = new CellRangeAddressList(1, 1000, 4, 4); // Column index 4 (form)
        DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(
                Arrays.stream(Medicine.Form.values()).map(Enum::name).toArray(String[]::new));
        DataValidation validation = validationHelper.createValidation(constraint, addressList);
        validation.setSuppressDropDownArrow(true);
        sheet.addValidationData(validation);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=medicine_template.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));

    }

    @PostMapping("/upload/{email}")
    public ResponseEntity<?> uploadMedicines(@RequestBody List<MedicineRequest> request ,@PathVariable String email) {

        for(MedicineRequest medicineRequest : request){
            Inventory i = new Inventory();
            i.setEmail(email);
            i.setQuantity(medicineRequest.getQuantity());
            i.setReorderLevel(medicineRequest.getReorderLevel());
            i.setUnitPrice(medicineRequest.getUnitPrice());
            i.setExpDate(medicineRequest.getExpDate());
            i.setManufacturingDate(medicineRequest.getManufacturingDate());
            Medicine m = new Medicine();
            m.setName(medicineRequest.getName());
            m.setBrandName(medicineRequest.getBrandName());
            m.setForm(medicineRequest.getForm());
            m.setDosage(medicineRequest.getDosage());
            i.setMedicine(findOrCreateMedicine(m));
            inventoryService.addInventory(i);
        }

        return ResponseEntity.ok("Medicines uploaded successfully!");
    }


}
