package com.example.demo.controller;

import com.example.demo.entities.Appointment;
import com.example.demo.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    private AppointmentService appoinmentService;

    // 1️⃣ Create appointment
    @PostMapping("/{patientId}")
    public ResponseEntity<?> createAppointment(
            @RequestParam String cause,
            @PathVariable Integer patientId
    ) {
        try {
            Appointment appointment =
                    appoinmentService.createAppointment(cause, patientId);
            return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    // 2️⃣ Add conclusion
    @PutMapping("/conclusion/{patientId}/{appointmentId}")
    public ResponseEntity<?> addConclusion(
            @PathVariable Integer appointmentId,
            @PathVariable Integer patientId,
            @RequestParam String conclusion
    ) {
        try {
            Appointment appointment =
                    appoinmentService.addAppointmentConclusion(
                            conclusion,
                            patientId,
                            appointmentId
                    );
            return ResponseEntity.ok(appointment);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    // 3️⃣ Add medical treatment
    @PutMapping("/medical-treatment/{patientId}/{appointmentId}")
    public ResponseEntity<?> addMedicalTreatment(
            @PathVariable Integer appointmentId,
            @PathVariable Integer patientId,
            @RequestParam String medicalTreatment
    ) {
        try {
            Appointment appointment =
                    appoinmentService.addAppointmentMedicalTreatment(
                            medicalTreatment,
                            patientId,
                            appointmentId
                    );
            return ResponseEntity.ok(appointment);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    // 4️⃣ Get appointments by patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getAppointmentsByPatient(
            @PathVariable Integer patientId
    ) {
        try {
            List<Appointment> appointments =
                    appoinmentService.getAppointmentsByPatientId(patientId);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    // Delete appointment
    @DeleteMapping("/{patientId}/{appointmentId}")
    public ResponseEntity<?> deleteAppointment(
            @PathVariable Integer appointmentId,
            @PathVariable Integer patientId
    ) {
        try {
            appoinmentService.supprimerAppointment(patientId, appointmentId);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }
}
