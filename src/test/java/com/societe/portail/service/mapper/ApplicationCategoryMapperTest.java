package com.societe.portail.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApplicationCategoryMapperTest {

    private ApplicationCategoryMapper applicationCategoryMapper;

    @BeforeEach
    public void setUp() {
        applicationCategoryMapper = new ApplicationCategoryMapperImpl();
    }
}
