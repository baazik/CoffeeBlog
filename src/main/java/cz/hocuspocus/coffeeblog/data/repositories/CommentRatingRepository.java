package cz.hocuspocus.coffeeblog.data.repositories;

import cz.hocuspocus.coffeeblog.data.entities.CommentEntity;
import cz.hocuspocus.coffeeblog.data.entities.CommentRatingEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRatingRepository extends CrudRepository<CommentRatingEntity, Long> {

    List<CommentRatingEntity> findByComment(CommentEntity comment);

}
