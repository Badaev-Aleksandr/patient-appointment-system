package de.ait.patientappointmentsystem.service;

import de.ait.patientappointmentsystem.dto.RegistrationUserDto;
import de.ait.patientappointmentsystem.dto.UpdateUserDto;
import de.ait.patientappointmentsystem.dto.UserDto;
import de.ait.patientappointmentsystem.enums.Role;
import de.ait.patientappointmentsystem.model.Patient;
import de.ait.patientappointmentsystem.model.User;
import de.ait.patientappointmentsystem.repositories.PatientRepository;
import de.ait.patientappointmentsystem.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User registerUser(RegistrationUserDto dto) {
        log.info("Registering user: " + dto.getUsername());
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            log.info("User already exists");
            throw new IllegalArgumentException("Username already exists");
        }

        Patient patient = new Patient();
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setFullName(dto.getFullName());
        patient.setEmail(dto.getEmail());
        log.info("Registered patient: {}", patient.getFullName());

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        user.setRoles(Set.of(Role.ROLE_USER));
        user.setPatient(patient);
        log.info("User registered successfully with username: {}", user.getUsername());

        return userRepository.save(user);
    }

    public List<UserDto> getAllUsers() {
        log.info("Getting all users");

        return userRepository.findAll().stream()
                .map(user -> {
                    UserDto userDto = new UserDto();
                    userDto.setId(user.getId());
                    userDto.setUsername(user.getUsername());
                    userDto.setPatientId(user.getPatient() != null ? user.getPatient().getId() : null);
                    userDto.setRoles(user.getRoles().stream()
                            .map(Enum::name)
                            .collect(Collectors.toSet()));
                    return userDto;
                }).collect(Collectors.toList());
    }


    public UserDto getUserDtoByUsername(String username) throws UsernameNotFoundException {
        log.info("Searching for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User with {} was not found", username);
                    return new UsernameNotFoundException("Username " + username + " was not found");
                });
        return toDto(user);
    }

    public UserDto getUserDtoById(Long id) {
        log.info("Searching for user: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User with id: {} was not found", id);
                    return new UsernameNotFoundException("User " + id + " was not found");
                });
        return toDto(user);
    }

    public UserDto updateUser(Long id, UpdateUserDto dto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User with id: {} was not found", id);
                    return new UsernameNotFoundException("Username " + id + " was not found");
                });

        existingUser.setUsername(dto.getUsername());
        existingUser.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        existingUser.setRoles(dto.getRoles());
        log.info("User was updated: {}", existingUser.getUsername());
        userRepository.save(existingUser);
        return toDto(existingUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User with id: {} was not found", id);
                    return new UsernameNotFoundException("Username " + id + " was not found");
                });
        userRepository.delete(user);
        log.info("Deleted user with id: {}", id);
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPatientId(user.getPatient() != null ? user.getPatient().getId() : null);
        dto.setRoles(user.getRoles().stream().map(Enum::name).collect(Collectors.toSet()));
        return dto;
    }
}
