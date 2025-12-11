package com.example.demo.Service;

import com.example.demo.Repository.PatientRepository;
import com.example.demo.entities.Patient;
import com.example.demo.entities.PatientRecord;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;

import org.springframework.stereotype.Service;


import java.util.ArrayList;

@AllArgsConstructor
@Service
public class PatientRecordService {
    private PatientRepository patientRepository;

    public PatientRecord createPatientRecord(Integer patientId , PatientRecord patientRecord) throws Exception {
        PatientRecord newPatientRecord = patientRecord ;
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        if (patient.getPatientRecord() != null) {
            throw new Exception("Patient already has a medical record");
        }

        patient.setPatientRecord(newPatientRecord);
        newPatientRecord.setPatient(patient);

        patientRepository.save(patient);


        return newPatientRecord;
    }

    public Patient deletePatientRecord(Integer patientId) throws Exception {
        Patient patient = patientRepository.findById(patientId).
                orElseThrow(() -> new EntityNotFoundException("Patient not found"));
        if (patient.getPatientRecord() != null) {
        patient.setPatientRecord(null);
        patientRepository.save(patient);
        }
        return patient;
    }

    public PatientRecord updatePatientRecord(Integer patientId, PatientRecord updatedRecord) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        PatientRecord existingRecord = patient.getPatientRecord();

        if (existingRecord == null) {
           existingRecord = new PatientRecord();

        }

        // ======== PARTIAL UPDATE OF SIMPLE FIELDS ========

        if (updatedRecord.getHeight() != null)
            existingRecord.setHeight(updatedRecord.getHeight());

        if (updatedRecord.getWeight() != null)
            existingRecord.setWeight(updatedRecord.getWeight());

        if (updatedRecord.getGender() != null)
            existingRecord.setGender(updatedRecord.getGender());

        if (updatedRecord.getAge() != null)
            existingRecord.setAge(updatedRecord.getAge());

        if (updatedRecord.getSmoking() != null)
            existingRecord.setSmoking(updatedRecord.getSmoking());

        if (updatedRecord.getDrinking() != null)
            existingRecord.setDrinking(updatedRecord.getDrinking());

        if (updatedRecord.getSexuallyActive() != null)
            existingRecord.setSexuallyActive(updatedRecord.getSexuallyActive());

        // ======== LIST FIELDS (append behavior) ========

        // Surgeries
        if (updatedRecord.getSurgeries() != null) {
            if (existingRecord.getSurgeries() == null)
                existingRecord.setSurgeries(new ArrayList<>());

            existingRecord.getSurgeries().addAll(updatedRecord.getSurgeries());
        }

        // Medications
        if (updatedRecord.getMedications() != null) {
            if (existingRecord.getMedications() == null)
                existingRecord.setMedications(new ArrayList<>());

            existingRecord.getMedications().addAll(updatedRecord.getMedications());
        }

        // Allergies
        if (updatedRecord.getAllergies() != null) {
            if (existingRecord.getAllergies() == null)
                existingRecord.setAllergies(new ArrayList<>());

            existingRecord.getAllergies().addAll(updatedRecord.getAllergies());
        }

        // Chronic diseases
        if (updatedRecord.getChronicDiseases() != null) {
            if (existingRecord.getChronicDiseases() == null)
                existingRecord.setChronicDiseases(new ArrayList<>());

            existingRecord.getChronicDiseases().addAll(updatedRecord.getChronicDiseases());
        }

        // Notes
        if (updatedRecord.getNotes() != null) {
            if (existingRecord.getNotes() == null)
                existingRecord.setNotes(new ArrayList<>());

            existingRecord.getNotes().addAll(updatedRecord.getNotes());
        }

        // ======== RELATIONSHIP ========
        existingRecord.setPatient(patient);

        // Save through the owning side
        patientRepository.save(patient);

        return existingRecord;
    }


}
