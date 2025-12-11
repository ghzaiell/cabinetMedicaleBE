package com.example.demo.controller;

import com.example.demo.service.PatientRecordService;

import com.example.demo.entities.Patient;
import com.example.demo.entities.PatientRecord;

import com.example.demo.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/record")
@CrossOrigin(origins ="http://localhost:4200")
public class RecordController {

    private PatientRecordService patientRecordService;
    private PatientService patientService;

    // Create a new patient record
    @PostMapping("/create/{patientId}")
    public ResponseEntity<?> createRecord(@PathVariable Integer patientId, @RequestBody PatientRecord patientRecord) {
        try {
            PatientRecord newRecord = patientRecordService.createPatientRecord(patientId, patientRecord);
            return ResponseEntity.ok(newRecord);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Update an existing patient record
    @PutMapping("/update/{patientId}")
    public ResponseEntity<?> updateRecord(@PathVariable Integer patientId, @RequestBody PatientRecord updatedRecord) {
        try {
            PatientRecord record = patientRecordService.updatePatientRecord(patientId, updatedRecord);
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Delete a patient's record
    @DeleteMapping("/delete/{patientId}")
    public ResponseEntity<?> deleteRecord(@PathVariable Integer patientId) {
        try {
            Patient patient = patientRecordService.deletePatientRecord(patientId);
            return ResponseEntity.ok("Patient record deleted successfully for patient ID: " + patient.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("get/{patientID}")
    public ResponseEntity<?> getRecord(@PathVariable Integer patientID) {
        try {
            PatientRecord record = patientService.getPatientRecord(patientID);
            return ResponseEntity.ok(record);

        }
        catch (Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    }

}
