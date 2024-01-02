package cz.hocuspocus.coffeeblog.models.dto.mappers;

import cz.hocuspocus.coffeeblog.data.entities.CommentEntity;
import cz.hocuspocus.coffeeblog.models.dto.CommentDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-02T13:09:28+0100",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Eclipse Adoptium)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentEntity toEntity(CommentDTO source) {
        if ( source == null ) {
            return null;
        }

        CommentEntity commentEntity = new CommentEntity();

        commentEntity.setId( source.getId() );
        commentEntity.setComment( source.getComment() );
        commentEntity.setDate( source.getDate() );
        commentEntity.setArticle( source.getArticle() );
        commentEntity.setUser( source.getUser() );
        commentEntity.setKarma( source.getKarma() );
        commentEntity.setUpVotes( source.getUpVotes() );
        commentEntity.setDownVotes( source.getDownVotes() );

        return commentEntity;
    }

    @Override
    public CommentDTO toDTO(CommentEntity source) {
        if ( source == null ) {
            return null;
        }

        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setUser( source.getUser() );
        commentDTO.setComment( source.getComment() );
        commentDTO.setDate( source.getDate() );
        commentDTO.setArticle( source.getArticle() );
        commentDTO.setKarma( source.getKarma() );
        commentDTO.setUpVotes( source.getUpVotes() );
        commentDTO.setDownVotes( source.getDownVotes() );
        commentDTO.setId( source.getId() );

        return commentDTO;
    }

    @Override
    public void updateCommentDTO(CommentDTO source, CommentDTO target) {
        if ( source == null ) {
            return;
        }

        target.setUser( source.getUser() );
        target.setComment( source.getComment() );
        target.setDate( source.getDate() );
        target.setArticle( source.getArticle() );
        target.setKarma( source.getKarma() );
        target.setUpVotes( source.getUpVotes() );
        target.setDownVotes( source.getDownVotes() );
        target.setId( source.getId() );
    }

    @Override
    public void updateCommentEntity(CommentDTO source, CommentEntity target) {
        if ( source == null ) {
            return;
        }

        target.setId( source.getId() );
        target.setComment( source.getComment() );
        target.setDate( source.getDate() );
        target.setArticle( source.getArticle() );
        target.setUser( source.getUser() );
        target.setKarma( source.getKarma() );
        target.setUpVotes( source.getUpVotes() );
        target.setDownVotes( source.getDownVotes() );
    }
}
