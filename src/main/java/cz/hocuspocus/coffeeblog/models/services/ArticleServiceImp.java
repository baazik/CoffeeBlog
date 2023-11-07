package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.data.entities.ArticleEntity;
import cz.hocuspocus.coffeeblog.data.repositories.ArticleRepository;
import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import cz.hocuspocus.coffeeblog.models.dto.mappers.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ArticleServiceImp implements ArticleService{

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * We take data from ArticleDTO and put it into ArticleEntity newArticle
     * with setters, we set title, content and description to the newArticle
     * then we save it to DB
     * @param article
     */
    @Override
    public void create(ArticleDTO article){
        // we map data from DTO to Entity here
        ArticleEntity newArticle = articleMapper.toEntity(article);

        articleRepository.save(newArticle);
    }

    /**
     * We get all the saved articles from DB as List of ArticleDTO
     * @return List of all articles in List<ArticleDTO>
     */
    @Override
    public List<ArticleDTO> getAll() {
        return StreamSupport.stream(articleRepository.findAll().spliterator(), false)
                .map(i -> articleMapper.toDTO(i))
                .toList();
    }

    /**
     * We get an articleRepository with articleId here
     * Then we can use in method edit, getById or remove
     * @param articleId
     * @return
     */
    private ArticleEntity getArticleOrThrow(long articleId) {
        return articleRepository
                .findById(articleId)
                .orElseThrow();
    }

    /**
     * We get an article by it's ID
      * @param articleId
     * @return articleMapper.toDTO(fetchedArticle)
     */
    @Override
    public ArticleDTO getById(long articleId) {
        ArticleEntity fetchedArticle = getArticleOrThrow(articleId);

        return articleMapper.toDTO(fetchedArticle);
    }

    /**
     * We edit an article
     * We create a new instance of ArticleEntity fetchedArticle and we "connect" it with ArticleDTO article with id
     * Then we use updateArticleEntity from articleMapper to fill values from DTO to Entity
     * Then we save the fetchedArticle to DB
     * @param article
     */
    @Override
    public void edit(ArticleDTO article) {
        ArticleEntity fetchedArticle = getArticleOrThrow(article.getArticleId());

        articleMapper.updateArticleEntity(article, fetchedArticle);
        articleRepository.save(fetchedArticle);
    }

    /**
     * We create a new instance of ArticleEntity fetchedEntity and we fill it with articleRepository by articleId
     * Then we delete such entity from DB by articleRepository.delete
     * @param articleId
     */
    @Override
    public void remove(long articleId){
        ArticleEntity fetchedEntity = getArticleOrThrow(articleId);
        articleRepository.delete(fetchedEntity);
    }


}
