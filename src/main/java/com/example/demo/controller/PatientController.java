package com.example.demo.controller;

import com.example.demo.Service.PatientService;
import com.example.demo.entities.FutureAppointment;
import com.example.demo.entities.Patient;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/patient")
@CrossOrigin(origins ="http://localhost:4200")
public class PatientController {

    private PatientService patientService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody Patient patient) {
        try {
            patientService.createPatient(patient);
            return ResponseEntity.ok("Patient is created" + patient);
        } catch (Exception e) {
            // Must return the ResponseEntity here
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
    // Get all patients
    @GetMapping("/all")
    public ResponseEntity<?> getAllPatients() {
        try {
            List<Patient> patients = patientService.findAll();
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Get patient by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Integer id) {

        try {
            Patient patient = patientService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            return ResponseEntity.ok(patient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Get patient by name
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getPatientByName(@PathVariable String name) {
        try {
            Patient patient = patientService.findByName(name)
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            return ResponseEntity.ok(patient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Get future appointments of a patient
    @GetMapping("/futureAppointments/{id}")
    public ResponseEntity<?> getFutureAppointments(@PathVariable int id) {
        try {
            List<FutureAppointment> appointments = patientService.getFutureAppointments(id);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
