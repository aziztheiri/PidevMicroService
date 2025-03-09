package com.example.pidevmicroservice.entities;

import com.example.pidevmicroservice.enums.Civilité;
import com.example.pidevmicroservice.enums.PackHabitation;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Habitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PackHabitation pack;

    @ElementCollection
    private List<Long> idGarantiesAssocies;

    private String typePersonne; // Personne Physique ou Morale


    private String typePiece;

    private String numeroPiece;
    private String villePiece;
    private Date datePiece;

    @Enumerated(EnumType.STRING)
    private Civilité civilité;

    private String nom;
    private String prenom;
    private Date dateNaissance;
    private String secteurActivite;
    private String profession;

    private String matriculeFiscale;
    private String raisonSociale;
    private String registreCommerce;

    private String adresse;
    private String ville;
    private String codePostale;
    private String numTelephone;
    private String email;

    private Date dateDebutContrat;
    private Date dateFinContrat;

    private String adresseLogement;
    private String villeLogement;
    private String codePostaleLogement;
    private String numTelephoneLogement;
    private String emailLogement;

    private Boolean photovoltaique;
}