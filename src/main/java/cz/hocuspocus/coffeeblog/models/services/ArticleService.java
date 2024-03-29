package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.data.entities.ArticleEntity;
import cz.hocuspocus.coffeeblog.data.entities.ArticleRatingEntity;
import cz.hocuspocus.coffeeblog.data.entities.UserEntity;
import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.util.List;

public interface ArticleService {

    void create(ArticleDTO article);

    List<ArticleDTO> getAll();

    ArticleDTO getById(long articleId);

    ArticleEntity getEntityById(long articleId);

    void edit(ArticleDTO article);

    void remove(long ArticleId);

    Page<ArticleEntity> findPaginated(int page);

    boolean hasUserRated(ArticleEntity article, UserEntity user);

    void updateVotes(ArticleEntity article);

    void upVote (ArticleEntity article, UserEntity user);

    void saveRating (ArticleRatingEntity rating);

    void visit (ArticleEntity article, HttpServletRequest request, HttpServletResponse response);


}
