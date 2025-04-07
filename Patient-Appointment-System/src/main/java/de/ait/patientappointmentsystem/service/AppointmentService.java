package de.ait.patientappointmentsystem.service;

import de.ait.patientappointmentsystem.dto.AppointmentDto;
import de.ait.patientappointmentsystem.dto.CreateAppointmentDto;
import de.ait.patientappointmentsystem.dto.UpdateAppointmentDto;
import de.ait.patientappointmentsystem.model.Appointment;
import de.ait.patientappointmentsystem.model.Patient;
import de.ait.patientappointmentsystem.repositories.AppointmentRepository;
import de.ait.patientappointmentsystem.repositories.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AppointmentService {

    private AppointmentRepository appointmentRepository;
    private PatientRepository patientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    public AppointmentDto createAppointment(CreateAppointmentDto dto) {
        log.info("Adding appointment for patient with ID: {}", dto.getPatientId());

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> {
                    log.error("Patient with id: " + dto.getPatientId() + " was not found");
                    return new EntityNotFoundException("Patient with ID: " + dto.getPatientId() + " was not found");
                });

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(dto.getAppointmentDateTime());
        patient.getAppointments().add(appointment);

        appointmentRepository.save(appointment);
        return toAppointmentDto(appointment);
    }

    public List<AppointmentDto> getAllAppointments() {
        log.info("Getting all appointments");
        return appointmentRepository.findAll().stream()
                .map(appointment -> {
                    AppointmentDto appointmentDto = new AppointmentDto();
                    appointmentDto.setAppointmentDateTime(appointment.getAppointmentDateTime());
                    appointmentDto.setId(appointment.getId());
                    appointmentDto.setPatientId(appointment.getPatient().getId());
                    return appointmentDto;
                })
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getAppointmentsByPatientId(Long patientId) {
        log.info("Getting all appointments by patient with ID: {}", patientId);
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> {
                    log.error("Patient with id: " + patientId + " was not found");
                    return new EntityNotFoundException("Patient with ID: " + patientId + " was not found");
                });
        Set<AppointmentDto> appointmentDtos = patient.getAppointments().stream()
                .map(appointment -> toAppointmentDto(appointment))
                .collect(Collectors.toSet());
        return List.copyOf(appointmentDtos);
    }

    public Appointment getAppointment(Long id) {
        log.info("Getting appointment with id: {}", id);
        return appointmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Appointment with id: {} was not found", id);
                    return new EntityNotFoundException("Appointment with id " + id + " not found");
                });
    }

    public AppointmentDto updateAppointment(UpdateAppointmentDto dto) {
        log.info("Update Appointment by id: {}", dto.getId());
        Appointment appointment = appointmentRepository.findById(dto.getId())
                .orElseThrow(() -> {
                    log.error("Appointment with id: {} was not found", dto.getId());
                    return new EntityNotFoundException("Appointment with id " + dto.getId() + " was not found");
                });
        appointment.setAppointmentDateTime(dto.getAppointmentDateTime());
        appointmentRepository.save(appointment);

        return toAppointmentDto(appointment);
    }

    public void deleteAppointment(Long id) {
        log.info("Deleting appointment with id: {}", id);
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Appointment with id: {} was not found", id);
                    return new EntityNotFoundException("Appointment with id " + id + " was not found");
                });
        appointmentRepository.delete(appointment);
    }

    private AppointmentDto toAppointmentDto(Appointment appointment) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(appointment.getId());
        appointmentDto.setAppointmentDateTime(appointment.getAppointmentDateTime());
        appointmentDto.setPatientId(appointment.getPatient().getId());
        return appointmentDto;
    }
}
