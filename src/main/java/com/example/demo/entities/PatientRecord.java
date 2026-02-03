package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer age = 0;

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
    @JsonIgnore
    @OneToOne
    private Patient patient ;
    @OneToMany(mappedBy = "patientRecord",cascade = CascadeType.ALL)
    private List<Appointment> appointmentsHistory ;





    @Override
    public String toString() {
        return "PatientRecord{" +
                "id=" + id +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", smoking=" + smoking +
                ", Drinking=" + Drinking +
                ", sexuallyActive=" + sexuallyActive +
                ", surgeries=" + (surgeries != null ? String.join(", ", surgeries) : "[]") +
                ", medications=" + (medications != null ? String.join(", ", medications) : "[]") +
                ", allergies=" + (allergies != null ? String.join(", ", allergies) : "[]") +
                ", chronicDiseases=" + (chronicDiseases != null ? String.join(", ", chronicDiseases) : "[]") +
                ", notes=" + (notes != null ? String.join(", ", notes) : "[]") +
                ", appointmentsHistory=" + (appointmentsHistory != null ? appointmentsHistory.stream()
                .map(Appointment::toString)
                .reduce((a, b) -> a + "; " + b).orElse("[]") : "[]") +
                '}';
    }

}
