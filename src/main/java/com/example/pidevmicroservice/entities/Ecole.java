package com.example.pidevmicroservice.entities;

import com.example.pidevmicroservice.enums.PackEcole;
import com.example.pidevmicroservice.enums.Role;
import com.example.pidevmicroservice.enums.TypePiece;
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
public class Ecole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private TypePiece typePiece;

    private String numeroPiece;
    private String nom;
    private String prenom;
    private String numTelephone;
    private String email;

    private String matriculeFiscale;
    private String raisonSociale;
    private String secteurActivite;
    private String profession;
    private String activiteEtablissement;
    private String adresseEtablissement;
    private String villeEtablissement;
    private String codePostaleEtablissement;

    @ElementCollection
    private List<String> nomsEnfants;
    @ElementCollection
    private List<String> prenomsEnfants;
    @ElementCollection
    private List<Date> datesNaissanceEnfants;

    private Date dateEffet;

    @Enumerated(EnumType.STRING)
    private PackEcole pack;

    private String adresseParent;
    private String ville;
    private String codePostale;

    private Date dateDebutContrat;
    private Date dateFinContrat;
}