package de.ait.patientappointmentsystem.service;

import de.ait.patientappointmentsystem.dto.AppointmentDto;
import de.ait.patientappointmentsystem.dto.PatientDto;
import de.ait.patientappointmentsystem.dto.UpdatePatientDto;
import de.ait.patientappointmentsystem.model.Patient;
import de.ait.patientappointmentsystem.model.User;
import de.ait.patientappointmentsystem.repositories.PatientRepository;
import de.ait.patientappointmentsystem.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientService(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    public List<PatientDto> getAllPatients() {
        log.info("Getting all patients");
        return patientRepository.findAll().stream()
                .map(patient -> {
                    PatientDto patientDto = new PatientDto();
                    patientDto.setId(patient.getId());
                    patientDto.setEmail(patient.getEmail());
                    patientDto.setFullName(patient.getFullName());
                    patientDto.setDateOfBirth(patient.getDateOfBirth());
                    patientDto.setPhoneNumber(patient.getPhoneNumber());

                    Set<AppointmentDto> appointments = patient.getAppointments().stream()
                            .map(appointment -> {
                                AppointmentDto appointmentDto = new AppointmentDto();
                                appointmentDto.setId(appointment.getId());
                                appointmentDto.setAppointmentDateTime(appointment.getAppointmentDateTime());
                                appointmentDto.setPatientId(patient.getId());
                                return appointmentDto;
                            })
                            .collect(Collectors.toSet());
                    patientDto.setAppointments(appointments);
                    return patientDto;
                }).collect(Collectors.toList());
    }

    public PatientDto getPatientById(Long id) {
        log.info("Getting patient by id: {}", id);
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient with id: " + id + " was not found"));
        return toDto(patient);
    }

    public PatientDto getPatientByUserId(Long userId) {
        log.info("Getting patient by id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with id: {} was not found", userId);
                    return new EntityNotFoundException("Patient with id: " + userId + " was not found");
                });
        return toDto(user.getPatient());
    }

    public PatientDto updatePatient(Long id, UpdatePatientDto dto) {
        log.info("Updating patient with id: {}", id);
        Patient patient = patientRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Patient with id: " + id + " was not found"));

        patient.setFullName(dto.getFullName());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setEmail(dto.getEmail());

        patientRepository.save(patient);
        return toDto(patient);
    }

    private PatientDto toDto(Patient patient) {
        PatientDto patientDto = new PatientDto();
        patientDto.setId(patient.getId());
        patientDto.setFullName(patient.getFullName());
        patientDto.setDateOfBirth(patient.getDateOfBirth());
        patientDto.setPhoneNumber(patient.getPhoneNumber());
        patientDto.setEmail(patient.getEmail());

        Set<AppointmentDto> appointmentDtos = patient.getAppointments().stream()
                .map(appointment -> {
                    AppointmentDto appointmentDto = new AppointmentDto();
                    appointmentDto.setId(appointment.getId());
                    appointmentDto.setAppointmentDateTime(appointment.getAppointmentDateTime());
                    appointmentDto.setPatientId(patient.getId());
                    return appointmentDto;
                })
                .collect(Collectors.toSet());
        patientDto.setAppointments(appointmentDtos);
        return patientDto;
    }
}
