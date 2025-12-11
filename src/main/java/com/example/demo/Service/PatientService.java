package com.example.demo.Service;

import com.example.demo.Repository.PatientRepository;
import com.example.demo.entities.FutureAppointment;
import com.example.demo.entities.Patient;
import com.example.demo.entities.PatientRecord;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PatientService {


    private PatientRepository patientRepository;

    public Patient createPatient(Patient patient) {
        patientRepository.findByName(patient.getName())
                .ifPresent(existingPatient -> {
                    throw new RuntimeException("Patient with this name already exists");
                });
        return patientRepository.save(patient);
    }


    public List<Patient> findAll() {
       return patientRepository.findAll();
   }

   public Optional<Patient> findById(int id) {
       return patientRepository.findById(id);
   }

   public Optional<Patient> findByName(String name) {return  this.patientRepository.findByName(name);}

   public List<FutureAppointment> getFutureAppointments(int id) {
       return patientRepository.findById(id).map(Patient::getFutureAppointments).orElse(Collections.emptyList());
   }

   public PatientRecord getPatientRecord(int id) {
       return patientRepository.findById(id).get().getPatientRecord();
   }

}
