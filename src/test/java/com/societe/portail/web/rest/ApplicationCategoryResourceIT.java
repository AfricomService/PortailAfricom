package com.societe.portail.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.societe.portail.IntegrationTest;
import com.societe.portail.domain.ApplicationCategory;
import com.societe.portail.repository.ApplicationCategoryRepository;
import com.societe.portail.service.dto.ApplicationCategoryDTO;
import com.societe.portail.service.mapper.ApplicationCategoryMapper;
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

/**
 * Integration tests for the {@link ApplicationCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApplicationCategoryResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDRE_AFFICHAGE = 1;
    private static final Integer UPDATED_ORDRE_AFFICHAGE = 2;

    private static final String ENTITY_API_URL = "/api/application-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApplicationCategoryRepository applicationCategoryRepository;

    @Autowired
    private ApplicationCategoryMapper applicationCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationCategoryMockMvc;

    private ApplicationCategory applicationCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationCategory createEntity(EntityManager em) {
        ApplicationCategory applicationCategory = new ApplicationCategory()
            .code(DEFAULT_CODE)
            .nom(DEFAULT_NOM)
            .description(DEFAULT_DESCRIPTION)
            .ordreAffichage(DEFAULT_ORDRE_AFFICHAGE);
        return applicationCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationCategory createUpdatedEntity(EntityManager em) {
        ApplicationCategory applicationCategory = new ApplicationCategory()
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .ordreAffichage(UPDATED_ORDRE_AFFICHAGE);
        return applicationCategory;
    }

    @BeforeEach
    public void initTest() {
        applicationCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createApplicationCategory() throws Exception {
        int databaseSizeBeforeCreate = applicationCategoryRepository.findAll().size();
        // Create the ApplicationCategory
        ApplicationCategoryDTO applicationCategoryDTO = applicationCategoryMapper.toDto(applicationCategory);
        restApplicationCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationCategory testApplicationCategory = applicationCategoryList.get(applicationCategoryList.size() - 1);
        assertThat(testApplicationCategory.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testApplicationCategory.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testApplicationCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testApplicationCategory.getOrdreAffichage()).isEqualTo(DEFAULT_ORDRE_AFFICHAGE);
    }

    @Test
    @Transactional
    void createApplicationCategoryWithExistingId() throws Exception {
        // Create the ApplicationCategory with an existing ID
        applicationCategory.setId(1L);
        ApplicationCategoryDTO applicationCategoryDTO = applicationCategoryMapper.toDto(applicationCategory);

        int databaseSizeBeforeCreate = applicationCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationCategoryRepository.findAll().size();
        // set the field null
        applicationCategory.setCode(null);

        // Create the ApplicationCategory, which fails.
        ApplicationCategoryDTO applicationCategoryDTO = applicationCategoryMapper.toDto(applicationCategory);

        restApplicationCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationCategoryRepository.findAll().size();
        // set the field null
        applicationCategory.setNom(null);

        // Create the ApplicationCategory, which fails.
        ApplicationCategoryDTO applicationCategoryDTO = applicationCategoryMapper.toDto(applicationCategory);

        restApplicationCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApplicationCategories() throws Exception {
        // Initialize the database
        applicationCategoryRepository.saveAndFlush(applicationCategory);

        // Get all the applicationCategoryList
        restApplicationCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].ordreAffichage").value(hasItem(DEFAULT_ORDRE_AFFICHAGE)));
    }

    @Test
    @Transactional
    void getApplicationCategory() throws Exception {
        // Initialize the database
        applicationCategoryRepository.saveAndFlush(applicationCategory);

        // Get the applicationCategory
        restApplicationCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, applicationCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationCategory.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.ordreAffichage").value(DEFAULT_ORDRE_AFFICHAGE));
    }

    @Test
    @Transactional
    void getNonExistingApplicationCategory() throws Exception {
        // Get the applicationCategory
        restApplicationCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApplicationCategory() throws Exception {
        // Initialize the database
        applicationCategoryRepository.saveAndFlush(applicationCategory);

        int databaseSizeBeforeUpdate = applicationCategoryRepository.findAll().size();

        // Update the applicationCategory
        ApplicationCategory updatedApplicationCategory = applicationCategoryRepository.findById(applicationCategory.getId()).get();
        // Disconnect from session so that the updates on updatedApplicationCategory are not directly saved in db
        em.detach(updatedApplicationCategory);
        updatedApplicationCategory
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .ordreAffichage(UPDATED_ORDRE_AFFICHAGE);
        ApplicationCategoryDTO applicationCategoryDTO = applicationCategoryMapper.toDto(updatedApplicationCategory);

        restApplicationCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationCategoryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeUpdate);
        ApplicationCategory testApplicationCategory = applicationCategoryList.get(applicationCategoryList.size() - 1);
        assertThat(testApplicationCategory.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testApplicationCategory.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testApplicationCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApplicationCategory.getOrdreAffichage()).isEqualTo(UPDATED_ORDRE_AFFICHAGE);
    }

    @Test
    @Transactional
    void putNonExistingApplicationCategory() throws Exception {
        int databaseSizeBeforeUpdate = applicationCategoryRepository.findAll().size();
        applicationCategory.setId(count.incrementAndGet());

        // Create the ApplicationCategory
        ApplicationCategoryDTO applicationCategoryDTO = applicationCategoryMapper.toDto(applicationCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationCategoryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicationCategory() throws Exception {
        int databaseSizeBeforeUpdate = applicationCategoryRepository.findAll().size();
        applicationCategory.setId(count.incrementAndGet());

        // Create the ApplicationCategory
        ApplicationCategoryDTO applicationCategoryDTO = applicationCategoryMapper.toDto(applicationCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicationCategory() throws Exception {
        int databaseSizeBeforeUpdate = applicationCategoryRepository.findAll().size();
        applicationCategory.setId(count.incrementAndGet());

        // Create the ApplicationCategory
        ApplicationCategoryDTO applicationCategoryDTO = applicationCategoryMapper.toDto(applicationCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationCategoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicationCategoryWithPatch() throws Exception {
        // Initialize the database
        applicationCategoryRepository.saveAndFlush(applicationCategory);

        int databaseSizeBeforeUpdate = applicationCategoryRepository.findAll().size();

        // Update the applicationCategory using partial update
        ApplicationCategory partialUpdatedApplicationCategory = new ApplicationCategory();
        partialUpdatedApplicationCategory.setId(applicationCategory.getId());

        partialUpdatedApplicationCategory.nom(UPDATED_NOM).description(UPDATED_DESCRIPTION);

        restApplicationCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationCategory))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeUpdate);
        ApplicationCategory testApplicationCategory = applicationCategoryList.get(applicationCategoryList.size() - 1);
        assertThat(testApplicationCategory.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testApplicationCategory.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testApplicationCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApplicationCategory.getOrdreAffichage()).isEqualTo(DEFAULT_ORDRE_AFFICHAGE);
    }

    @Test
    @Transactional
    void fullUpdateApplicationCategoryWithPatch() throws Exception {
        // Initialize the database
        applicationCategoryRepository.saveAndFlush(applicationCategory);

        int databaseSizeBeforeUpdate = applicationCategoryRepository.findAll().size();

        // Update the applicationCategory using partial update
        ApplicationCategory partialUpdatedApplicationCategory = new ApplicationCategory();
        partialUpdatedApplicationCategory.setId(applicationCategory.getId());

        partialUpdatedApplicationCategory
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .ordreAffichage(UPDATED_ORDRE_AFFICHAGE);

        restApplicationCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationCategory))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeUpdate);
        ApplicationCategory testApplicationCategory = applicationCategoryList.get(applicationCategoryList.size() - 1);
        assertThat(testApplicationCategory.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testApplicationCategory.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testApplicationCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApplicationCategory.getOrdreAffichage()).isEqualTo(UPDATED_ORDRE_AFFICHAGE);
    }

    @Test
    @Transactional
    void patchNonExistingApplicationCategory() throws Exception {
        int databaseSizeBeforeUpdate = applicationCategoryRepository.findAll().size();
        applicationCategory.setId(count.incrementAndGet());

        // Create the ApplicationCategory
        ApplicationCategoryDTO applicationCategoryDTO = applicationCategoryMapper.toDto(applicationCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicationCategoryDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicationCategory() throws Exception {
        int databaseSizeBeforeUpdate = applicationCategoryRepository.findAll().size();
        applicationCategory.setId(count.incrementAndGet());

        // Create the ApplicationCategory
        ApplicationCategoryDTO applicationCategoryDTO = applicationCategoryMapper.toDto(applicationCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicationCategory() throws Exception {
        int databaseSizeBeforeUpdate = applicationCategoryRepository.findAll().size();
        applicationCategory.setId(count.incrementAndGet());

        // Create the ApplicationCategory
        ApplicationCategoryDTO applicationCategoryDTO = applicationCategoryMapper.toDto(applicationCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplicationCategory() throws Exception {
        // Initialize the database
        applicationCategoryRepository.saveAndFlush(applicationCategory);

        int databaseSizeBeforeDelete = applicationCategoryRepository.findAll().size();

        // Delete the applicationCategory
        restApplicationCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicationCategory.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
