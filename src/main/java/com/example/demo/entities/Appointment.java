package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String  appointmentCause ;
    private   String conclusion ;
    private   String medicalTraitement ;


    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL)
    private List<AiSuggesionQuestions> aiSuggesionQuestions ;

    @ManyToOne
    private PatientRecord patientRecord;
}
