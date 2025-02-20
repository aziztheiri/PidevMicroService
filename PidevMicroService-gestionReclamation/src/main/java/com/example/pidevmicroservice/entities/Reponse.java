package com.example.pidevmicroservice.entities;

import jakarta.persistence.*;

@Entity
public class Reponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String statut; // Exemple : "Traitée", "Rejetée", "En cours"

    @OneToOne
    @JoinColumn(name = "reclamation_id", referencedColumnName = "id", unique = true)
    private Reclamation reclamation;

    // Constructeurs
    public Reponse() {}

    public Reponse(String statut, Reclamation reclamation) {
        this.statut = statut;
        this.reclamation = reclamation;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Reclamation getReclamation() {
        return reclamation;
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
    }
}
