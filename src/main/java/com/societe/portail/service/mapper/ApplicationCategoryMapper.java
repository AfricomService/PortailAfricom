package com.societe.portail.service.mapper;

import com.societe.portail.domain.ApplicationCategory;
import com.societe.portail.service.dto.ApplicationCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApplicationCategory} and its DTO {@link ApplicationCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApplicationCategoryMapper extends EntityMapper<ApplicationCategoryDTO, ApplicationCategory> {
    @Named("nom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    ApplicationCategoryDTO toDtoNom(ApplicationCategory applicationCategory);
}
