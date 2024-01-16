package cz.hocuspocus.coffeeblog.controllers;

import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import cz.hocuspocus.coffeeblog.models.dto.RecipeDTO;
import cz.hocuspocus.coffeeblog.models.services.RecipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Secured("ROLE_ADMIN")
    @GetMapping("create")
    public String renderCreateForm(@ModelAttribute RecipeDTO recipe){
        return "pages/recipes/create";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("create")
    public String createRecipe(
            @Valid @ModelAttribute RecipeDTO recipe,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {

        if (result.hasErrors()){
            return renderCreateForm(recipe);
        }

        recipeService.create(recipe);
        redirectAttributes.addFlashAttribute("success", "Recept byl úspěšně vytvořen.");
        return "redirect:/recipes";
    }

}
