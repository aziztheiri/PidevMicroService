package com.example.pidevmicroservice.entities;

import com.example.pidevmicroservice.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "Utilisateur")
public class User {
    @Id
    private String cin;
    private String email;
    private String password;
    private String name;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    private boolean isVerified;
    private String image;

}
