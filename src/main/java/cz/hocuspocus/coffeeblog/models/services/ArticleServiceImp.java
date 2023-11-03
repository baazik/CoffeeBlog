package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.data.entities.ArticleEntity;
import cz.hocuspocus.coffeeblog.data.repositories.ArticleRepository;
import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImp implements ArticleService{

    @Autowired
    private ArticleRepository articleRepository;

    /**
     * We take data from ArticleDTO and put it into ArticleEntity newArticle
     * with setters, we se  title, content and description to the newArticle
     * then we save it to DB
     * @param article
     */
    @Override
    public void create(ArticleDTO article){
        ArticleEntity newArticle = new ArticleEntity();

        // this part will be later on changed with the mapper
        newArticle.setTitle(article.getTitle());
        newArticle.setContent(article.getContent());
        newArticle.setDescription(article.getDescription());

        articleRepository.save(newArticle);
    }

}
