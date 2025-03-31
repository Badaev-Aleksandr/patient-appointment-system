package de.ait.patientappointmentsystem.repositories;

import de.ait.patientappointmentsystem.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findById(Long id);

    Optional<Patient> findPatientByDateOfBirth(LocalDate dateOfBirth);
}
