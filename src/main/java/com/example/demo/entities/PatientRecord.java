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
public class PatientRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    private String height ;
    private String weight ;
    private String gender ;
    private Integer age ;

    private Boolean smoking ;
    private Boolean Drinking ;
    private Boolean sexuallyActive ;

    @Column(columnDefinition="text[]")
    private List<String> surgeries ;
    @Column(columnDefinition="text[]")
    private List<String> medications ;
    @Column(columnDefinition="text[]")
    private List<String> allergies ;
    @Column(columnDefinition="text[]")
    private List<String> chronicDiseases ;
    @Column(columnDefinition="text[]")
    private List<String> notes ;


    @OneToOne
    private Patient patient ;
    @OneToMany(mappedBy = "patientRecord",cascade = CascadeType.ALL)
    private List<Appointment> appointmentsHistory ;

}
