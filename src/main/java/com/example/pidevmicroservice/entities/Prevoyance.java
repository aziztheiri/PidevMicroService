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
public class Prevoyance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dateEffet;
    private Date dateNaissance;
    private int duree;
    private String frequenceReglement;
    private int capitalDeces;
    private Integer decesAccidentel;
    private Integer invaliditePP;
    private boolean incapaciteTemp;
    private String franchise;
    private String nom;
    private String prenom;
    private String mail;
    private String telephone;
}