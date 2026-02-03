package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    private LocalDate appointmentDate;
    private String  appointmentCause ;
    private   String conclusion ;
    private   String medicalTraitement ;


    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL)
    private List<AiSuggesionQuestions> aiSuggesionQuestions ;

    @JsonIgnore
    @ManyToOne
    private PatientRecord patientRecord;

    @OneToOne(cascade = CascadeType.ALL)
    private AiDetector aiDetector;

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentCause='" + appointmentCause + '\'' +

                '}';
    }

}
