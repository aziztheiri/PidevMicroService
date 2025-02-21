package com.example.pidevmicroservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {


    private final JavaMailSender mailSender;
   private String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verify Your Account");
        message.setText("Your OTP is: " + otp + "\n\nPlease enter this code in the verification page to verify your account.");
        mailSender.send(message);
    }
    @Async
    public void sentDesactivationMail(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Account desactivated ! ");
        message.setText( "Alert: Your account was deactivated on %s due to multiple failed login attempts. " + "If this wasn't you, please contact support immediately."+ today);
        mailSender.send(message);
    }
}
