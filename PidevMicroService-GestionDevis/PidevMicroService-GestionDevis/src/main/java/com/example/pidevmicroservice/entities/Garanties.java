package com.example.pidevmicroservice.entities;

import com.example.pidevmicroservice.enums.TypeAssurance;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Garanties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Enumerated(EnumType.STRING)
    private TypeAssurance typeAssurance;

    private Double limite;
}