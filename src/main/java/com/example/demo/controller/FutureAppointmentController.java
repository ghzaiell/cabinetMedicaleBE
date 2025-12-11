package com.example.demo.controller;

import com.example.demo.entities.FutureAppointment;
import com.example.demo.service.FutureAppointmentService;
import com.example.demo.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class FutureAppointmentController {
    private final FutureAppointmentService futureAppointmentService;
    private final PatientService patientService;
    @PostMapping("/{patientId}/create")
    public ResponseEntity<?> create(@PathVariable Integer patientId,
                                    @RequestBody FutureAppointment futureAppointment) {
        System.out.println("Request received for patientId: " + patientId);
        try {
            FutureAppointment createdAppointment = futureAppointmentService.createFutureAppointment(
                    futureAppointment.getDate(),
                    patientId
            );
            return ResponseEntity.ok(createdAppointment); // 200 OK with the created appointment
        } catch (RuntimeException e) {
            // If patient not found or any runtime error occurs
            return ResponseEntity.status(400).body(
                    Map.of("success", false, "message", e.getMessage())
            );
        } catch (Exception e) {
            // Catch any unexpected exception
            return ResponseEntity.status(500).body(
                    Map.of("success", false, "message", "Server error")
            );
        }
    }
    @PutMapping("/{appointmentId}/update")
    public ResponseEntity<?> update(@PathVariable Integer appointmentId,
                                    @RequestBody FutureAppointment futureAppointment) {
       try {
            FutureAppointment updatedAppointment = futureAppointmentService.updateFutureAppointment(futureAppointment,appointmentId);

            return ResponseEntity.ok(updatedAppointment);

        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "Server error"));
        }
    }
    @DeleteMapping("/{appointmentId}/delete")
    public ResponseEntity<?> delete(@PathVariable Integer appointmentId) {
        try {
            futureAppointmentService.deleteFutureAppointment(appointmentId);
            return ResponseEntity.ok(Map.of("success", true, "message", "Appointment deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "Server error"));
        }
    }

}
