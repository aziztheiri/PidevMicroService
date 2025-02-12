package com.example.pidevmicroservice.entities;

import com.example.pidevmicroservice.enums.TrancheAge;
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

    private Integer dureeContrat;
    private Date dateDepart;
    private Date dateRetour;
    private String pays;
    private String nationalite;

    @Enumerated(EnumType.STRING)
    private TrancheAge trancheAge;

    private Date dateDebutContrat;
    private Date dateFinContrat;
}