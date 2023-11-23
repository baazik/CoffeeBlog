package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.data.entities.ArticleEntity;
import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.util.List;

public interface ArticleService {

    void create(ArticleDTO article);

    List<ArticleDTO> getAll();

    ArticleDTO getById(long articleId);

    void edit(ArticleDTO article);

    void remove(long ArticleId);

    Page<ArticleEntity> findPaginated(int page);

}
