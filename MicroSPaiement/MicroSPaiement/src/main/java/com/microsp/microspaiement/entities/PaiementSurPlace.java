package com.microsp.microspaiement.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class PaiementSurPlace extends Paiement {
    private String agence;
    private String date_rdv;
    private String cin;


}
