package com.example.demo.entities;


import com.fasterxml.jackson.annotation.JsonTypeId;
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
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String  name;
    @OneToOne(cascade = CascadeType.ALL)
    private PatientRecord patientRecord;

    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL)
    private List<FutureAppointment> futureAppointments ;




}
