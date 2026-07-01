package com.societe.portail.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.societe.portail.domain.ApplicationCategory} entity.
 */
@ApiModel(description = "Catégorie permettant de regrouper les applications\ndans le tableau de bord (ex: RH, Finance, Support...)")
public class ApplicationCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String nom;

    private String description;

    private Integer ordreAffichage;

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

    public Integer getOrdreAffichage() {
        return ordreAffichage;
    }

    public void setOrdreAffichage(Integer ordreAffichage) {
        this.ordreAffichage = ordreAffichage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationCategoryDTO)) {
            return false;
        }

        ApplicationCategoryDTO applicationCategoryDTO = (ApplicationCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, applicationCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationCategoryDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", ordreAffichage=" + getOrdreAffichage() +
            "}";
    }
}
