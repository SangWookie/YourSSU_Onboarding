package yourssu.yourssu_onboarding.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import yourssu.yourssu_onboarding.entity.Article;
import yourssu.yourssu_onboarding.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {

    }

    @Transactional
    @Test
    @DisplayName("save Article")
    void save() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .created_at(now)
                .updated_at(now)
                .email("sangwook@gmail.com")
                .password("password123")
                .username("sangwook")
                .build();
        savedUser = userRepository.save(user);

        Article article = Article.builder()
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .title("title")
                .content("content")
                .user(savedUser)
                .build();

        // when
        Article savedArticle = articleRepository.save(article);

        // then
        assertEquals(savedArticle.getTitle(), "title");
        assertEquals(savedArticle.getUser().getUser_id(), savedUser.getUser_id());
    }

    @Transactional
    @Test
    @DisplayName("findAllByUser test")
    void findAllByUser() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .created_at(now)
                .updated_at(now)
                .email("sangwook@gmail.com")
                .password("password123")
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

        Article article2 = Article.builder()
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .title("title second")
                .content("content second")
                .user(savedUser)
                .build();

        // when
        Article savedArticle1 = articleRepository.save(article1);
        Article savedArticle2 = articleRepository.save(article2);

        List<Article> articles = articleRepository.findAllByUser(savedUser);
        // then

        assertEquals(articles.size(), 2);
        assertEquals(articles.get(0).getTitle(), "title");
        assertEquals(articles.get(1).getTitle(), "title second");
    }

    @Test
    @Transactional
    @DisplayName("delete all by User test")
    void deleteAllByUser() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .created_at(now)
                .updated_at(now)
                .email("sangwook@gmail.com")
                .password("password123")
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

        Article article2 = Article.builder()
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .title("title second")
                .content("content second")
                .user(savedUser)
                .build();

        // when
        Article savedArticle1 = articleRepository.save(article1);
        Article savedArticle2 = articleRepository.save(article2);
        articleRepository.deleteByUser(savedUser);

        // then
        List<Article> articles = articleRepository.findAllByUser(savedUser);
        assertEquals(0, articles.size());
    }
}