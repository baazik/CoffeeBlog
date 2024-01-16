package cz.hocuspocus.coffeeblog.models.dto.mappers;

import cz.hocuspocus.coffeeblog.data.entities.RecipeEntity;
import cz.hocuspocus.coffeeblog.models.dto.RecipeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    RecipeEntity toEntity(RecipeDTO source);

    RecipeDTO toDTO(RecipeEntity source);

    void updateRecipeDTO(RecipeDTO source, @MappingTarget RecipeDTO target);

    void updateRecipeEntity(RecipeDTO source, @MappingTarget RecipeEntity target);

}
