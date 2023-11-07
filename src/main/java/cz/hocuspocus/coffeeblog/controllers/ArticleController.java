package cz.hocuspocus.coffeeblog.controllers;

import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import cz.hocuspocus.coffeeblog.models.dto.mappers.ArticleMapper;
import cz.hocuspocus.coffeeblog.models.exceptions.ArticleNotFoundException;
import cz.hocuspocus.coffeeblog.models.services.ArticleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleMapper articleMapper;


    /*
    Giving the list of articles to model for view
     */
    @GetMapping
    public String renderIndex(Model model)
    {
        List<ArticleDTO> articles = articleService.getAll();
        model.addAttribute("articles",articles);
        return "pages/articles/index";
    }

    /*
    Getting the create form for view
     */
    @GetMapping("create")
    public String renderCreateForm(@ModelAttribute ArticleDTO article){
        return "pages/articles/create";
    }

    /*
    Post method for form for creating an article
     */
    @PostMapping("create")
    public String createArticle(
            @Valid @ModelAttribute ArticleDTO article,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        // we ask, if an user filled at least one field wrong - if he does, the form is shown again with error messages
        if (result.hasErrors()){
            return renderCreateForm(article);
        }

        // if there are no errors in form field, we save article to DB and redirect the user to articles
        articleService.create(article);
        redirectAttributes.addFlashAttribute("success", "The article was successfully created.");

        return "redirect:/articles";

    }

    /*
    Get method for detail of an article
     */
    @GetMapping("{articleId}")
    public String renderDetail(
            @PathVariable long articleId,
            Model model
    ) {
        ArticleDTO article = articleService.getById(articleId);
        model.addAttribute("article", article);

        return "pages/articles/detail";
    }

    /*
    Get method for edit of an article
     */
    @GetMapping("edit/{articleId}")
    public String renderEditForm(
            @PathVariable Long articleId,
            ArticleDTO article
    ) {
        ArticleDTO fetchedArticle = articleService.getById(articleId);
        articleMapper.updateArticleDTO(fetchedArticle, article);

        return "pages/articles/edit";
    }

    /*
    Post method for edit of an artcile
     */
    @PostMapping("edit/{articleId}")
    public String editArticle(
            @PathVariable long articleId,
            @Valid ArticleDTO article,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors())
            return renderEditForm(articleId, article);

        article.setArticleId(articleId);
        articleService.edit(article);
        redirectAttributes.addFlashAttribute("success","The article was successfully edited.");

        return "redirect:/articles";
    }

    /*
    Get method for delete of an article
     */
    @GetMapping("delete/{articleId}")
    public String deleteArticle(
            @PathVariable long articleId,
            RedirectAttributes redirectAttributes
    ){
        articleService.remove(articleId);
        redirectAttributes.addFlashAttribute("success","The article was deleted.");

        return "redirect:/articles";
    }

    /*
    Method for throwing exception, when article is not found
     */
    @ExceptionHandler({ArticleNotFoundException.class})
    public String handleArticleNotFoundException(
            RedirectAttributes redirectAttributes
    ){
        redirectAttributes.addFlashAttribute("error","The article was not found.");
        return "redirect:/articles";
    }

}
