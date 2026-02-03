package com.example.demo.service;

import com.example.demo.repository.DocteurRepository;
import com.example.demo.entities.Docteur;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;
@AllArgsConstructor
@Service
public class DocteurService {
    private DocteurRepository docteurRepository;





    public Docteur createDocteur(Docteur docteur) {
        // Check if a docteur already exists
        if (docteurRepository.count() >= 1) {
            throw new IllegalStateException("Only one docteur can exist");
        }
        return docteurRepository.save(docteur);
    }


    public Docteur findByName(String name) {
        Optional<Docteur> docteurOpt = docteurRepository.findByName(name);
        return docteurOpt.orElse(null);
    }
}
