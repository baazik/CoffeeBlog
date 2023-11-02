package cz.hocuspocus.coffeeblog.controllers;

import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @GetMapping
    public String renderIndex(){
        return "pages/articles/index";
    }

    @GetMapping("create")
    public String renderCreateForm(@ModelAttribute ArticleDTO article){
        return "pages/articles/create";
    }

    @PostMapping("create")
    public String createArticle(
            @Valid @ModelAttribute ArticleDTO article,
            BindingResult result
    ) {
        // we ask, if an user filled at least one field wrong - if he does, the form is shown again with error messages
        if (result.hasErrors()){
            return renderCreateForm(article);
        }

        // Here will be a work with DB later on

        // if there are no errors in form field, we print title and description to the log and redirect the user to articles
        System.out.println(article.getTitle() + " - " + article.getDescription());

        return "redirect:/articles";

    }

}
