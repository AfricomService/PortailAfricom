package com.societe.portail.service;

import com.societe.portail.domain.ApplicationCategory;
import com.societe.portail.repository.ApplicationCategoryRepository;
import com.societe.portail.service.dto.ApplicationCategoryDTO;
import com.societe.portail.service.mapper.ApplicationCategoryMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ApplicationCategory}.
 */
@Service
@Transactional
public class ApplicationCategoryService {

    private final Logger log = LoggerFactory.getLogger(ApplicationCategoryService.class);

    private final ApplicationCategoryRepository applicationCategoryRepository;

    private final ApplicationCategoryMapper applicationCategoryMapper;

    public ApplicationCategoryService(
        ApplicationCategoryRepository applicationCategoryRepository,
        ApplicationCategoryMapper applicationCategoryMapper
    ) {
        this.applicationCategoryRepository = applicationCategoryRepository;
        this.applicationCategoryMapper = applicationCategoryMapper;
    }

    /**
     * Save a applicationCategory.
     *
     * @param applicationCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ApplicationCategoryDTO save(ApplicationCategoryDTO applicationCategoryDTO) {
        log.debug("Request to save ApplicationCategory : {}", applicationCategoryDTO);
        ApplicationCategory applicationCategory = applicationCategoryMapper.toEntity(applicationCategoryDTO);
        applicationCategory = applicationCategoryRepository.save(applicationCategory);
        return applicationCategoryMapper.toDto(applicationCategory);
    }

    /**
     * Partially update a applicationCategory.
     *
     * @param applicationCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ApplicationCategoryDTO> partialUpdate(ApplicationCategoryDTO applicationCategoryDTO) {
        log.debug("Request to partially update ApplicationCategory : {}", applicationCategoryDTO);

        return applicationCategoryRepository
            .findById(applicationCategoryDTO.getId())
            .map(existingApplicationCategory -> {
                applicationCategoryMapper.partialUpdate(existingApplicationCategory, applicationCategoryDTO);

                return existingApplicationCategory;
            })
            .map(applicationCategoryRepository::save)
            .map(applicationCategoryMapper::toDto);
    }

    /**
     * Get all the applicationCategories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ApplicationCategoryDTO> findAll() {
        log.debug("Request to get all ApplicationCategories");
        return applicationCategoryRepository
            .findAll()
            .stream()
            .map(applicationCategoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one applicationCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ApplicationCategoryDTO> findOne(Long id) {
        log.debug("Request to get ApplicationCategory : {}", id);
        return applicationCategoryRepository.findById(id).map(applicationCategoryMapper::toDto);
    }

    /**
     * Delete the applicationCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ApplicationCategory : {}", id);
        applicationCategoryRepository.deleteById(id);
    }
}
