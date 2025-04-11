package com.microsp.microspaiement.entities;

public class User {
    private static final Long STATIC_USER_ID = 1L; // ID statique
    private static Double walletBalance = 0.0; // Solde statique du wallet

    public static Long getStaticUserId() {
        return STATIC_USER_ID;
    }

    public static Double getWalletBalance() {
        return walletBalance;
    }

    public static void addFunds(Double amount) {
        walletBalance += amount;
        System.out.println("Ajout de " + amount + " au wallet de l'utilisateur " + STATIC_USER_ID + ". Nouveau solde : " + walletBalance);
    }
}
