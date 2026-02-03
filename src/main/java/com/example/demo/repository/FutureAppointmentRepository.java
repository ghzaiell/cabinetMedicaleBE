package com.example.demo.repository;

import com.example.demo.entities.FutureAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FutureAppointmentRepository extends JpaRepository<FutureAppointment, Integer> {
}
