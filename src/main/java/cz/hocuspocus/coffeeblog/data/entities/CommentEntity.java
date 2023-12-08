package cz.hocuspocus.coffeeblog.data.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column
    private LocalDateTime date;

    // Comment has link to article
    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity article;

    // Comment has link to user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false)
    private int karma;

    @Column(nullable = false)
    private int upVotes;

    @Column(nullable = false)
    private int downVotes;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<CommentRatingEntity> userRatings = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }

    public List<CommentRatingEntity> getUserRatings() {
        return userRatings;
    }

    public void setUserRatings(List<CommentRatingEntity> userRatings) {
        this.userRatings = userRatings;
    }
}
