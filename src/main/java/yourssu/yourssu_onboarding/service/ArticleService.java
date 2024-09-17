package yourssu.yourssu_onboarding.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yourssu.yourssu_onboarding.DTO.ArticleDTO;
import yourssu.yourssu_onboarding.entity.Article;
import yourssu.yourssu_onboarding.entity.User;
import yourssu.yourssu_onboarding.repository.ArticleRepository;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article findById(Long id) {
        Optional<Article> findArticle = articleRepository.findById(id);
        if(findArticle.isPresent()) {
            return findArticle.get();
        }
        else{
            throw new EntityNotFoundException("Article with id " + id + " not found");
        }
    }

    public void validateOwner(Long articleId, User user) {
        Article article = findById(articleId);
        if(!Objects.equals(article.getUser().getUser_id(), user.getUser_id()))
            throw new BadCredentialsException("Unauthorized to access this resource");
    }

    @Transactional
    public Article newArticle(ArticleDTO articleDTO, User user) {
        articleDTO.validateData();

        LocalDateTime now = LocalDateTime.now();

        Article article = Article.builder()
                .title(articleDTO.getTitle())
                .content(articleDTO.getContent())
                .created_at(now)
                .updated_at(now)
                .user(user)
                .build();
        return articleRepository.save(article);
    }

    @Transactional
    public Article updateArticle(ArticleDTO articleDTO, Long articleId) {
        articleDTO.validateData();
        LocalDateTime now = LocalDateTime.now();

        Article article = findById(articleId);
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setUpdated_at(now);
        return articleRepository.save(article);
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    @Transactional
    public void deleteAllByUser(User user) {
        articleRepository.deleteByUser(user);
    }
}
