package com.demo.stratus.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${aws.ses.sender}")
    private String senderEmail;

    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            log.info("[{}] Sending email to: {} | Subject: {}",
                    LocalDateTime.now(), to, subject);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);
            log.info("[{}] Email sent successfully to: {}",
                    LocalDateTime.now(), to);

        } catch (Exception e) {
            log.error("[{}] Failed to send email to: {} | Error: {}",
                    LocalDateTime.now(), to, e.getMessage());
        }
    }
}