package repository;

import com.example.crudProject.CrudProjectApplication;
import com.example.crudProject.entity.Article;
import com.example.crudProject.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ArticleRepositoryTest extends CrudProjectApplication {
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void create(){
        Article article = new Article();

        article.setTitle("title12");
        article.setContent("content12");

        Article article1 = articleRepository.save(article);
    }

}
