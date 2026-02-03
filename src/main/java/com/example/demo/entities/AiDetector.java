package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AiDetector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String warning;
    String AiAnalyse;
    String level ;
    @JsonIgnore
    @OneToOne
    Appointment appointment;
}
