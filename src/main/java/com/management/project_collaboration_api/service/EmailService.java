package com.management.project_collaboration_api.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // Use MimeMessageHelper to simplify setting the fields
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("hello@projectcollaboration.com");
            helper.setTo(to);
            helper.setSubject(subject);

            // The 'true' flag here tells Spring this is HTML content
            helper.setText(htmlBody, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            // Log the error (it's better to use a logger in a real project)
            System.err.println("Failed to send email: " + e.getMessage());
            throw new RuntimeException("Email sending failed");
        }
    }
}