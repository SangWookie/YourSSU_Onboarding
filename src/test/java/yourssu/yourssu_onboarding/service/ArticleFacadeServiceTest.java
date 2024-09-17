package yourssu.yourssu_onboarding.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;
import yourssu.yourssu_onboarding.DTO.ArticleDTO;
import yourssu.yourssu_onboarding.DTO.ArticleReturnDTO;
import yourssu.yourssu_onboarding.DTO.Credential;
import yourssu.yourssu_onboarding.entity.Article;
import yourssu.yourssu_onboarding.entity.Comment;
import yourssu.yourssu_onboarding.entity.User;
import yourssu.yourssu_onboarding.repository.ArticleRepository;
import yourssu.yourssu_onboarding.repository.CommentRepository;
import yourssu.yourssu_onboarding.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleFacadeServiceTest {
    @Autowired
    private ArticleFacadeService articleFacadeService;
    @Autowired
    private UserRepository userRepository;

    private User savedUser;
    private Article savedArticle;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    @Transactional
    @DisplayName("fix article with correct credential")
    void fixArticleWithCorrectCredential() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .created_at(now)
                .updated_at(now)
                .email("sangwook@gmail.com")
                .password("$2a$10$UoZLU11psuxcu.y9Nk9syuShh2p6Cg9/ojvSKzTqAj0uJ0nelhtc2") // password123
                .username("sangwook")
                .build();
        savedUser = userRepository.save(user);

        Article article1 = Article.builder()
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .title("title")
                .content("content")
                .user(savedUser)
                .build();
        savedArticle = articleRepository.save(article1);

        // when
        ArticleDTO articleDTO = ArticleDTO.builder()
                .content("new content")
                .title("new title")
                .email("sangwook@gmail.com")
                .password("password123")
                .build();

        // then
        ArticleReturnDTO articleReturnDTO = articleFacadeService.updateArticle(articleDTO, savedArticle.getArticleId());
        assertEquals(articleReturnDTO.getTitle(), articleDTO.getTitle());
    }

    @Test
    @Transactional
    @DisplayName("fix article from other unauthorized user")
    void fixArticleFromOtherUnauthorizedUser() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .created_at(now)
                .updated_at(now)
                .email("sangwook@gmail.com")
                .password("$2a$10$UoZLU11psuxcu.y9Nk9syuShh2p6Cg9/ojvSKzTqAj0uJ0nelhtc2") // password123
                .username("sangwook")
                .build();
        savedUser = userRepository.save(user);

        Article article1 = Article.builder()
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .title("title")
                .content("content")
                .user(savedUser)
                .build();
        savedArticle = articleRepository.save(article1);

        User anotherUser = User.builder()
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .email("random@email.com")
                .password("password123") // password123
                .username("anotherUser")
                .build();
        userRepository.save(anotherUser);

        // when
        ArticleDTO articleDTO = ArticleDTO.builder()
                .content("new content")
                .title("new title")
                .email("random@email.com")
                .password("$2a$10$UoZLU11psuxcu.y9Nk9syuShh2p6Cg9/ojvSKzTqAj0uJ0nelhtc2") // password123
                .build();

        // then
        assertThrows(BadCredentialsException.class, () -> {
            articleFacadeService.updateArticle(articleDTO, savedArticle.getArticleId());
        });
    }

    @Test
    @Transactional
    @DisplayName("fix article with incorrect title/content")
    void fixArticleWrongCred() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .created_at(now)
                .updated_at(now)
                .email("sangwook@gmail.com")
                .password("$2a$10$UoZLU11psuxcu.y9Nk9syuShh2p6Cg9/ojvSKzTqAj0uJ0nelhtc2") // password123
                .username("sangwook")
                .build();
        savedUser = userRepository.save(user);

        Article article1 = Article.builder()
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .title("title")
                .content("content")
                .user(savedUser)
                .build();
        savedArticle = articleRepository.save(article1);

        // when
        ArticleDTO articleDTO = ArticleDTO.builder()
                .content("")
                .title(" ")
                .email("sangwook@gmail.com")
                .password("password123") // password123
                .build();

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            articleFacadeService.updateArticle(articleDTO, savedArticle.getArticleId());
        });
    }

    @Test
    @Transactional
    @DisplayName("Delete article from user who is not the owner")
    void deleteArticleFromUserWhoIsNotTheOwner() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .created_at(now)
                .updated_at(now)
                .email("sangwook@gmail.com")
                .password("$2a$10$UoZLU11psuxcu.y9Nk9syuShh2p6Cg9/ojvSKzTqAj0uJ0nelhtc2") // password123
                .username("sangwook")
                .build();
        savedUser = userRepository.save(user);

        Article article1 = Article.builder()
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .title("title")
                .content("content")
                .user(savedUser)
                .build();
        savedArticle = articleRepository.save(article1);

        User anotherUser = User.builder()
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .email("random@email.com")
                .password("password123") // password123
                .username("anotherUser")
                .build();
        userRepository.save(anotherUser);

        // when
        Credential credential = Credential.builder()
                .email("random@email.com")
                .password("password123")
                .build();

        // then
        assertThrows(BadCredentialsException.class, () -> {
            articleFacadeService.deleteArticle(credential, savedArticle.getArticleId());
        });
    }

    @Test
    @DisplayName("All comments deleted when article is deleted")
    void deleteAllCommentsWhenArticleIsDeleted() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .created_at(now)
                .updated_at(now)
                .email("sangwook@gmail.com")
                .password("$2a$10$UoZLU11psuxcu.y9Nk9syuShh2p6Cg9/ojvSKzTqAj0uJ0nelhtc2") // password123
                .username("sangwook")
                .build();
        savedUser = userRepository.save(user);

        Article article1 = Article.builder()
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .title("title")
                .content("content")
                .user(savedUser)
                .build();
        savedArticle = articleRepository.save(article1);

        Comment comment = Comment.builder()
                .article(savedArticle)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .user(savedUser)
                .content("comment comment")
                .build();
        Comment savedComment = commentRepository.save(comment);

        // when
        Credential credential = Credential.builder()
                .email("sangwook@gmail.com")
                .password("password123")
                .build();
        articleFacadeService.deleteArticle(credential, savedArticle.getArticleId());

        // then
        assertTrue(articleRepository.findByArticleId(savedArticle.getArticleId()).isEmpty());
        assertTrue(commentRepository.findAllByArticle(savedArticle).isEmpty());

        // delete test user
        userRepository.delete(savedUser);
    }
}