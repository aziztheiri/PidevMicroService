package tn.esprit.pidevmicro.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Entity
public class Sinistre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date de déclaration est obligatoire")
    @Temporal(TemporalType.DATE)
    private Date dateDeclaration;

    @NotBlank(message = "La description est obligatoire")
    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    private String description;

    @NotBlank(message = "Le statut est obligatoire")
    private String statut;

    @NotNull(message = "Le montant estimé est obligatoire")
    @Min(value = 0, message = "Le montant estimé ne peut pas être négatif")
    private Float montantEstime;

    @NotBlank(message = "Le lieu est obligatoire")
    private String lieu;

    @NotBlank(message = "Le type de sinistre est obligatoire")
    private String typeSinistre;

    @NotBlank(message = "La responsabilité est obligatoire")
    private String responsabilite;

    @Temporal(TemporalType.DATE)
    private Date dateCloture;

    @OneToMany(mappedBy = "sinistre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents;

    // Constructeur par défaut
    public Sinistre()  {}

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Date getDateDeclaration() { return dateDeclaration; }
    public void setDateDeclaration(Date dateDeclaration) { this.dateDeclaration = dateDeclaration; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Float getMontantEstime() { return montantEstime; }
    public void setMontantEstime(Float montantEstime) { this.montantEstime = montantEstime; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public String getTypeSinistre() { return typeSinistre; }
    public void setTypeSinistre(String typeSinistre) { this.typeSinistre = typeSinistre; }

    public String getResponsabilite() { return responsabilite; }
    public void setResponsabilite(String responsabilite) { this.responsabilite = responsabilite; }

    public Date getDateCloture() { return dateCloture; }
    public void setDateCloture(Date dateCloture) { this.dateCloture = dateCloture; }

    public List<Document> getDocuments() { return documents; }
    public void setDocuments(List<Document> documents) { this.documents = documents; }
}