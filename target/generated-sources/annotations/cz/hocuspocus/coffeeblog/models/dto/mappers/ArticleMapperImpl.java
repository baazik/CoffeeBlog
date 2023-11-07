package cz.hocuspocus.coffeeblog.models.dto.mappers;

import cz.hocuspocus.coffeeblog.data.entities.ArticleEntity;
import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-06T11:17:00+0100",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Eclipse Adoptium)"
)
@Component
public class ArticleMapperImpl implements ArticleMapper {

    @Override
    public ArticleEntity toEntity(ArticleDTO source) {
        if ( source == null ) {
            return null;
        }

        ArticleEntity articleEntity = new ArticleEntity();

        articleEntity.setArticleId( source.getArticleId() );
        articleEntity.setTitle( source.getTitle() );
        articleEntity.setDescription( source.getDescription() );
        articleEntity.setContent( source.getContent() );

        return articleEntity;
    }

    @Override
    public ArticleDTO toDTO(ArticleEntity source) {
        if ( source == null ) {
            return null;
        }

        ArticleDTO articleDTO = new ArticleDTO();

        articleDTO.setTitle( source.getTitle() );
        articleDTO.setDescription( source.getDescription() );
        articleDTO.setContent( source.getContent() );
        articleDTO.setArticleId( source.getArticleId() );

        return articleDTO;
    }

    @Override
    public void updateArticleDTO(ArticleDTO source, ArticleDTO target) {
        if ( source == null ) {
            return;
        }

        target.setTitle( source.getTitle() );
        target.setDescription( source.getDescription() );
        target.setContent( source.getContent() );
        target.setArticleId( source.getArticleId() );
    }

    @Override
    public void updateArticleEntity(ArticleDTO source, ArticleEntity target) {
        if ( source == null ) {
            return;
        }

        target.setArticleId( source.getArticleId() );
        target.setTitle( source.getTitle() );
        target.setDescription( source.getDescription() );
        target.setContent( source.getContent() );
    }
}
