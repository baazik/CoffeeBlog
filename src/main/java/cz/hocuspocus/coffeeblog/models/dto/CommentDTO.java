package cz.hocuspocus.coffeeblog.models.dto;

import cz.hocuspocus.coffeeblog.data.entities.ArticleEntity;
import cz.hocuspocus.coffeeblog.data.entities.UserEntity;

import java.time.LocalDateTime;

public class CommentDTO {

    private LocalDateTime date;

    private String comment;

    // Comment has link to User
    private UserEntity user;

    // Comment has link to Article
    private ArticleEntity article;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public ArticleEntity getArticle() {
        return article;
    }

    public void setArticle(ArticleEntity article) {
        this.article = article;
    }
}
