package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.data.entities.RecipeEntity;
import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import cz.hocuspocus.coffeeblog.models.dto.RecipeDTO;

import java.util.List;
import java.util.Map;

public interface RecipeService {

    void create(RecipeDTO recipe);

    RecipeDTO getById(long recipeId);

    List<RecipeDTO> getAll();

    void edit(RecipeDTO recipe);

    void remove(long recipeId);

    Map<Character, List<RecipeEntity>> getAllRecipesByAlphabet();

    Map<Character, List<RecipeEntity>> getAllRecipesByAlphabetSorted();

}
