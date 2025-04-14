package de.ait.patientappointmentsystem.repositories;

import de.ait.patientappointmentsystem.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findById(Long id);
}
