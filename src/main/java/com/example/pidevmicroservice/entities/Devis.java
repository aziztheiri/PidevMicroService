package com.example.pidevmicroservice.entities;

import com.example.pidevmicroservice.enums.StatutDevis;
import com.example.pidevmicroservice.enums.TypeAssurance;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Devis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cin;


    private Double montant;

    private Date dateCalcul;

    @Enumerated(EnumType.STRING)
    private TypeAssurance typeAssurance;

    private Long idAssurance;

    @Enumerated(EnumType.STRING)
    private StatutDevis statut;

    private Date dateDebutContrat;
    private Date dateFinContrat;
}