package de.ait.patientappointmentsystem.configurations;

import de.ait.patientappointmentsystem.model.User;
import de.ait.patientappointmentsystem.model.enums.Role;
import de.ait.patientappointmentsystem.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDataInitializer {

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
        if (userRepository.findByUsername("user").isEmpty()){
            User user1 =  new User();
            User user2 =  new User();

            user1.setUsername("user1");
            user1.setPassword(passwordEncoder.encode("user123"));
            user1.getRoles().add(Role.ROLE_USER);

            user2.setUsername("user2");
            user2.setPassword(passwordEncoder.encode("user123"));
            user2.getRoles().add(Role.ROLE_USER);

            userRepository.saveAll(List.of(user1, user2));
        }
    }
}
