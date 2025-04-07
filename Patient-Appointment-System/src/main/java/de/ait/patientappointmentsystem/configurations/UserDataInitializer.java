package de.ait.patientappointmentsystem.configurations;

import com.github.javafaker.Faker;
import de.ait.patientappointmentsystem.model.Appointment;
import de.ait.patientappointmentsystem.model.Patient;
import de.ait.patientappointmentsystem.model.User;
import de.ait.patientappointmentsystem.enums.Role;
import de.ait.patientappointmentsystem.repositories.AppointmentRepository;
import de.ait.patientappointmentsystem.repositories.PatientRepository;
import de.ait.patientappointmentsystem.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class UserDataInitializer {

    Faker faker = new Faker();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.getRoles().add(Role.ROLE_ADMIN);
            admin.getRoles().add(Role.ROLE_USER);
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            userRepository.save(admin);
        }

        if (userRepository.findByUsername("user1").isEmpty()) {
            User user = new User();
            Patient patient = new Patient();
            Appointment appointment = new Appointment();

            patient.setEmail(faker.internet().emailAddress());
            patient.setFullName(faker.name().fullName());
            patient.setDateOfBirth(LocalDate.of(2000, 10, 7));
            patient.setPhoneNumber(faker.phoneNumber().phoneNumber());

            user.setUsername("user1");
            user.setPassword(passwordEncoder.encode("user1123"));
            user.getRoles().add(Role.ROLE_USER);
            user.setPatient(patient);

            appointment.setPatient(patient);
            appointment.setAppointmentDateTime(LocalDateTime.of(2025, 5, 15, 14, 45));

            patient.getAppointments().add(appointment);

            userRepository.save(user);
        }

        if (userRepository.findByUsername("user2").isEmpty()) {
            User user = new User();
            Patient patient = new Patient();
            Appointment appointment = new Appointment();

            patient.setEmail(faker.internet().emailAddress());
            patient.setFullName(faker.name().fullName());
            patient.setDateOfBirth(LocalDate.of(2010, 10, 17));
            patient.setPhoneNumber(faker.phoneNumber().phoneNumber());

            user.setUsername("user2");
            user.setPassword(passwordEncoder.encode("user2123"));
            user.getRoles().add(Role.ROLE_USER);
            user.setPatient(patient);

            appointment.setPatient(patient);
            appointment.setAppointmentDateTime(LocalDateTime.of(2025, 8, 15, 14, 45));

            patient.getAppointments().add(appointment);

            userRepository.save(user);
        }
    }
}
