package com.microsp.microspaiement.entities;

import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class PaiementSurPlace extends Paiement {
    private String agence;
    private LocalDate date_rdv;

    public String getAgence() {
        return agence;
    }

    public void setAgence(String agence) {
        this.agence = agence;
    }

    public LocalDate getDate_rdv() {
        return date_rdv;
    }

    public void setDate_rdv(LocalDate date_rdv) {
        this.date_rdv = date_rdv;
    }
}
