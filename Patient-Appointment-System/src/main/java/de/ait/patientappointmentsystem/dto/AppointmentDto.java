package de.ait.patientappointmentsystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDto {
    private Long id;
    private LocalDateTime appointmentDateTime;
    private Long patientId;
}
