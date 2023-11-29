package cz.hocuspocus.coffeeblog.data.repositories;

import cz.hocuspocus.coffeeblog.data.entities.ArticleEntity;
import cz.hocuspocus.coffeeblog.data.entities.ArticleRatingEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleRatingRepository extends CrudRepository<ArticleRatingEntity, Long> {

    List<ArticleRatingEntity> findByArticle(ArticleEntity article);

}
