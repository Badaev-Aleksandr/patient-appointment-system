package de.ait.patientappointmentsystem.service;

import de.ait.patientappointmentsystem.dto.EmailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendHTMLEmail(EmailDto emailDto) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", emailDto.getName());
        context.setVariable("message", emailDto.getMessage());

        String htmlBody = templateEngine.process("email-template", context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(emailDto.getTo());
        mimeMessageHelper.setSubject(emailDto.getSubject());
        mimeMessageHelper.setText(htmlBody, true);

        javaMailSender.send(mimeMessage);
    }
}
