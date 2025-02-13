package com.example.pidevmicroservice.entities;

import jakarta.persistence.*;

@Entity
public class Reclamation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sujet;
    private String description;

    @Enumerated(EnumType.STRING)
    private TypeReclamation type;
    private String statut;

    // Constructeurs
    public Reclamation() {}

    public Reclamation(String sujet, String description, TypeReclamation type, String statut) {
        this.sujet = sujet;
        this.description = description;
        this.type = type;
        this.statut = statut;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeReclamation getType() {
        return type;
    }

    public void setType(TypeReclamation type) {
        this.type = type;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
