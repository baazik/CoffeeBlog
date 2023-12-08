package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.data.entities.CommentEntity;
import cz.hocuspocus.coffeeblog.data.entities.CommentRatingEntity;
import cz.hocuspocus.coffeeblog.data.entities.UserEntity;
import cz.hocuspocus.coffeeblog.models.dto.CommentDTO;

import java.util.List;

public interface CommentService {

    List<CommentDTO> getByArticleId(long articleId);

    void create(CommentDTO comment, long articleId);

    UserEntity getLoggedUser();

    boolean hasUserRated(CommentEntity comment, UserEntity user);

    void updateKarma(CommentEntity comment);

    void updateVotes(CommentEntity comment);

    void upVote (CommentEntity comment, UserEntity user);

    void downVote (CommentEntity comment, UserEntity user);

    void saveRating (CommentRatingEntity rating);

    CommentEntity getCommentEntityById(long commentId);

}
