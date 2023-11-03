package cz.hocuspocus.coffeeblog.data.repositories;

import cz.hocuspocus.coffeeblog.data.entities.ArticleEntity;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<ArticleEntity, Long> {

}
