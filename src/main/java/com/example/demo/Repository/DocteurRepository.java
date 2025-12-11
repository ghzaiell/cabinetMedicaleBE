package com.example.demo.Repository;

import java.util.Optional;

import com.example.demo.entities.Docteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface  DocteurRepository extends JpaRepository<Docteur, Integer> {

     Optional<Docteur> findByName(String name);



}
