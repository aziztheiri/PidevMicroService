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

}
