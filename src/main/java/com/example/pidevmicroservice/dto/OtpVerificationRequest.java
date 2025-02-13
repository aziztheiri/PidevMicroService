package com.example.pidevmicroservice.dto;

import lombok.Data;


@Data
public class OtpVerificationRequest {
    private String email;
    private String otp;
}
