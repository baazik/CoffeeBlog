package cz.hocuspocus.coffeeblog.models.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class ArticleDTO {

    /* Zde později přidáme ještě jeden atribut */

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
    //endregion

}
