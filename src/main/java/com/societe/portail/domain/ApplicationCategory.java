package com.societe.portail.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Catégorie permettant de regrouper les applications\ndans le tableau de bord (ex: RH, Finance, Support...)
 */
@Entity
@Table(name = "application_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationCategory implements Serializable {

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

    @Column(name = "ordre_affichage")
    private Integer ordreAffichage;

    @OneToMany(mappedBy = "categorie")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "categorie" }, allowSetters = true)
    private Set<Application> applications = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApplicationCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public ApplicationCategory code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return this.nom;
    }

    public ApplicationCategory nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public ApplicationCategory description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrdreAffichage() {
        return this.ordreAffichage;
    }

    public ApplicationCategory ordreAffichage(Integer ordreAffichage) {
        this.setOrdreAffichage(ordreAffichage);
        return this;
    }

    public void setOrdreAffichage(Integer ordreAffichage) {
        this.ordreAffichage = ordreAffichage;
    }

    public Set<Application> getApplications() {
        return this.applications;
    }

    public void setApplications(Set<Application> applications) {
        if (this.applications != null) {
            this.applications.forEach(i -> i.setCategorie(null));
        }
        if (applications != null) {
            applications.forEach(i -> i.setCategorie(this));
        }
        this.applications = applications;
    }

    public ApplicationCategory applications(Set<Application> applications) {
        this.setApplications(applications);
        return this;
    }

    public ApplicationCategory addApplication(Application application) {
        this.applications.add(application);
        application.setCategorie(this);
        return this;
    }

    public ApplicationCategory removeApplication(Application application) {
        this.applications.remove(application);
        application.setCategorie(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationCategory)) {
            return false;
        }
        return id != null && id.equals(((ApplicationCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationCategory{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", ordreAffichage=" + getOrdreAffichage() +
            "}";
    }
}
