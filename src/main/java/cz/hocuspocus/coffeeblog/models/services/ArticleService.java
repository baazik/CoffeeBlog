package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;

import java.util.List;

public interface ArticleService {

    void create(ArticleDTO article);

    List<ArticleDTO> getAll();

}
