package de.ait.patientappointmentsystem.controller;

import de.ait.patientappointmentsystem.dto.PatientDto;
import de.ait.patientappointmentsystem.dto.UpdatePatientDto;
import de.ait.patientappointmentsystem.service.CustomUserDetails;
import de.ait.patientappointmentsystem.service.PatientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patients")
@Slf4j
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/patient/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(patientService.getPatientById(id));
        } catch (EntityNotFoundException exception) {
            log.error(exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PatientDto> getCurrentPatient(Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getUser().getId();
            return ResponseEntity.ok(patientService.getPatientByUserId(userId));
        } catch (EntityNotFoundException exception) {
            log.error(exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.patientId")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable Long id, @RequestBody UpdatePatientDto dto) {
        try {
            return ResponseEntity.ok(patientService.updatePatient(id, dto));
        } catch (EntityNotFoundException exception) {
            log.error(exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
