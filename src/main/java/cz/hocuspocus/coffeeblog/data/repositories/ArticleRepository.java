package cz.hocuspocus.coffeeblog.data.repositories;

import cz.hocuspocus.coffeeblog.data.entities.ArticleEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ArticleRepository extends CrudRepository<ArticleEntity, Long> {

    @Transactional(readOnly = true)
    Page<ArticleEntity> findAll(Pageable pageable) throws DataAccessException;

}
