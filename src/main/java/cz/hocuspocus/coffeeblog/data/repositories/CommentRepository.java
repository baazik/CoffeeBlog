package cz.hocuspocus.coffeeblog.data.repositories;

import cz.hocuspocus.coffeeblog.data.entities.CommentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends CrudRepository<CommentEntity, Long> {

    @Query("SELECT c FROM CommentEntity c WHERE c.article.articleId = :articleId ORDER BY c.date DESC")
    List<CommentEntity> findByArticleId(@Param("articleId") long articleId);

}
