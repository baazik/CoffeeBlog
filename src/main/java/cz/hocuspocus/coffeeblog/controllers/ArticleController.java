package cz.hocuspocus.coffeeblog.controllers;

import com.fasterxml.jackson.databind.DatabindContext;
import cz.hocuspocus.coffeeblog.data.entities.ArticleEntity;
import cz.hocuspocus.coffeeblog.data.entities.ArticleRatingEntity;
import cz.hocuspocus.coffeeblog.data.entities.CommentEntity;
import cz.hocuspocus.coffeeblog.data.entities.UserEntity;
import cz.hocuspocus.coffeeblog.data.lists.Articles;
import cz.hocuspocus.coffeeblog.data.repositories.CommentRepository;
import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import cz.hocuspocus.coffeeblog.models.dto.CommentDTO;
import cz.hocuspocus.coffeeblog.models.dto.mappers.ArticleMapper;
import cz.hocuspocus.coffeeblog.models.exceptions.ArticleNotFoundException;
import cz.hocuspocus.coffeeblog.models.services.ArticleService;
import cz.hocuspocus.coffeeblog.models.services.CommentService;
import cz.hocuspocus.coffeeblog.models.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentRepository commentRepository;


    private String addArticlesPaginationModel(int page, Page<ArticleEntity> paginated, Model model) {
        List<ArticleEntity> listArticles = paginated.getContent(); // vytvori seznam clanku na aktualni strance z objektu paginated
		/*
		ulozeni promennych do promennych pro html sablonu
		 */
        model.addAttribute("article", new ArticleEntity());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("listArticles", listArticles);
        return "pages/articles/index";

    }

    @GetMapping
    public String renderIndex(@RequestParam(defaultValue = "1") int page, Model model) {
        // Here we are returning an object of type 'Donates' rather than a collection of Donate
        // objects so it is simpler for Object-Xml mapping

        Articles articles = new Articles();
        Page<ArticleEntity> paginated = articleService.findPaginated(page); // vrati vysledek na jedne strance z db donates
        articles.getArticlesList().addAll(paginated.toList()); // vytvori arraylist, do ktereho se prida predchozi vysledek
        return addArticlesPaginationModel(page, paginated, model); // zavola se metoda viz nize, ktera prida data do modelu
    }

    /*
    Giving the list of articles to model for view
    @GetMapping
    public String renderIndex(Model model)
    {
        List<ArticleDTO> articles = articleService.getAll();
        model.addAttribute("articles",articles);
        return "pages/articles/index";
    }
     */

    /*
    Getting the create form for view
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("create")
    public String renderCreateForm(@ModelAttribute ArticleDTO article){
        return "pages/articles/create";
    }

    /*
    Post method for form for creating an article
     */
    @Secured("ROLE_ADMIN")
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
    Post method for creating a comment
     */
    @PostMapping("{articleId}/createComment")
    public String createComment(
            @PathVariable long articleId,
            @Valid @ModelAttribute("commentDTO") CommentDTO comment,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        // we ask if a user filled at least one field wrong - if he does, the form is shown again with error messages
        if (result.hasErrors()) {
            return renderDetail(articleId, model);
        }

        if (comment.getComment().isEmpty()){
            redirectAttributes.addFlashAttribute("error", "The comment cannot be empty.");
            return "redirect:/articles/{articleId}";
        }

        // if there are no errors in form fields, we save the comment to DB and redirect the user to articles
        commentService.create(comment, articleId);
        redirectAttributes.addFlashAttribute("success", "The comment was successfully created.");
        return "redirect:/articles/{articleId}";
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
        ArticleEntity articleEntity = articleService.getEntityById(articleId);
        articleService.visit(articleEntity);
        List<CommentDTO> comments = commentService.getByArticleId(articleId);
        model.addAttribute("comments",comments);
        model.addAttribute("article", article);
        model.addAttribute("commentDTO", new CommentDTO());
        model.addAttribute("articleId", articleId);

        return "pages/articles/detail";
    }

    /*
    Get method for edit of an article
     */
    @Secured("ROLE_ADMIN")
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
    @Secured("ROLE_ADMIN")
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
    @Secured("ROLE_ADMIN")
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

    /*
    Post method for "like"
     */
    @PostMapping("voteUp")
    public String voteUp(
            @RequestParam long articleId,
            RedirectAttributes redirectAttributes,
            Model model
    ){
        try {
            ArticleEntity article = articleService.getEntityById(articleId);
            UserEntity user = userService.getLoggedUserEntity();
            model.addAttribute("userId",user.getUserId());

            /*
            If user is not logged, he gets an error.
            If user is logged, but already voted for the article, also gets an error
            Else - the upvote will be done
             */
            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "The user is not authenticated.");
            } else if(articleService.hasUserRated(article,user)){
                redirectAttributes.addFlashAttribute("error", "The user has already voted for the article.");
            } else {
                articleService.upVote(article,user);
                redirectAttributes.addFlashAttribute("success", "The voting was successful.");
                return "redirect:/articles/" + articleId;
            }
            return "redirect:/articles/" + articleId;
        } catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error while voting up.");
            return "redirect:/articles/" + articleId;
        }
    }

    /*
    Post method for "dislike"
     */
    @PostMapping("voteDown")
    public String voteDown(
            @RequestParam long articleId,
            RedirectAttributes redirectAttributes,
            Model model
    ){
        try {
            ArticleEntity article = articleService.getEntityById(articleId);
            UserEntity user = userService.getLoggedUserEntity();
            model.addAttribute("userId",user.getUserId());

            /*
            If user is not logged, he gets an error.
            If user is logged, but already voted for the article, also gets an error
            Else - the downvote will be done
             */
            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "The user is not authenticated.");
            } else if(articleService.hasUserRated(article,user)){
                redirectAttributes.addFlashAttribute("error", "The user has already voted for the article.");
            } else {
                articleService.downVote(article,user);
                redirectAttributes.addFlashAttribute("success", "The voting was successful.");
                return "redirect:/articles/" + articleId;
            }
            return "redirect:/articles/" + articleId;
        } catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error while voting up.");
            return "redirect:/articles/" + articleId;
        }
    }

    @PostMapping("comment/{commentId}/voteUp")
    public String commentVoteUp(
            @PathVariable long commentId,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        try {
            CommentEntity comment = commentService.getCommentEntityById(commentId);
            UserEntity user = userService.getLoggedUserEntity();
            model.addAttribute("userId", user.getUserId());

            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "The user is not authenticated.");
            } else if(commentService.hasUserRated(comment, user)){
            } else {
                commentService.upVote(comment, user);
            }
            return "redirect:/articles/" + comment.getArticle().getArticleId() + "#comment-" + comment.getId();
        } catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error while voting.");
            return "redirect:/articles/";
        }
    }

    @PostMapping("comment/{commentId}/voteDown")
    public String commentVoteDown(
            @PathVariable long commentId,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        try {
            CommentEntity comment = commentService.getCommentEntityById(commentId);
            UserEntity user = userService.getLoggedUserEntity();
            model.addAttribute("userId", user.getUserId());

            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "The user is not authenticated.");
            } else if(commentService.hasUserRated(comment, user)){
            } else {
                commentService.downVote(comment, user);
            }
            return "redirect:/articles/" + comment.getArticle().getArticleId() + "#comment-" + comment.getId();
        } catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error while voting.");
            return "redirect:/articles/";
        }
    }

}
