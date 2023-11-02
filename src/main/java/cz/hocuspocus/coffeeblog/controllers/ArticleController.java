package cz.hocuspocus.coffeeblog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @GetMapping
    public String renderIndex(){
        return "pages/articles/index";
    }

}
