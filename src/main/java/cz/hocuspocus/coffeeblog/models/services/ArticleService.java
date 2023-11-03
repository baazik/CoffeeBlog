package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;

public interface ArticleService {

    void create(ArticleDTO article);

}
