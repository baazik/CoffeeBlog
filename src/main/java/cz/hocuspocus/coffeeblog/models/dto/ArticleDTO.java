package cz.hocuspocus.coffeeblog.models.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;


public class ArticleDTO {

    private long articleId;

    @NotBlank(message = "Insert the title")
    @NotNull(message = "Insert the title")
    @Size(max = 100, message = "The title is too long")
    private String title;

    @NotBlank(message = "Insert the description")
    @NotNull(message = "Insert the description")
    private String description;

    @NotBlank(message = "Insert the content")
    @NotNull(message = "Insert the content")
    private String content;

    private LocalDateTime date;

    private int karma;

    private int upVotes;

    private int downVotes;

    //region: Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    //endregion


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
}
