package com.societe.portail.service.mapper;

import com.societe.portail.domain.Application;
import com.societe.portail.service.dto.ApplicationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Application} and its DTO {@link ApplicationDTO}.
 */
@Mapper(componentModel = "spring", uses = { ApplicationCategoryMapper.class })
public interface ApplicationMapper extends EntityMapper<ApplicationDTO, Application> {
    @Mapping(target = "categorie", source = "categorie", qualifiedByName = "nom")
    ApplicationDTO toDto(Application s);
}
