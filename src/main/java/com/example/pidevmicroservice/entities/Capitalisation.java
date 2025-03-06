package com.example.pidevmicroservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Capitalisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dateEffet;
    private int duree;
    private int capitalInitial;
    private int versementRegulier;
    private String frequence;
    private String primeVariable;
    private String nom;
    private String prenom;
    private String mail;
    private String telephone;
}