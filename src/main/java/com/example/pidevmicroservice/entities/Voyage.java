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
public class Voyage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dureeContrat;
    private Date dateDepart;
    private Date dateRetour;
    private String pays;
    private String nationalite;


    private String trancheAge;

    private Date dateDebutContrat;
    private Date dateFinContrat;
}