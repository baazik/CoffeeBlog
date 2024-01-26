package cz.hocuspocus.coffeeblog.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RecipeDTO {

    private long recipeId;

    @NotBlank(message = "Vložte název")
    @NotNull(message = "Vložte název")
    @Size(max = 100, message = "Název je přílši dlouhý")
    private String title;

    @NotBlank(message = "Vložte popis")
    @NotNull(message = "Vložte popis")
    private String description;

    @NotBlank(message = "Vložte obsah")
    @NotNull(message = "Vložte obsah")
    private String content;

    private int upVotes;

    private String tags;

    private String image;

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

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

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
            this.image = image;
    }
}
