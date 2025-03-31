package de.ait.patientappointmentsystem.service;

import de.ait.patientappointmentsystem.model.Appointment;
import de.ait.patientappointmentsystem.repositories.AppointmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    private AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment addAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointment(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment with id " + id + " not found"));
    }

    public List<Appointment> getAppointmentsByPatientDateOfBirth(LocalDate dateOfBirth) {
        return appointmentRepository.findAllAppointmentsByPatientDateOfBirth(dateOfBirth)
                .orElseThrow(() -> new EntityNotFoundException("Appointment with dateOfBirth " + dateOfBirth + " not found"));
    }

    public Appointment updateAppointment(Appointment appointment) {
        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setId(appointment.getId());
        updatedAppointment.setPatient(appointment.getPatient());
        updatedAppointment.setAppointmentDateTime(appointment.getAppointmentDateTime());
        return appointmentRepository.save(updatedAppointment);
    }

    public void deleteAppointment(Appointment appointment) {
        appointmentRepository.delete(appointment);
    }
}
