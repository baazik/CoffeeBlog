package cz.hocuspocus.coffeeblog.controllers;

import cz.hocuspocus.coffeeblog.data.entities.ArticleEntity;
import cz.hocuspocus.coffeeblog.data.entities.RecipeEntity;
import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import cz.hocuspocus.coffeeblog.models.dto.CommentDTO;
import cz.hocuspocus.coffeeblog.models.dto.RecipeDTO;
import cz.hocuspocus.coffeeblog.models.dto.mappers.RecipeMapper;
import cz.hocuspocus.coffeeblog.models.services.RecipeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeMapper recipeMapper;

    /*
    @GetMapping
    public String renderIndex(Model model) {
        List<RecipeDTO> recipes = recipeService.getAll();
        model.addAttribute("recipes", recipes);

        return "pages/recipes/index";
    }
     */

    @GetMapping
    public String getAllRecipes(Model model) {
        Map<Character, List<RecipeEntity>> recipesByAlphabet = recipeService.getAllRecipesByAlphabetSorted();
        model.addAttribute("recipesByAlphabet", recipesByAlphabet);
        return "pages/recipes/index";
    }

    @GetMapping("{recipeId}")
    public String renderDetail(
            @PathVariable long recipeId,
            Model model
    ) {
        RecipeDTO recipe = recipeService.getById(recipeId);
        model.addAttribute("recipe", recipe);
        model.addAttribute("recipeId", recipeId);

        return "pages/recipes/detail";
    }

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

    @Secured("ROLE_ADMIN")
    @GetMapping("edit/{recipeId}")
    public String renderEditForm(
            @PathVariable Long recipeId,
            RecipeDTO recipe
    ) {
        RecipeDTO fetchedRecipe = recipeService.getById(recipeId);
        recipeMapper.updateRecipeDTO(fetchedRecipe, recipe);

        return "pages/recipes/edit";
    }

    /*
    Post method for edit of an artcile
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("edit/{recipeId}")
    public String editRecipe(
            @PathVariable long recipeId,
            @Valid RecipeDTO recipe,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors())
            return renderEditForm(recipeId, recipe);

        recipe.setRecipeId(recipeId);
        recipeService.edit(recipe);
        redirectAttributes.addFlashAttribute("success","Recept byl úspěšně upraven.");

        return "redirect:/recipes";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("delete/{recipeId}")
    public String deleteRecipe(
            @PathVariable long recipeId,
            RedirectAttributes redirectAttributes
    ){
        recipeService.remove(recipeId);
        redirectAttributes.addFlashAttribute("success","Článek byl smazán.");

        return "redirect:/recipes";
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<String> handleImageUpload(@RequestParam("file") MultipartFile file) {
        try {
            // Zpracování nahrávání obrázku a uložení do fyzické složky
            String relativePath = "uploads/" + file.getOriginalFilename();
            Path path = Paths.get("src/main/resources/static", relativePath);

            // Vytvoření složky, pokud neexistuje
            Files.createDirectories(path.getParent());

            // Zápis souboru
            Files.write(path, file.getBytes());

            // Vráťte JSON s URL k nově nahrávanému obrázku
            String imageUrl = relativePath;
            String jsonResponse = "{ \"location\": \"" + imageUrl + "\" }";
            System.out.println("Obrázek nahrán.");
            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(jsonResponse);
        } catch (Exception e) {
            // Zalogování detailů výjimky
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Chyba při zpracování obrázku");
        }
    }



}
