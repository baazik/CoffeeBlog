package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.data.entities.RecipeEntity;
import cz.hocuspocus.coffeeblog.data.repositories.RecipeRepository;
import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import cz.hocuspocus.coffeeblog.models.dto.RecipeDTO;
import cz.hocuspocus.coffeeblog.models.dto.mappers.RecipeMapper;
import cz.hocuspocus.coffeeblog.models.exceptions.RecipeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class RecipeServiceImpl implements RecipeService{

    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private RecipeRepository recipeRepository;

    private RecipeEntity getRecipeOrThrow(long recipeId) {
        return recipeRepository
                .findById(recipeId)
                .orElseThrow(RecipeNotFoundException::new);
    }

    @Override
    public RecipeDTO getById(long recipeId) {
        RecipeEntity fetchedRecipe = getRecipeOrThrow(recipeId);
        return recipeMapper.toDTO(fetchedRecipe);
    }

    @Override
    public List<RecipeDTO> getAll() {
        return StreamSupport.stream(recipeRepository.findAll().spliterator(), false)
                .map(i -> recipeMapper.toDTO(i))
                .toList();
    }

    @Override
    public void create(RecipeDTO recipe){
        RecipeEntity newRecipe = recipeMapper.toEntity(recipe);
        recipeRepository.save(newRecipe);
    }

    @Override
    public void edit(RecipeDTO recipe) {
        RecipeEntity fetchedRecipe = getRecipeOrThrow(recipe.getRecipeId());

        recipeMapper.updateRecipeEntity(recipe, fetchedRecipe);
        recipeRepository.save(fetchedRecipe);
    }

    @Override
    public void remove(long recipeId){
        RecipeEntity fetchedRecipe = getRecipeOrThrow(recipeId);
        recipeRepository.delete(fetchedRecipe);
    }

    @Override
    public Map<Character, List<RecipeEntity>> getAllRecipesByAlphabetSorted() {
        Map<Character, List<RecipeEntity>> recipesByAlphabet = getAllRecipesByAlphabet();

        // Seřadit seznamy receptů v každé skupině
        recipesByAlphabet.forEach((key, value) -> {
            value.sort(Comparator.comparing(RecipeEntity::getTitle, String.CASE_INSENSITIVE_ORDER));
        });

        // Seřadit mapu podle klíčů (abecedně)
        Map<Character, List<RecipeEntity>> sortedRecipesByAlphabet = new TreeMap<>(recipesByAlphabet);

        return sortedRecipesByAlphabet;
    }

    @Override
    public Map<Character, List<RecipeEntity>> getAllRecipesByAlphabet() {
        Map<Character, List<RecipeEntity>> recipesByAlphabet = new HashMap<>();

        for (char letter = 'A'; letter <= 'Z'; letter++) {
            String letterAsString = String.valueOf(letter);
            List<RecipeEntity> recipesForLetter = recipeRepository.findByTitleStartingWithOrderByTitleAsc(letterAsString);
            if (!recipesForLetter.isEmpty()) {
                recipesForLetter.sort(Comparator.comparing(recipe -> recipe.getTitle(), String.CASE_INSENSITIVE_ORDER));
                recipesByAlphabet.put(letter, recipesForLetter);
            }
        }

        return recipesByAlphabet;

    }

}
