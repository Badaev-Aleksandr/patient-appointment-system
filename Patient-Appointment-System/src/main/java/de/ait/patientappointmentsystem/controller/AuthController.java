package de.ait.patientappointmentsystem.controller;

import de.ait.patientappointmentsystem.dto.LoginRequestDto;
import de.ait.patientappointmentsystem.dto.RegistrationUserDto;
import de.ait.patientappointmentsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok("User logged in successfully:  " + loginRequestDto.getUsername());
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegistrationUserDto dto) {
        userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully :  " + dto.getUsername());
    }
}
