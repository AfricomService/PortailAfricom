package com.societe.portail.repository;

import com.societe.portail.domain.ApplicationCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ApplicationCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationCategoryRepository extends JpaRepository<ApplicationCategory, Long> {}
