package com.example.demo.controller;

import com.example.demo.entities.AiDetector;
import com.example.demo.entities.AiSuggesionQuestions;
import com.example.demo.entities.Appointment;
import com.example.demo.entities.Patient;
import com.example.demo.service.AiQuestionsService;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/ai")
public class AiController {

    AiQuestionsService aiQuestionsService;
    PatientService patientService;
    AppointmentService appointmentService;

    @PostMapping("/questions/{patientId}/{appointmentId}")
    public List<AiSuggesionQuestions> generateQuestions(
            @PathVariable Integer patientId,
            @PathVariable Integer appointmentId) {

        return aiQuestionsService.generateAiQuestion(patientId, appointmentId);
    }

    @GetMapping("/result")
    public ResponseEntity<AiDetector> getAiResult(
            @RequestParam Integer patientId,
            @RequestParam Integer appointmentId) {

        try {
            Appointment appointment = appointmentService.getAppointmentsById(patientId, appointmentId);
            return new ResponseEntity<>(appointment.getAiDetector(), HttpStatus.OK);
        } catch (RuntimeException e) {
            // Return 404 if patient or appointment not found, or 400 for invalid data
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PostMapping("/analyse/{patientId}/{appointmentId}")
    public AiDetector AiAnalyse(
            @PathVariable Integer patientId,
            @PathVariable Integer appointmentId) {

        return aiQuestionsService.getAiResult(patientId, appointmentId);
    }

    @PutMapping("/response/{patientId}/{appointmentId}/{questionId}/{data}")
    public AiSuggesionQuestions response(
            @PathVariable Integer patientId,
            @PathVariable Integer appointmentId ,
            @PathVariable Integer questionId ,
            @PathVariable String data

    )
    {
         aiQuestionsService.respondToQuestion(patientId,appointmentId,questionId,data);
         return appointmentService.getAppointmentsById(patientId,appointmentId).getAiSuggesionQuestions().stream()
                 .filter(q->q.getId().equals(questionId)).findFirst().get();

    }

}

