package cz.hocuspocus.coffeeblog.data.lists;

import cz.hocuspocus.coffeeblog.data.entities.ArticleEntity;

import java.util.ArrayList;
import java.util.List;

public class Articles {

    private List<ArticleEntity> articles;

    public List<ArticleEntity> getArticlesList(){
        if (articles == null){
            articles = new ArrayList<>();
        }
        return articles;
    }

}
