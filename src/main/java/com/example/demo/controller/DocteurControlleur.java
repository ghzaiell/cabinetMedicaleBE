package com.example.demo.controller;

import com.example.demo.Repository.DocteurRepository;
import com.example.demo.entities.Docteur;
import com.example.demo.service.DocteurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/doctor")
@CrossOrigin(origins = "http://localhost:4200")
public class DocteurControlleur {

    private final DocteurService docteurService;
    private final DocteurRepository docteurRepository;

    public DocteurControlleur(DocteurService docteurService, DocteurRepository docteurRepository) {
        this.docteurService = docteurService;
        this.docteurRepository = docteurRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createDocteur(@RequestBody Docteur docteur) {
        try {
            Docteur newDocteur = docteurService.createDocteur(docteur);
            return ResponseEntity.ok(newDocteur);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/getDoc/{name}")
    public ResponseEntity<?> findByName(@PathVariable("name") String name) {
        try {
            Optional<Docteur> docteurOpt = docteurRepository.findByName(name);
            if (docteurOpt.isPresent()) {
                Docteur d = docteurOpt.get();
                Map<String, String> result = new HashMap<>();
                result.put("name", d.getName());
                result.put("email", d.getEmail());
                return ResponseEntity.ok(result);
            } else {
                Map<String, String> message = new HashMap<>();
                message.put("message", "Aucun docteur trouvé avec ce nom");
                return ResponseEntity.status(404).body(message);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Une erreur est survenue: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }


}

