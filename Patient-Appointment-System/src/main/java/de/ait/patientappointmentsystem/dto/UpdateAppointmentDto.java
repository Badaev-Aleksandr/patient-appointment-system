package de.ait.patientappointmentsystem.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateAppointmentDto {
    @NotNull(message = "Appointment ID is required")
    private Long id;

    @NotNull(message = "Appointment date and time is required")
    @FutureOrPresent(message = "Appointment date and time must be in the present or future")
    private LocalDateTime appointmentDateTime;
}
