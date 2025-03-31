package de.ait.patientappointmentsystem.configurations;

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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;


    public UserDataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder,
                               PatientRepository patientRepository, AppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
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
        if (userRepository.findByUsername("user").isEmpty()) {
            User user1 = new User();
            User user2 = new User();
            Patient patient1 = new Patient();
            Patient patient2 = new Patient();
            Appointment appointment1 = new Appointment();
            Appointment appointment2 = new Appointment();

            user1.setUsername("user1");
            user1.setPassword(passwordEncoder.encode("user123"));
            user1.getRoles().add(Role.ROLE_USER);

            appointment1.setPatient(patient1);
            appointment2.setPatient(patient2);
            appointment1.setAppointmentDateTime(LocalDateTime.of(2025, 05, 15, 14, 45));
            appointment2.setAppointmentDateTime(LocalDateTime.of(2025, 06, 22, 10, 45));
            patient1.setUser(user1);
            patient1.setEmail("user1@gmail.com");
            patient1.setFullName("User1");
            patient1.setDateOfBirth(LocalDate.of(2000, 10, 07));
            patient1.setPhoneNumber("1234567890");
            patient1.getAppointments().add(appointment1);

            patient2.setPhoneNumber("+41234567890");
            patient2.setUser(user2);
            patient2.setEmail("user2@gmail.com");
            patient2.setFullName("User2");
            patient2.setDateOfBirth(LocalDate.of(2010, 10, 07));
            patient2.getAppointments().add(appointment2);

            user2.setUsername("user2");
            user2.setPassword(passwordEncoder.encode("user123"));
            user2.getRoles().add(Role.ROLE_USER);

            userRepository.saveAll(List.of(user1, user2));
            appointmentRepository.saveAll(List.of(appointment1, appointment2));
            patientRepository.saveAll(List.of(patient1, patient2));
        }
    }
}
