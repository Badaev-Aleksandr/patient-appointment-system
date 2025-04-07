package de.ait.patientappointmentsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteAppointmentDto {
    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;
}
