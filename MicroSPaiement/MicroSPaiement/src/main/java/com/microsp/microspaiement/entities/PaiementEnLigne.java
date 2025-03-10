package com.microsp.microspaiement.entities;

import com.microsp.microspaiement.entities.Paiement;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PaiementEnLigne extends Paiement {
    private String numeroCarte;
    private String cvv;
    private String expiration;

    private String cin;

}
