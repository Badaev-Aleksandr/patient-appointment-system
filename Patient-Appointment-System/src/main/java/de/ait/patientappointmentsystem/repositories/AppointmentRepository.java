package de.ait.patientappointmentsystem.repositories;

import de.ait.patientappointmentsystem.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findById(Long id);
}
