package com.microsp.microspaiement.entities;

import com.microsp.microspaiement.entities.Paiement;
import jakarta.persistence.Entity;

@Entity
public class PaiementEnLigne extends Paiement {
    private String numeroCarte;
    private String cvv;
    private String expiration;


    public String getNumeroCarte() {
        return numeroCarte;
    }

    public void setNumeroCarte(String numeroCarte) {
        this.numeroCarte = numeroCarte;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
}
