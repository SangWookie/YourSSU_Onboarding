package yourssu.yourssu_onboarding.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yourssu.yourssu_onboarding.DTO.ArticleDTO;
import yourssu.yourssu_onboarding.DTO.ArticleReturnDTO;
import yourssu.yourssu_onboarding.DTO.Credential;
import yourssu.yourssu_onboarding.entity.Article;
import yourssu.yourssu_onboarding.entity.User;
import yourssu.yourssu_onboarding.repository.CommentRepository;

@Service
public class ArticleFacadeService {
    private final ArticleService articleService;
    private final UserService userService;
    private final CommentRepository commentRepository;

    public ArticleFacadeService(ArticleService articleService, UserService userService, CommentRepository commentRepository) {
        this.articleService = articleService;
        this.userService = userService;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public ArticleReturnDTO newArticle(ArticleDTO articleDTO) {
        // authorization
        userService.authenticate(articleDTO.getEmail(), articleDTO.getPassword());

        User user = userService.findByEmail(articleDTO.getEmail());
        Article article = articleService.newArticle(articleDTO, user);

        return ArticleReturnDTO.builder()
                .title(article.getTitle())
                .content(article.getContent())
                .articleId(article.getArticleId())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public ArticleReturnDTO updateArticle(ArticleDTO articleDTO, Long articleId) {
        //authorization
        userService.authenticate(articleDTO.getEmail(), articleDTO.getPassword());

        // validate owner of the article
        User user = userService.findByEmail(articleDTO.getEmail());
        articleService.validateOwner(articleId, user);

        Article article = articleService.updateArticle(articleDTO, articleId);

        return ArticleReturnDTO.builder()
                .title(article.getTitle())
                .content(article.getContent())
                .articleId(article.getArticleId())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public void deleteArticle(Credential credential, Long articleId) {
        //authorization
        userService.authenticate(credential.getEmail(), credential.getPassword());

        // validate owner of the article
        User user = userService.findByEmail(credential.getEmail());
        articleService.validateOwner(articleId, user);

        articleService.deleteArticle(articleId);
    }
}
