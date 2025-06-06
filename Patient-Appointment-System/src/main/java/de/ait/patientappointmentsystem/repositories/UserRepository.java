package de.ait.patientappointmentsystem.repositories;

import de.ait.patientappointmentsystem.model.Patient;
import de.ait.patientappointmentsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
