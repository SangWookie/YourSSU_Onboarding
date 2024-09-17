package yourssu.yourssu_onboarding.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yourssu.yourssu_onboarding.DTO.ArticleDTO;
import yourssu.yourssu_onboarding.DTO.CommentDTO;
import yourssu.yourssu_onboarding.DTO.Credential;
import yourssu.yourssu_onboarding.DTO.RegisterDTO;
import yourssu.yourssu_onboarding.entity.Comment;
import yourssu.yourssu_onboarding.service.*;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    private final UserService userService;
    private final ArticleService articleService;
    private final ArticleFacadeService articleFacadeService;
    private final CommentFacadeService commentFacadeService;

    public RestController(UserService userService, ArticleService articleService, ArticleFacadeService articleFacadeService, CommentFacadeService commentFacadeService) {
        this.userService = userService;
        this.articleService = articleService;
        this.articleFacadeService = articleFacadeService;
        this.commentFacadeService = commentFacadeService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        return new ResponseEntity<>(userService.register(registerDTO), HttpStatus.OK);
    }

    @DeleteMapping("/account")
    public ResponseEntity<?> deleteAccount(@RequestBody Credential credential) {
        userService.deleteAccount(credential);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/article/new")
    public ResponseEntity<?> newArticle(@RequestBody ArticleDTO articleDTO) {
        return new ResponseEntity<>(articleFacadeService.newArticle(articleDTO), HttpStatus.OK);
    }

    @PutMapping("/article/{id}")
    public ResponseEntity<?> fixArticle(@PathVariable("id") Long id, @RequestBody ArticleDTO articleDTO) {
        return new ResponseEntity<>(articleFacadeService.updateArticle(articleDTO, id), HttpStatus.OK);
    }

    @DeleteMapping("/article/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable("id") Long id, @RequestBody Credential credential) {
        articleFacadeService.deleteArticle(credential, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/article/{id}/comment")
    public ResponseEntity<?> comment(@PathVariable("id") Long id, @RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentFacadeService.newComment(commentDTO, id), HttpStatus.OK);
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<?> fixComment(@PathVariable("id") Long id, @RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentFacadeService.fixComment(commentDTO, id), HttpStatus.OK);
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id, @RequestBody Credential credential) {
        commentFacadeService.deleteComment(credential, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
