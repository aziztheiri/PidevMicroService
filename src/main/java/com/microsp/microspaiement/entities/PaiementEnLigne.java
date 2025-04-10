package com.microsp.microspaiement.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PaiementEnLigne extends Paiement {
    private String paymentMethodNonce;

    // Les autres attributs hérités de Paiement (montant, etc.)

    // Constructeurs
    public PaiementEnLigne() {
    }

    public String getPaymentMethodNonce() {
        return paymentMethodNonce;
    }

    public void setPaymentMethodNonce(String paymentMethodNonce) {
        this.paymentMethodNonce = paymentMethodNonce;
    }

    // Vous pouvez supprimer les anciens champs numeroCarte, cvv, expiration
    // ou les commenter si vous pensez en avoir besoin temporairement.
    // private String numeroCarte;
    // private String cvv;
    // private String expiration;

    // public String getNumeroCarte() {
    //     return numeroCarte;
    // }
    //
    // public void setNumeroCarte(String numeroCarte) {
    //     this.numeroCarte = numeroCarte;
    // }
    //
    // public String getCvv() {
    //     return cvv;
    // }
    //
    // public void setCvv(String cvv) {
    //     this.cvv = cvv;
    // }
    //
    // public String getExpiration() {
    //     return expiration;
    // }
    //
    // public void setExpiration(String expiration) {
    //     this.expiration = expiration;
    // }
}