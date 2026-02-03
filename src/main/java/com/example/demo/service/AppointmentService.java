package com.example.demo.service;

import com.example.demo.entities.AiSuggesionQuestions;
import com.example.demo.entities.Appointment;
import com.example.demo.entities.Patient;
import com.example.demo.entities.PatientRecord;
import com.example.demo.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Transactional
@Service
@AllArgsConstructor
public class AppointmentService {
    private PatientRepository patientRepository;
   //creation
    public Appointment createAppointment(String cause,Integer patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        Appointment newAppointment = new Appointment();
        newAppointment.setAppointmentCause(cause);
        newAppointment.setAppointmentDate(LocalDate.now());
        newAppointment.setAiSuggesionQuestions(new ArrayList<>());
        if (patient.getPatientRecord().getAppointmentsHistory()== null) {
            patient.getPatientRecord().setAppointmentsHistory(new ArrayList<Appointment>());
        }
        newAppointment.setPatientRecord(patient.getPatientRecord());
        patient.getPatientRecord().getAppointmentsHistory().add(newAppointment);
        patientRepository.save(patient);
        return newAppointment;
    }
      // add conclution
    public Appointment addAppointmentConclusion(String conclusion, Integer patientId, Integer appointmentId) {
        // find patient and throw exception if not
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        // find the appointment and update it and throw exception if not
        Appointment updatedAppointment = patient.getPatientRecord().getAppointmentsHistory().stream()
                .filter(appointment -> appointment.getId()
                        .equals(appointmentId))
                        .findFirst().orElseThrow(()-> new RuntimeException("there no Appointment with this id"));

        updatedAppointment.setConclusion(conclusion);
        // save patient
        patientRepository.save(patient);

        return updatedAppointment;

    }

    //add medical treatement
    public Appointment addAppointmentMedicalTreatment(String med, Integer patientId, Integer appointmentId) {
        // find patient and throw exception if not
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        // find the appointment and update it and throw exception if not
        Appointment updatedAppointment = patient.getPatientRecord().getAppointmentsHistory().stream()
                .filter(appointment -> appointment.getId().equals(appointmentId))
                .findFirst().orElseThrow(()-> new RuntimeException("there no Appointment with this id"));
        String medicalTreatment = LocalDate.now().toString() + " : "+ med +" //";
        updatedAppointment.setMedicalTraitement(medicalTreatment);
        if (patient.getPatientRecord().getMedications() == null) {
            patient.getPatientRecord().setMedications(new ArrayList<>());
        }
        patient.getPatientRecord().getMedications().add(medicalTreatment);
        // save patient
        patientRepository.save(patient);

        return updatedAppointment;

    }

   // get appointments by patient id
    public List<Appointment> getAppointmentsByPatientId(Integer patientId) {
        // find patient and throw exception if not
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        List<Appointment> history = patient.getPatientRecord().getAppointmentsHistory();
        if (history == null) {
            throw new RuntimeException("there no Appointments history with this id: " + patientId);
        }
        return history ;
    }
    public Appointment getAppointmentsById(Integer patientId,Integer appointmentId) {
        // find patient and throw exception if not
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        Appointment appointment = patient.getPatientRecord().getAppointmentsHistory().stream()
                .filter(p -> p.getId().equals(appointmentId)).findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "There is no appointment with id: " + appointmentId + " for patient id: " + patientId
                ));

        return appointment ;

    }


     // supprimer appointment
     public void supprimerAppointment(Integer patientId, Integer appointmentId) {

         Patient patient = patientRepository.findById(patientId)
                 .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

         List<Appointment> appointments = patient.getPatientRecord().getAppointmentsHistory();

         if (appointments == null || appointments.isEmpty()) {
             throw new RuntimeException("No appointments history for patient id: " + patientId);
         }

         boolean removed = appointments.removeIf(
                 appointment -> appointment.getId().equals(appointmentId)
         );

         if (!removed) {
             throw new RuntimeException("No appointment found with id: " + appointmentId);
         }

         // save patient after deletion
         patientRepository.save(patient);
     }



}



