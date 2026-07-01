package com.societe.portail.service.dto;

import com.societe.portail.domain.enumeration.EtatApplication;
import com.societe.portail.domain.enumeration.TypeAcces;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.societe.portail.domain.Application} entity.
 */
@ApiModel(description = "Une application externe de la société, affichée comme bouton/carte\nsur le tableau de bord du portail.")
public class ApplicationDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String nom;

    private String description;

    private String url;

    @Lob
    private byte[] icon;

    private String iconContentType;
    private String couleur;

    private Integer ordreAffichage;

    @NotNull
    private EtatApplication etat;

    @NotNull
    private TypeAcces typeAcces;

    private ZonedDateTime dateCreation;

    private ZonedDateTime dateActivation;

    private ZonedDateTime dateDesactivation;

    private ApplicationCategoryDTO categorie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public String getIconContentType() {
        return iconContentType;
    }

    public void setIconContentType(String iconContentType) {
        this.iconContentType = iconContentType;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public Integer getOrdreAffichage() {
        return ordreAffichage;
    }

    public void setOrdreAffichage(Integer ordreAffichage) {
        this.ordreAffichage = ordreAffichage;
    }

    public EtatApplication getEtat() {
        return etat;
    }

    public void setEtat(EtatApplication etat) {
        this.etat = etat;
    }

    public TypeAcces getTypeAcces() {
        return typeAcces;
    }

    public void setTypeAcces(TypeAcces typeAcces) {
        this.typeAcces = typeAcces;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ZonedDateTime getDateActivation() {
        return dateActivation;
    }

    public void setDateActivation(ZonedDateTime dateActivation) {
        this.dateActivation = dateActivation;
    }

    public ZonedDateTime getDateDesactivation() {
        return dateDesactivation;
    }

    public void setDateDesactivation(ZonedDateTime dateDesactivation) {
        this.dateDesactivation = dateDesactivation;
    }

    public ApplicationCategoryDTO getCategorie() {
        return categorie;
    }

    public void setCategorie(ApplicationCategoryDTO categorie) {
        this.categorie = categorie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationDTO)) {
            return false;
        }

        ApplicationDTO applicationDTO = (ApplicationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, applicationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", url='" + getUrl() + "'" +
            ", icon='" + getIcon() + "'" +
            ", couleur='" + getCouleur() + "'" +
            ", ordreAffichage=" + getOrdreAffichage() +
            ", etat='" + getEtat() + "'" +
            ", typeAcces='" + getTypeAcces() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateActivation='" + getDateActivation() + "'" +
            ", dateDesactivation='" + getDateDesactivation() + "'" +
            ", categorie=" + getCategorie() +
            "}";
    }
}
