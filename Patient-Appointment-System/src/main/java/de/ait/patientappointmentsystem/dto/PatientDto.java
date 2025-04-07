package de.ait.patientappointmentsystem.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class PatientDto {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Set<AppointmentDto> appointments;
}
