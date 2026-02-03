package com.example.demo.service;

import com.example.demo.repository.FutureAppointmentRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.entities.Patient;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.entities.FutureAppointment;

import java.util.ArrayList;
import java.util.Date;

@Service
@AllArgsConstructor
public class FutureAppointmentService {
    private final FutureAppointmentRepository futureAppointmentRepository;
    private final PatientRepository patientRepository;

    public FutureAppointment createFutureAppointment( Date date, Integer patientId) {

        // 1 — Check if patient exists
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        // 2 — Create the appointment
        FutureAppointment appointment = new FutureAppointment();
        appointment.setDate(date);
        appointment.setPatient(patient);
        if (patient.getFutureAppointments() == null) {
            patient.setFutureAppointments(new ArrayList<>());
        }
        patient.getFutureAppointments().add(appointment);


        // 3 — Save it
        return futureAppointmentRepository.save(appointment);
    }


    public FutureAppointment updateFutureAppointment(FutureAppointment futureAppointment , Integer appointmentId) {
        FutureAppointment appointment = futureAppointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("FutureAppointment not found with id: " + appointmentId));

        appointment.setDate(futureAppointment.getDate());

        // 3 — Save the updated appointment
        return futureAppointmentRepository.save(appointment);

    }


    public FutureAppointment deleteFutureAppointment(Integer appointmentId) {
        FutureAppointment appointment = futureAppointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("FutureAppointment not found with id: " + appointmentId));
        futureAppointmentRepository.delete(appointment);

        return appointment;

    }
}
