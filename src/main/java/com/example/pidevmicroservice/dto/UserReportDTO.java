package com.example.pidevmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReportDTO {
    private String cin;
    private String email;
    private String name;
    private String userRole;
    private Boolean isVerified;
    private String creationDate; // On peut formater la date en chaîne
    private Integer age;
    private String gender;
}
