package de.ait.patientappointmentsystem.controller;

import de.ait.patientappointmentsystem.dto.EmailDto;
import de.ait.patientappointmentsystem.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@Slf4j
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-html")
    public ResponseEntity<String> sendHTMLEmail(@RequestBody EmailDto emailDto) {
        try {
            emailService.sendHTMLEmail(emailDto);
            log.info("HTML Email sent to: {} successfully", emailDto.getName());
            return ResponseEntity.ok("HTML Email sent to: " + emailDto.getName() + " successfully");
        } catch (MessagingException e) {
            log.error("Error sending email", e);
            return ResponseEntity.status(500).body("Error sending email " + e.getMessage());
        }
    }
}
