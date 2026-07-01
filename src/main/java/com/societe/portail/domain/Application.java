package com.societe.portail.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.societe.portail.domain.enumeration.EtatApplication;
import com.societe.portail.domain.enumeration.TypeAcces;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Une application externe de la société, affichée comme bouton/carte\nsur le tableau de bord du portail.
 */
@Entity
@Table(name = "application")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

    @Lob
    @Column(name = "icon")
    private byte[] icon;

    @Column(name = "icon_content_type")
    private String iconContentType;

    @Column(name = "couleur")
    private String couleur;

    @Column(name = "ordre_affichage")
    private Integer ordreAffichage;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "etat", nullable = false)
    private EtatApplication etat;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_acces", nullable = false)
    private TypeAcces typeAcces;

    @Column(name = "date_creation")
    private ZonedDateTime dateCreation;

    @Column(name = "date_activation")
    private ZonedDateTime dateActivation;

    @Column(name = "date_desactivation")
    private ZonedDateTime dateDesactivation;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "applications" }, allowSetters = true)
    private ApplicationCategory categorie;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Application id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Application code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return this.nom;
    }

    public Application nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public Application description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return this.url;
    }

    public Application url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getIcon() {
        return this.icon;
    }

    public Application icon(byte[] icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public String getIconContentType() {
        return this.iconContentType;
    }

    public Application iconContentType(String iconContentType) {
        this.iconContentType = iconContentType;
        return this;
    }

    public void setIconContentType(String iconContentType) {
        this.iconContentType = iconContentType;
    }

    public String getCouleur() {
        return this.couleur;
    }

    public Application couleur(String couleur) {
        this.setCouleur(couleur);
        return this;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public Integer getOrdreAffichage() {
        return this.ordreAffichage;
    }

    public Application ordreAffichage(Integer ordreAffichage) {
        this.setOrdreAffichage(ordreAffichage);
        return this;
    }

    public void setOrdreAffichage(Integer ordreAffichage) {
        this.ordreAffichage = ordreAffichage;
    }

    public EtatApplication getEtat() {
        return this.etat;
    }

    public Application etat(EtatApplication etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(EtatApplication etat) {
        this.etat = etat;
    }

    public TypeAcces getTypeAcces() {
        return this.typeAcces;
    }

    public Application typeAcces(TypeAcces typeAcces) {
        this.setTypeAcces(typeAcces);
        return this;
    }

    public void setTypeAcces(TypeAcces typeAcces) {
        this.typeAcces = typeAcces;
    }

    public ZonedDateTime getDateCreation() {
        return this.dateCreation;
    }

    public Application dateCreation(ZonedDateTime dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ZonedDateTime getDateActivation() {
        return this.dateActivation;
    }

    public Application dateActivation(ZonedDateTime dateActivation) {
        this.setDateActivation(dateActivation);
        return this;
    }

    public void setDateActivation(ZonedDateTime dateActivation) {
        this.dateActivation = dateActivation;
    }

    public ZonedDateTime getDateDesactivation() {
        return this.dateDesactivation;
    }

    public Application dateDesactivation(ZonedDateTime dateDesactivation) {
        this.setDateDesactivation(dateDesactivation);
        return this;
    }

    public void setDateDesactivation(ZonedDateTime dateDesactivation) {
        this.dateDesactivation = dateDesactivation;
    }

    public ApplicationCategory getCategorie() {
        return this.categorie;
    }

    public void setCategorie(ApplicationCategory applicationCategory) {
        this.categorie = applicationCategory;
    }

    public Application categorie(ApplicationCategory applicationCategory) {
        this.setCategorie(applicationCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Application)) {
            return false;
        }
        return id != null && id.equals(((Application) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Application{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", url='" + getUrl() + "'" +
            ", icon='" + getIcon() + "'" +
            ", iconContentType='" + getIconContentType() + "'" +
            ", couleur='" + getCouleur() + "'" +
            ", ordreAffichage=" + getOrdreAffichage() +
            ", etat='" + getEtat() + "'" +
            ", typeAcces='" + getTypeAcces() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateActivation='" + getDateActivation() + "'" +
            ", dateDesactivation='" + getDateDesactivation() + "'" +
            "}";
    }
}
