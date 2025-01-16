package com.example.backend.services;

import com.example.backend.models.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Value("${application.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    public void sendVerificationEmail(User user) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(user.getEmail());
            helper.setSubject("Verify your email address");

            String verificationLink = frontendUrl + "/verify-email?token=" + user.getVerificationToken();
            String htmlContent = String.format("""
                <html>
                    <body>
                        <h2>Welcome to Our App!</h2>
                        <p>Hi %s,</p>
                        <p>Please verify your email address by clicking the link below:</p>
                        <p><a href="%s">Verify Email</a></p>
                        <p>This link will expire in 24 hours.</p>
                        <p>Best regards,<br/>Your App Team</p>
                    </body>
                </html>
                """, user.getFullName(), verificationLink);

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            log.error("Failed to send verification email", e);
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
}