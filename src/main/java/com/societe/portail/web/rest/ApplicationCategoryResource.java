package com.societe.portail.web.rest;

import com.societe.portail.repository.ApplicationCategoryRepository;
import com.societe.portail.service.ApplicationCategoryService;
import com.societe.portail.service.dto.ApplicationCategoryDTO;
import com.societe.portail.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.societe.portail.domain.ApplicationCategory}.
 */
@RestController
@RequestMapping("/api")
public class ApplicationCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationCategoryResource.class);

    private static final String ENTITY_NAME = "applicationCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationCategoryService applicationCategoryService;

    private final ApplicationCategoryRepository applicationCategoryRepository;

    public ApplicationCategoryResource(
        ApplicationCategoryService applicationCategoryService,
        ApplicationCategoryRepository applicationCategoryRepository
    ) {
        this.applicationCategoryService = applicationCategoryService;
        this.applicationCategoryRepository = applicationCategoryRepository;
    }

    /**
     * {@code POST  /application-categories} : Create a new applicationCategory.
     *
     * @param applicationCategoryDTO the applicationCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicationCategoryDTO, or with status {@code 400 (Bad Request)} if the applicationCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/application-categories")
    public ResponseEntity<ApplicationCategoryDTO> createApplicationCategory(
        @Valid @RequestBody ApplicationCategoryDTO applicationCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ApplicationCategory : {}", applicationCategoryDTO);
        if (applicationCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new applicationCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApplicationCategoryDTO result = applicationCategoryService.save(applicationCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/application-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /application-categories/:id} : Updates an existing applicationCategory.
     *
     * @param id the id of the applicationCategoryDTO to save.
     * @param applicationCategoryDTO the applicationCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the applicationCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicationCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/application-categories/{id}")
    public ResponseEntity<ApplicationCategoryDTO> updateApplicationCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ApplicationCategoryDTO applicationCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ApplicationCategory : {}, {}", id, applicationCategoryDTO);
        if (applicationCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApplicationCategoryDTO result = applicationCategoryService.save(applicationCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, applicationCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /application-categories/:id} : Partial updates given fields of an existing applicationCategory, field will ignore if it is null
     *
     * @param id the id of the applicationCategoryDTO to save.
     * @param applicationCategoryDTO the applicationCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the applicationCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the applicationCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the applicationCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/application-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApplicationCategoryDTO> partialUpdateApplicationCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ApplicationCategoryDTO applicationCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApplicationCategory partially : {}, {}", id, applicationCategoryDTO);
        if (applicationCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApplicationCategoryDTO> result = applicationCategoryService.partialUpdate(applicationCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, applicationCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /application-categories} : get all the applicationCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applicationCategories in body.
     */
    @GetMapping("/application-categories")
    public List<ApplicationCategoryDTO> getAllApplicationCategories() {
        log.debug("REST request to get all ApplicationCategories");
        return applicationCategoryService.findAll();
    }

    /**
     * {@code GET  /application-categories/:id} : get the "id" applicationCategory.
     *
     * @param id the id of the applicationCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/application-categories/{id}")
    public ResponseEntity<ApplicationCategoryDTO> getApplicationCategory(@PathVariable Long id) {
        log.debug("REST request to get ApplicationCategory : {}", id);
        Optional<ApplicationCategoryDTO> applicationCategoryDTO = applicationCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(applicationCategoryDTO);
    }

    /**
     * {@code DELETE  /application-categories/:id} : delete the "id" applicationCategory.
     *
     * @param id the id of the applicationCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/application-categories/{id}")
    public ResponseEntity<Void> deleteApplicationCategory(@PathVariable Long id) {
        log.debug("REST request to delete ApplicationCategory : {}", id);
        applicationCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
