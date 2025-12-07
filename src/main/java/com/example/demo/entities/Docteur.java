package com.example.demo.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Docteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer  id;
    
    private String name;
    private String pasword;
    private String email;
    private String Role = "ROLE_ADMIN"; 
    

}
