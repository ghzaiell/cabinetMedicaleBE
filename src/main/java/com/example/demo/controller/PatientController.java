package com.example.demo.controller;

import com.example.demo.Service.PatientService;
import com.example.demo.entities.Patient;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.ok("Patient is created");
        } catch (Exception e) {
            // Must return the ResponseEntity here
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

}
