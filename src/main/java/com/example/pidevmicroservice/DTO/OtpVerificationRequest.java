package com.example.pidevmicroservice.DTO;

import lombok.Data;
import lombok.Getter;

@Data
public class OtpVerificationRequest {
    private String email;
    private String otp;
}
