package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.data.entities.ArticleEntity;
import cz.hocuspocus.coffeeblog.data.entities.CommentEntity;
import cz.hocuspocus.coffeeblog.data.entities.UserEntity;
import cz.hocuspocus.coffeeblog.data.repositories.ArticleRepository;
import cz.hocuspocus.coffeeblog.data.repositories.CommentRepository;
import cz.hocuspocus.coffeeblog.data.repositories.UserRepository;
import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import cz.hocuspocus.coffeeblog.models.dto.CommentDTO;
import cz.hocuspocus.coffeeblog.models.dto.mappers.ArticleMapper;
import cz.hocuspocus.coffeeblog.models.dto.mappers.CommentMapper;
import cz.hocuspocus.coffeeblog.models.exceptions.ArticleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public void create(CommentDTO comment, long articleId){
        comment.setDate(LocalDateTime.now());
        comment.setUser(getLoggedUser());
        ArticleEntity articleById = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
        comment.setArticle(articleById);
        CommentEntity newComment = commentMapper.toEntity(comment);
        commentRepository.save(newComment);
    }

}
