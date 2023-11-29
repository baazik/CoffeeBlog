package cz.hocuspocus.coffeeblog.data.entities;

import jakarta.persistence.*;

@Entity
public class ArticleRatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ratingId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity article;

    private int rating;

    public long getRatingId() {
        return ratingId;
    }

    public void setRatingId(long ratingId) {
        this.ratingId = ratingId;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ArticleEntity getArticle() {
        return article;
    }

    public void setArticle(ArticleEntity article) {
        this.article = article;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


}
