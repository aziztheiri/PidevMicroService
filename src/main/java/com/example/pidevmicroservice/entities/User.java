package com.example.pidevmicroservice.entities;

import com.example.pidevmicroservice.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)

@Table(name = "Utilisateur")
public class User {
    @Id
    private String cin;
    private String email;
    private String keycloakId;
    private String password;
    private String name;
    private UserRole userRole;
    private boolean isVerified;
    private String image;
    private String location;
    private LocalDateTime creationDate;
    private Integer age ;
    private String gender;
    @Column(name = "qr_code_redeemed")
    private Boolean qrCodeRedeemed;
    private Boolean reduction;
    private Boolean quizpassed;
    private Integer points;
    @Column(name = "months_since_last_claim")
    private Long monthsSinceLastClaim;

    @Column(name = "total_claim_amount")
    private Double totalClaimAmount;

    @Column(name = "monthly_premium_auto")
    private Long monthlyPremiumAuto;

    @Column(name = "customer_lifetime_value")
    private Double customerLifetimeValue;

    @Column(name = "vehicle_class_luxury_car")
    private Integer vehicleClassLuxuryCar;

    @Column(name = "employment_status_employed")
    private Integer employmentStatusEmployed;

    @Column(name = "location_code_suburban")
    private Integer locationCodeSuburban;
    public boolean isQrCodeRedeemed() {
        return qrCodeRedeemed;
    }
}
