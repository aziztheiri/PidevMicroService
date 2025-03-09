package com.example.pidevmicroservice.entities;

import com.example.pidevmicroservice.enums.Profession;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Accidents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String profession;

    private int capitalDeces;
    private int capitalIPP;
    private int montantRenteParJourDT;
    private String dureeFranchise;
    private int capitalTraitement;
    private String nom;
    private String prenom;
    private String mail;
    private String telephone;
}