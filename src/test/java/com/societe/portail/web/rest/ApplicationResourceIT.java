package com.societe.portail.web.rest;

import static com.societe.portail.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.societe.portail.IntegrationTest;
import com.societe.portail.domain.Application;
import com.societe.portail.domain.ApplicationCategory;
import com.societe.portail.domain.enumeration.EtatApplication;
import com.societe.portail.domain.enumeration.TypeAcces;
import com.societe.portail.repository.ApplicationRepository;
import com.societe.portail.service.dto.ApplicationDTO;
import com.societe.portail.service.mapper.ApplicationMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ApplicationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApplicationResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ICON = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ICON = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ICON_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ICON_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_COULEUR = "AAAAAAAAAA";
    private static final String UPDATED_COULEUR = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDRE_AFFICHAGE = 1;
    private static final Integer UPDATED_ORDRE_AFFICHAGE = 2;

    private static final EtatApplication DEFAULT_ETAT = EtatApplication.ACTIF;
    private static final EtatApplication UPDATED_ETAT = EtatApplication.INACTIF;

    private static final TypeAcces DEFAULT_TYPE_ACCES = TypeAcces.PUBLIC;
    private static final TypeAcces UPDATED_TYPE_ACCES = TypeAcces.RESTREINT;

    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_ACTIVATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_ACTIVATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_DESACTIVATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_DESACTIVATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/applications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationMockMvc;

    private Application application;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Application createEntity(EntityManager em) {
        Application application = new Application()
            .code(DEFAULT_CODE)
            .nom(DEFAULT_NOM)
            .description(DEFAULT_DESCRIPTION)
            .url(DEFAULT_URL)
            .icon(DEFAULT_ICON)
            .iconContentType(DEFAULT_ICON_CONTENT_TYPE)
            .couleur(DEFAULT_COULEUR)
            .ordreAffichage(DEFAULT_ORDRE_AFFICHAGE)
            .etat(DEFAULT_ETAT)
            .typeAcces(DEFAULT_TYPE_ACCES)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateActivation(DEFAULT_DATE_ACTIVATION)
            .dateDesactivation(DEFAULT_DATE_DESACTIVATION);
        // Add required entity
        ApplicationCategory applicationCategory;
        if (TestUtil.findAll(em, ApplicationCategory.class).isEmpty()) {
            applicationCategory = ApplicationCategoryResourceIT.createEntity(em);
            em.persist(applicationCategory);
            em.flush();
        } else {
            applicationCategory = TestUtil.findAll(em, ApplicationCategory.class).get(0);
        }
        application.setCategorie(applicationCategory);
        return application;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Application createUpdatedEntity(EntityManager em) {
        Application application = new Application()
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .icon(UPDATED_ICON)
            .iconContentType(UPDATED_ICON_CONTENT_TYPE)
            .couleur(UPDATED_COULEUR)
            .ordreAffichage(UPDATED_ORDRE_AFFICHAGE)
            .etat(UPDATED_ETAT)
            .typeAcces(UPDATED_TYPE_ACCES)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateActivation(UPDATED_DATE_ACTIVATION)
            .dateDesactivation(UPDATED_DATE_DESACTIVATION);
        // Add required entity
        ApplicationCategory applicationCategory;
        if (TestUtil.findAll(em, ApplicationCategory.class).isEmpty()) {
            applicationCategory = ApplicationCategoryResourceIT.createUpdatedEntity(em);
            em.persist(applicationCategory);
            em.flush();
        } else {
            applicationCategory = TestUtil.findAll(em, ApplicationCategory.class).get(0);
        }
        application.setCategorie(applicationCategory);
        return application;
    }

    @BeforeEach
    public void initTest() {
        application = createEntity(em);
    }

    @Test
    @Transactional
    void createApplication() throws Exception {
        int databaseSizeBeforeCreate = applicationRepository.findAll().size();
        // Create the Application
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);
        restApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeCreate + 1);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testApplication.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testApplication.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testApplication.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testApplication.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testApplication.getIconContentType()).isEqualTo(DEFAULT_ICON_CONTENT_TYPE);
        assertThat(testApplication.getCouleur()).isEqualTo(DEFAULT_COULEUR);
        assertThat(testApplication.getOrdreAffichage()).isEqualTo(DEFAULT_ORDRE_AFFICHAGE);
        assertThat(testApplication.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testApplication.getTypeAcces()).isEqualTo(DEFAULT_TYPE_ACCES);
        assertThat(testApplication.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testApplication.getDateActivation()).isEqualTo(DEFAULT_DATE_ACTIVATION);
        assertThat(testApplication.getDateDesactivation()).isEqualTo(DEFAULT_DATE_DESACTIVATION);
    }

    @Test
    @Transactional
    void createApplicationWithExistingId() throws Exception {
        // Create the Application with an existing ID
        application.setId(1L);
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        int databaseSizeBeforeCreate = applicationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setCode(null);

        // Create the Application, which fails.
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        restApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setNom(null);

        // Create the Application, which fails.
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        restApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEtatIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setEtat(null);

        // Create the Application, which fails.
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        restApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeAccesIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setTypeAcces(null);

        // Create the Application, which fails.
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        restApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApplications() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList
        restApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(application.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].iconContentType").value(hasItem(DEFAULT_ICON_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(Base64Utils.encodeToString(DEFAULT_ICON))))
            .andExpect(jsonPath("$.[*].couleur").value(hasItem(DEFAULT_COULEUR)))
            .andExpect(jsonPath("$.[*].ordreAffichage").value(hasItem(DEFAULT_ORDRE_AFFICHAGE)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].typeAcces").value(hasItem(DEFAULT_TYPE_ACCES.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))))
            .andExpect(jsonPath("$.[*].dateActivation").value(hasItem(sameInstant(DEFAULT_DATE_ACTIVATION))))
            .andExpect(jsonPath("$.[*].dateDesactivation").value(hasItem(sameInstant(DEFAULT_DATE_DESACTIVATION))));
    }

    @Test
    @Transactional
    void getApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get the application
        restApplicationMockMvc
            .perform(get(ENTITY_API_URL_ID, application.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(application.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.iconContentType").value(DEFAULT_ICON_CONTENT_TYPE))
            .andExpect(jsonPath("$.icon").value(Base64Utils.encodeToString(DEFAULT_ICON)))
            .andExpect(jsonPath("$.couleur").value(DEFAULT_COULEUR))
            .andExpect(jsonPath("$.ordreAffichage").value(DEFAULT_ORDRE_AFFICHAGE))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()))
            .andExpect(jsonPath("$.typeAcces").value(DEFAULT_TYPE_ACCES.toString()))
            .andExpect(jsonPath("$.dateCreation").value(sameInstant(DEFAULT_DATE_CREATION)))
            .andExpect(jsonPath("$.dateActivation").value(sameInstant(DEFAULT_DATE_ACTIVATION)))
            .andExpect(jsonPath("$.dateDesactivation").value(sameInstant(DEFAULT_DATE_DESACTIVATION)));
    }

    @Test
    @Transactional
    void getNonExistingApplication() throws Exception {
        // Get the application
        restApplicationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application
        Application updatedApplication = applicationRepository.findById(application.getId()).get();
        // Disconnect from session so that the updates on updatedApplication are not directly saved in db
        em.detach(updatedApplication);
        updatedApplication
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .icon(UPDATED_ICON)
            .iconContentType(UPDATED_ICON_CONTENT_TYPE)
            .couleur(UPDATED_COULEUR)
            .ordreAffichage(UPDATED_ORDRE_AFFICHAGE)
            .etat(UPDATED_ETAT)
            .typeAcces(UPDATED_TYPE_ACCES)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateActivation(UPDATED_DATE_ACTIVATION)
            .dateDesactivation(UPDATED_DATE_DESACTIVATION);
        ApplicationDTO applicationDTO = applicationMapper.toDto(updatedApplication);

        restApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testApplication.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testApplication.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApplication.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testApplication.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testApplication.getIconContentType()).isEqualTo(UPDATED_ICON_CONTENT_TYPE);
        assertThat(testApplication.getCouleur()).isEqualTo(UPDATED_COULEUR);
        assertThat(testApplication.getOrdreAffichage()).isEqualTo(UPDATED_ORDRE_AFFICHAGE);
        assertThat(testApplication.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testApplication.getTypeAcces()).isEqualTo(UPDATED_TYPE_ACCES);
        assertThat(testApplication.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testApplication.getDateActivation()).isEqualTo(UPDATED_DATE_ACTIVATION);
        assertThat(testApplication.getDateDesactivation()).isEqualTo(UPDATED_DATE_DESACTIVATION);
    }

    @Test
    @Transactional
    void putNonExistingApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(count.incrementAndGet());

        // Create the Application
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(count.incrementAndGet());

        // Create the Application
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(count.incrementAndGet());

        // Create the Application
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicationWithPatch() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application using partial update
        Application partialUpdatedApplication = new Application();
        partialUpdatedApplication.setId(application.getId());

        partialUpdatedApplication
            .nom(UPDATED_NOM)
            .ordreAffichage(UPDATED_ORDRE_AFFICHAGE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateDesactivation(UPDATED_DATE_DESACTIVATION);

        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplication.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplication))
            )
            .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testApplication.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testApplication.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testApplication.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testApplication.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testApplication.getIconContentType()).isEqualTo(DEFAULT_ICON_CONTENT_TYPE);
        assertThat(testApplication.getCouleur()).isEqualTo(DEFAULT_COULEUR);
        assertThat(testApplication.getOrdreAffichage()).isEqualTo(UPDATED_ORDRE_AFFICHAGE);
        assertThat(testApplication.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testApplication.getTypeAcces()).isEqualTo(DEFAULT_TYPE_ACCES);
        assertThat(testApplication.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testApplication.getDateActivation()).isEqualTo(DEFAULT_DATE_ACTIVATION);
        assertThat(testApplication.getDateDesactivation()).isEqualTo(UPDATED_DATE_DESACTIVATION);
    }

    @Test
    @Transactional
    void fullUpdateApplicationWithPatch() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application using partial update
        Application partialUpdatedApplication = new Application();
        partialUpdatedApplication.setId(application.getId());

        partialUpdatedApplication
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .icon(UPDATED_ICON)
            .iconContentType(UPDATED_ICON_CONTENT_TYPE)
            .couleur(UPDATED_COULEUR)
            .ordreAffichage(UPDATED_ORDRE_AFFICHAGE)
            .etat(UPDATED_ETAT)
            .typeAcces(UPDATED_TYPE_ACCES)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateActivation(UPDATED_DATE_ACTIVATION)
            .dateDesactivation(UPDATED_DATE_DESACTIVATION);

        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplication.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplication))
            )
            .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testApplication.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testApplication.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApplication.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testApplication.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testApplication.getIconContentType()).isEqualTo(UPDATED_ICON_CONTENT_TYPE);
        assertThat(testApplication.getCouleur()).isEqualTo(UPDATED_COULEUR);
        assertThat(testApplication.getOrdreAffichage()).isEqualTo(UPDATED_ORDRE_AFFICHAGE);
        assertThat(testApplication.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testApplication.getTypeAcces()).isEqualTo(UPDATED_TYPE_ACCES);
        assertThat(testApplication.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testApplication.getDateActivation()).isEqualTo(UPDATED_DATE_ACTIVATION);
        assertThat(testApplication.getDateDesactivation()).isEqualTo(UPDATED_DATE_DESACTIVATION);
    }

    @Test
    @Transactional
    void patchNonExistingApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(count.incrementAndGet());

        // Create the Application
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(count.incrementAndGet());

        // Create the Application
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(count.incrementAndGet());

        // Create the Application
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        int databaseSizeBeforeDelete = applicationRepository.findAll().size();

        // Delete the application
        restApplicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, application.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
