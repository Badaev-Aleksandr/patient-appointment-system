package de.ait.patientappointmentsystem.controller;

import de.ait.patientappointmentsystem.dto.AppointmentDto;
import de.ait.patientappointmentsystem.dto.CreateAppointmentDto;
import de.ait.patientappointmentsystem.dto.DeleteAppointmentDto;
import de.ait.patientappointmentsystem.dto.UpdateAppointmentDto;
import de.ait.patientappointmentsystem.model.Appointment;
import de.ait.patientappointmentsystem.service.AppointmentService;
import de.ait.patientappointmentsystem.service.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@Slf4j
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Appointment> getAppointment(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(appointmentService.getAppointment(id));
        } catch (EntityNotFoundException exception) {
            log.error(exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AppointmentDto>> getAppointmentByMe(Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long id = userDetails.getPatientId();
            return ResponseEntity.ok(appointmentService.getAppointmentsByPatientId(id));
        } catch (EntityNotFoundException exception) {
            log.error(exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or #dto.patientId == authentication.principal.patientId")
    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody @Valid CreateAppointmentDto dto) {
        try {
            return ResponseEntity.ok(appointmentService.createAppointment(dto));
        } catch (EntityNotFoundException exception) {
            log.error(exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or #dto.id == authentication.principal.patientId")
    public ResponseEntity<AppointmentDto> updateAppointment(@RequestBody @Valid UpdateAppointmentDto dto) {
        try {
            return ResponseEntity.ok(appointmentService.updateAppointment(dto));
        } catch (EntityNotFoundException exception) {
            log.error(exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN') or #dto.patientId == authentication.principal.patientId")
    public ResponseEntity<String> deleteAppointment(@RequestBody @Valid DeleteAppointmentDto dto) {
        try {
            appointmentService.deleteAppointment(dto.getAppointmentId());
            return ResponseEntity.ok().body("Appointment deleted successfully");
        } catch (EntityNotFoundException exception) {
            log.error(exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
