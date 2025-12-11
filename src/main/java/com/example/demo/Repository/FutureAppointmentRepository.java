package com.example.demo.Repository;

import com.example.demo.entities.FutureAppointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FutureAppointmentRepository extends JpaRepository<FutureAppointment, Integer> {
}
