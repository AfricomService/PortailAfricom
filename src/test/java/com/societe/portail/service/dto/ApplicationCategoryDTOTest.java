package com.societe.portail.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.societe.portail.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApplicationCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationCategoryDTO.class);
        ApplicationCategoryDTO applicationCategoryDTO1 = new ApplicationCategoryDTO();
        applicationCategoryDTO1.setId(1L);
        ApplicationCategoryDTO applicationCategoryDTO2 = new ApplicationCategoryDTO();
        assertThat(applicationCategoryDTO1).isNotEqualTo(applicationCategoryDTO2);
        applicationCategoryDTO2.setId(applicationCategoryDTO1.getId());
        assertThat(applicationCategoryDTO1).isEqualTo(applicationCategoryDTO2);
        applicationCategoryDTO2.setId(2L);
        assertThat(applicationCategoryDTO1).isNotEqualTo(applicationCategoryDTO2);
        applicationCategoryDTO1.setId(null);
        assertThat(applicationCategoryDTO1).isNotEqualTo(applicationCategoryDTO2);
    }
}
