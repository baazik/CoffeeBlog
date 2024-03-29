package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.data.entities.ArticleEntity;
import cz.hocuspocus.coffeeblog.data.entities.ArticleRatingEntity;
import cz.hocuspocus.coffeeblog.data.entities.UserEntity;
import cz.hocuspocus.coffeeblog.data.repositories.ArticleRatingRepository;
import cz.hocuspocus.coffeeblog.data.repositories.ArticleRepository;
import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import cz.hocuspocus.coffeeblog.models.dto.mappers.ArticleMapper;
import cz.hocuspocus.coffeeblog.models.exceptions.ArticleNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ArticleServiceImp implements ArticleService{

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleRatingRepository articleRatingRepository;

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
        article.setDate(LocalDateTime.now());
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
                .orElseThrow(ArticleNotFoundException::new);
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

    @Override
    public ArticleEntity getEntityById(long articleId) {
        ArticleEntity fetchedArticle = getArticleOrThrow(articleId);
        return fetchedArticle;
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

    @Override
    public Page<ArticleEntity> findPaginated(int page) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("date").descending());
        return articleRepository.findAll(pageable);
    }

    @Override
    public boolean hasUserRated(ArticleEntity article, UserEntity user) {
        for (ArticleRatingEntity rating : article.getUserRatings()) {
            if (rating.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateVotes(ArticleEntity article){
        List<ArticleRatingEntity> ratings = articleRatingRepository.findByArticle(article);

        int upVotes = 0;

        for (ArticleRatingEntity rating : ratings) {
            if (rating.getRating() == 1) {
                upVotes++;
            }
        }

        article.setUpVotes(upVotes);

        articleRepository.save(article);
    }

    @Override
    public void saveRating (ArticleRatingEntity rating){
        articleRatingRepository.save(rating);
    }

    @Override
    public void upVote(ArticleEntity article, UserEntity user){
        ArticleRatingEntity rating = new ArticleRatingEntity();
        rating.setUser(user);
        rating.setArticle(article);
        rating.setRating(1);
        saveRating(rating);
        updateVotes(article);
    }

    /*
    Method for tracking visits in article - because of cookies, user (logged or unlogged) will increase the counter in the same article just once
     */
    @Override
    public void visit(ArticleEntity article, HttpServletRequest request, HttpServletResponse response){
        boolean hasVisited = hasUserVisited(article, request);

        if (!hasVisited) {
            article.setVisit(article.getVisit() + 1);
            articleRepository.save(article);

            markArticleAsVisited(article, response);
        }
    }

    private boolean hasUserVisited(ArticleEntity article, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("visited_articles") && cookie.getValue().contains(String.valueOf(article.getArticleId()))) {
                    return true;
                }
            }
        }

        return false;
    }

    private void markArticleAsVisited(ArticleEntity article, HttpServletResponse response) {
        Cookie cookie = new Cookie("visited_articles", article.getArticleId() + "");
        cookie.setMaxAge(365 * 24 * 60 * 60); // Expires in one year
        cookie.setPath("/"); // Set the cookie path as needed
        response.addCookie(cookie);
    }


}
