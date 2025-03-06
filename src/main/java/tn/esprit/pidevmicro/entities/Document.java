package tn.esprit.pidevmicro.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Date;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le type de document est obligatoire")
    private String type;

    @NotBlank(message = "Le chemin du document est obligatoire")
    private String chemin;

    @NotNull(message = "La date d'ajout est obligatoire")
    @Temporal(TemporalType.DATE)
    private Date dateAjout;

    @NotNull(message = "La taille du document est obligatoire")
    @Min(value = 0, message = "La taille du document ne peut pas être négative")
    private Float taille;

    @NotBlank(message = "Le format du document est obligatoire")
    private String format;

    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    private String description;

    @NotBlank(message = "Le propriétaire du document est obligatoire")
    private String proprietaire;

    @ManyToOne
    @JoinColumn(name = "sinistre_id", nullable = false)
    private Sinistre sinistre;

    // Constructeur par défaut
    public Document() {}

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getChemin() { return chemin; }
    public void setChemin(String chemin) { this.chemin = chemin; }

    public Date getDateAjout() { return dateAjout; }
    public void setDateAjout(Date dateAjout) { this.dateAjout = dateAjout; }

    public Float getTaille() { return taille; }
    public void setTaille(Float taille) { this.taille = taille; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getProprietaire() { return proprietaire; }
    public void setProprietaire(String proprietaire) { this.proprietaire = proprietaire; }

    public Sinistre getSinistre() { return sinistre; }
    public void setSinistre(Sinistre sinistre) { this.sinistre = sinistre; }
}