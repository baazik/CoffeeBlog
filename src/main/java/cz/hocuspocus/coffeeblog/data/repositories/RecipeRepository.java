package cz.hocuspocus.coffeeblog.data.repositories;

import cz.hocuspocus.coffeeblog.data.entities.RecipeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface RecipeRepository extends CrudRepository<RecipeEntity, Long> {

//    List<RecipeEntity> findByTitleStartingWithOrderByTitleAsc(String title);

    //
    //@Query(value = "SELECT * FROM recipe_entity r WHERE LOWER(r.title) LIKE LOWER(CONCAT(:title, '%')) ORDER BY LOWER(r.title) COLLATE utf8mb4_unicode_ci ASC", nativeQuery = true)
   // List<RecipeEntity> findByTitleStartingWithOrderByTitleAsc(@Param("title") String title);

    @Query(value = "SELECT * FROM recipe_entity r WHERE LOWER(r.title) LIKE LOWER(CONCAT(:title, '%')) ORDER BY r.title ASC", nativeQuery = true)
    List<RecipeEntity> findByTitleStartingWithOrderByTitleAsc(@Param("title") String title);

}
