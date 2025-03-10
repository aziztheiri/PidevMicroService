package com.microsp.microspaiement.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_p;

    private double montant = 100;  // Montant fixe
    private LocalDateTime date_paiement = LocalDateTime.now();


    public Long getId_p() {
        return id_p;
    }

    public void setId_p(Long id_p) {
        this.id_p = id_p;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public LocalDateTime getDate_paiement() {
        return date_paiement;
    }

    public void setDate_paiement(LocalDateTime date_paiement) {
        this.date_paiement = date_paiement;
    }
}
