package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.data.entities.ArticleEntity;
import cz.hocuspocus.coffeeblog.data.entities.RecipeEntity;
import cz.hocuspocus.coffeeblog.data.repositories.RecipeRepository;
import cz.hocuspocus.coffeeblog.models.dto.RecipeDTO;
import cz.hocuspocus.coffeeblog.models.dto.mappers.RecipeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService{

    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private RecipeRepository recipeRepository;

    @Override
    public void create(RecipeDTO recipe){
        RecipeEntity newRecipe = recipeMapper.toEntity(recipe);
        recipeRepository.save(newRecipe);
    }

}
