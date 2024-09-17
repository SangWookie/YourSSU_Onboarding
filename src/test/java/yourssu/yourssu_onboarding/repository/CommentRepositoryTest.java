package yourssu.yourssu_onboarding.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import yourssu.yourssu_onboarding.entity.Article;
import yourssu.yourssu_onboarding.entity.Comment;
import yourssu.yourssu_onboarding.entity.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    private User savedUser;
    private Article savedArticle;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void setup(){
        User user = User.builder()
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .email("sangwook@gmail.com")
                .password("password123")
                .username("sangwook")
                .build();
        savedUser = userRepository.save(user);

        Article article = Article.builder()
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .user(savedUser)
                .title("hello title")
                .content("hello world!")
                .build();
        savedArticle = articleRepository.save(article);
    }
    @AfterEach
    void clean(){
        userRepository.delete(savedUser);
        articleRepository.delete(savedArticle);
    }

    @Test
    @DisplayName("save comment")
    void saveComment() {
        // given
        Comment comment = Comment.builder()
                .content("content")
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .user(savedUser)
                .article(savedArticle)
                .build();
        // when
        Comment savedComment = commentRepository.save(comment);

        // then
        assertEquals(savedUser.getEmail(), savedComment.getUser().getEmail());
        assertEquals(savedArticle.getTitle(), savedComment.getArticle().getTitle());
        assertEquals("content", savedComment.getContent());
    }

}