package cz.hocuspocus.coffeeblog.models.dto.mappers;

import cz.hocuspocus.coffeeblog.data.entities.RecipeEntity;
import cz.hocuspocus.coffeeblog.models.dto.RecipeDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-31T14:33:57+0100",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Eclipse Adoptium)"
)
@Component
public class RecipeMapperImpl implements RecipeMapper {

    @Override
    public RecipeEntity toEntity(RecipeDTO source) {
        if ( source == null ) {
            return null;
        }

        RecipeEntity recipeEntity = new RecipeEntity();

        recipeEntity.setRecipeId( source.getRecipeId() );
        recipeEntity.setTitle( source.getTitle() );
        recipeEntity.setDescription( source.getDescription() );
        recipeEntity.setContent( source.getContent() );
        recipeEntity.setUpVotes( source.getUpVotes() );
        recipeEntity.setTags( source.getTags() );
        recipeEntity.setImage( source.getImage() );

        return recipeEntity;
    }

    @Override
    public RecipeDTO toDTO(RecipeEntity source) {
        if ( source == null ) {
            return null;
        }

        RecipeDTO recipeDTO = new RecipeDTO();

        recipeDTO.setRecipeId( source.getRecipeId() );
        recipeDTO.setTitle( source.getTitle() );
        recipeDTO.setDescription( source.getDescription() );
        recipeDTO.setContent( source.getContent() );
        recipeDTO.setUpVotes( source.getUpVotes() );
        recipeDTO.setTags( source.getTags() );
        recipeDTO.setImage( source.getImage() );

        return recipeDTO;
    }

    @Override
    public void updateRecipeDTO(RecipeDTO source, RecipeDTO target) {
        if ( source == null ) {
            return;
        }

        target.setRecipeId( source.getRecipeId() );
        target.setTitle( source.getTitle() );
        target.setDescription( source.getDescription() );
        target.setContent( source.getContent() );
        target.setUpVotes( source.getUpVotes() );
        target.setTags( source.getTags() );
        target.setImage( source.getImage() );
    }

    @Override
    public void updateRecipeEntity(RecipeDTO source, RecipeEntity target) {
        if ( source == null ) {
            return;
        }

        target.setRecipeId( source.getRecipeId() );
        target.setTitle( source.getTitle() );
        target.setDescription( source.getDescription() );
        target.setContent( source.getContent() );
        target.setUpVotes( source.getUpVotes() );
        target.setTags( source.getTags() );
        target.setImage( source.getImage() );
    }
}
