package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.data.entities.*;
import cz.hocuspocus.coffeeblog.data.repositories.ArticleRepository;
import cz.hocuspocus.coffeeblog.data.repositories.CommentRatingRepository;
import cz.hocuspocus.coffeeblog.data.repositories.CommentRepository;
import cz.hocuspocus.coffeeblog.data.repositories.UserRepository;
import cz.hocuspocus.coffeeblog.models.dto.CommentDTO;
import cz.hocuspocus.coffeeblog.models.dto.mappers.CommentMapper;
import cz.hocuspocus.coffeeblog.models.exceptions.ArticleNotFoundException;
import cz.hocuspocus.coffeeblog.models.exceptions.CommentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRatingRepository commentRatingRepository;

    private CommentEntity getCommentOrThrow(long commentId) {
        return commentRepository
                .findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }

    @Override
    public CommentEntity getCommentEntityById(long commentId) {
        CommentEntity fetchedComment = getCommentOrThrow(commentId);
        return fetchedComment;
    }

    /**
     * We get logged user here as entity
     * @return userEntity
     */
    @Override
    public UserEntity getLoggedUser(){
        // Get email of logged user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // Get userEntity via userEmail
        UserEntity userEntity = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return userEntity;
    }

    /**
     * We get List of CommentDTO here by articleId
     * @param articleId
     * @return List<CommentDTO>
     */
    @Override
    public List<CommentDTO> getByArticleId(long articleId) {
        return commentRepository.findByArticleId(articleId).stream()
                .map(commentMapper::toDTO)
                .toList();
    }

    /**
     * we set LocalDateTime.now() to comment
     * we set User to comment - we take logged user, that means user, that writes the comment
     * we create a new ArticleEntity articleById, in which we save article from DB by id from URL
     * we set this articleByID to the comment
     * we create a new CommentEntity newComment and with mapper, we save DTO comment to it
     * then we save it to DB
     * @param comment new CommentDTO
     * @param articleId will be taken from URL
     */
    @Override
    public void create(CommentDTO comment, long articleId) {
        comment.setDate(LocalDateTime.now());
        comment.setUser(getLoggedUser());

        ArticleEntity articleById = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
        comment.setArticle(articleById);

        CommentEntity newComment = commentMapper.toEntity(comment);
        commentRepository.save(newComment);
    }

    @Override
    public boolean hasUserRated(CommentEntity comment, UserEntity user) {
        for (CommentRatingEntity rating : comment.getUserRatings()) {
            if (rating.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateVotes(CommentEntity comment){
        List<CommentRatingEntity> ratings = commentRatingRepository.findByComment(comment);

        int upVotes = 0;

        for (CommentRatingEntity rating : ratings) {
            if (rating.getRating() == 1) {
                upVotes++;
            }
        }

        comment.setUpVotes(upVotes);

        commentRepository.save(comment);
    }

    @Override
    public void saveRating (CommentRatingEntity rating){
        commentRatingRepository.save(rating);
    }

    @Override
    public void upVote(CommentEntity comment, UserEntity user){
        CommentRatingEntity rating = new CommentRatingEntity();
        rating.setUser(user);
        rating.setComment(comment);
        rating.setRating(1);
        saveRating(rating);
        updateVotes(comment);
    }

}
