package yourssu.yourssu_onboarding.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import yourssu.yourssu_onboarding.DTO.Credential;
import yourssu.yourssu_onboarding.entity.Article;
import yourssu.yourssu_onboarding.entity.Comment;
import yourssu.yourssu_onboarding.entity.User;
import yourssu.yourssu_onboarding.repository.ArticleRepository;
import yourssu.yourssu_onboarding.repository.CommentRepository;
import yourssu.yourssu_onboarding.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserIntegrateTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserService userService;

    @Test
//    @Transactional 실제로 커밋이 되지 않기 때문에 delete 후 조회를 하면 아직 데이터가 존재하는 것 처럼 인식
    @DisplayName("Delete everything when account is deleted")
    void deleteAccount() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .created_at(now)
                .updated_at(now)
                .email("sangwook@gmail.com")
                .password("$2a$10$UoZLU11psuxcu.y9Nk9syuShh2p6Cg9/ojvSKzTqAj0uJ0nelhtc2") // password123
                .username("sangwook")
                .build();
        User savedUser = userRepository.save(user);

        Article article = Article.builder()
                .created_at(now)
                .updated_at(now)
                .user(savedUser)
                .title("hello title")
                .content("hello world!")
                .build();
        Article savedArticle = articleRepository.save(article);

        Comment comment = Comment.builder()
                .created_at(now)
                .updated_at(now)
                .user(savedUser)
                .article(savedArticle)
                .content("hello content")
                .build();
        Comment savedComment = commentRepository.save(comment);

        // when
        assertTrue(userRepository.findByEmail("sangwook@gmail.com").isPresent());
        assertFalse(commentRepository.findAllByUser(savedUser).isEmpty());
        assertFalse(articleRepository.findAllByUser(savedUser).isEmpty());

        Credential credential = new Credential();
        credential.setEmail("sangwook@gmail.com");
        credential.setPassword("password123");
        userService.deleteAccount(credential);

        // then
        assertTrue(commentRepository.findAllByUser(savedUser).isEmpty());
        assertTrue(articleRepository.findAllByUser(savedUser).isEmpty());
        assertTrue(userRepository.findByEmail("sangwook@gmail.com").isEmpty());
    }
}
