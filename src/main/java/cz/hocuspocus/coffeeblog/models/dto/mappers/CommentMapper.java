package cz.hocuspocus.coffeeblog.models.dto.mappers;

import cz.hocuspocus.coffeeblog.data.entities.ArticleEntity;
import cz.hocuspocus.coffeeblog.data.entities.CommentEntity;
import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import cz.hocuspocus.coffeeblog.models.dto.CommentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentEntity toEntity(CommentDTO source);

    CommentDTO toDTO(CommentEntity source);

    void updateCommentDTO(CommentDTO source, @MappingTarget CommentDTO target);

    void updateCommentEntity(CommentDTO source, @MappingTarget CommentEntity target);

}
