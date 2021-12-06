package com.example.crudProject.controller;

import com.example.crudProject.dto.ArticleForm;
import com.example.crudProject.entity.Article;
import com.example.crudProject.repository.ArticleRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
public class MainController {
    @Autowired
    private ArticleRepository articleRepository;


    @GetMapping("/articles")
    public String List(Model model){

        List<Article> articleList = articleRepository.findAll();
        model.addAttribute("articleList", articleList);
        log.info(String.valueOf(articleList));
        return "main";
    }

    @GetMapping("/write")
    public String write(){
        return "articles/write";
    }

    @PostMapping("/articles/create")
    public String create(ArticleForm form){
        // DTO를 Entity로 변환
        Article article = form.toEntity();
        // Repository에게 Entity를 DB에 저장한다.
        Article saved = articleRepository.save(article);
        // 성공적으로 저장한다음, 다시 재요청으로 페이지를 리다이렉트로 상세페이지 호출
        return "redirect:/articles/" + saved.getId();
    }
    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model){
        log.info("id : "+ id);

        //1. id로 데이터를 가져옴
        // Optional<Article> articleEntity = articleRepository.findById(id); 자바8버전부터 제네릭코드를 이용해서 적용가능하나 일단 지양.
        Article articleEntity = articleRepository.findById(id).orElse(null); //orElse(null) = 값을 찾을 때 만약 값이 없다면 null을 반환해라라는 뜻.

        //2. 가져온 데이터를 모델에 등록
        model.addAttribute("article", articleEntity);

        //3. 보여줄 페이지를 설정
        return "articles/show";
    }
    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        //PathVariable = 매핑주소에 있던 변수값을 가져온다는 뜻

        //수정 할 데이터를 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);

        //모델 등록
        model.addAttribute("article", articleEntity);

        //뷰 페이지 설정
        return "articles/edit";
    }

    @PostMapping("/articles/update")
    public String update(ArticleForm form){
        //1. DTO를 엔티티로 변환한다.
        Article articleEntity = form.toEntity();

        //2. 엔티티를 DB로 변환한다.
        //2-1. 기존의 DB를 가져오기
        // Optional<Article> target = articleRepository.findById(articleEntity.getId()); 이렇게 해도됨
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
        //2-2. 기존의 DB가 있다면? 값을 갱신한다. 새로저장.
        if(target != null){
            articleRepository.save(articleEntity);
        }
        log.info(articleEntity.toString());
        //3. 수정결과 페이지로 리다이렉트를 한다.
        return "redirect:/articles/" + articleEntity.getId();
    }

    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr){
        log.info("삭제요청이 들어왔습니다. ");

        //1. 삭제 대상을 가져온다.
        Article target = articleRepository.findById(id).orElse(null);
        log.info(target.toString());

        //2. 대상을 삭제한다.
        if(target != null){
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제가 완료되었습니다!");
        }

        //3. 결과 페이지로 리다이렉트 한다.
        return "redirect:/articles";
    }
}
