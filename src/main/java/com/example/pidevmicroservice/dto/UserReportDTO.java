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
    private String creationDate;
    private Integer age;
    private String gender;
    private Long monthsSinceLastClaim;
    private Double totalClaimAmount;
    private Long monthlyPremiumAuto;
    private Double customerLifetimeValue;
    private Integer vehicleClassLuxuryCar;
    private Integer employmentStatusEmployed;
    private Integer locationCodeSuburban;
}
