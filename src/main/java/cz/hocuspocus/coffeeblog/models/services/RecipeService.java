package cz.hocuspocus.coffeeblog.models.services;

import cz.hocuspocus.coffeeblog.models.dto.RecipeDTO;

public interface RecipeService {

    void create(RecipeDTO recipe);

}
