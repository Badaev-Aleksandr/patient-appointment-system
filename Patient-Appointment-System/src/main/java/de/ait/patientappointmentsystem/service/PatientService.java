package de.ait.patientappointmentsystem.service;

import de.ait.patientappointmentsystem.model.Patient;
import de.ait.patientappointmentsystem.repositories.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient with id: " + id + " was not found"));
    }

    public Patient getPatientByDateOfBirth(LocalDate dateOfBirth) {
        return patientRepository.findPatientByDateOfBirth(dateOfBirth)
                .orElseThrow(() -> new EntityNotFoundException("Patient with id: " + dateOfBirth + " was not found"));
    }

    public Patient updateDatePatient(Patient patient) {
        Patient updatedPatient = new Patient();
        updatedPatient.setUser(patient.getUser());
        updatedPatient.setFullName(patient.getFullName());
        updatedPatient.setDateOfBirth(patient.getDateOfBirth());
        updatedPatient.setPhoneNumber(updatedPatient.getPhoneNumber());
        updatedPatient.setEmail(patient.getEmail());
        updatedPatient.setAppointments(patient.getAppointments());
        return patientRepository.save(updatedPatient);
    }

    public void deletePatient(Patient patient) {
        patientRepository.delete(patient);
    }
}
