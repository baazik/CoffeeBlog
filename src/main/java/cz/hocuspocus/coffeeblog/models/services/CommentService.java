package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.data.entities.UserEntity;
import cz.hocuspocus.coffeeblog.models.dto.CommentDTO;

import java.util.List;

public interface CommentService {

    List<CommentDTO> getByArticleId(long articleId);

    void create(CommentDTO comment, long articleId);

    UserEntity getLoggedUser();

}
