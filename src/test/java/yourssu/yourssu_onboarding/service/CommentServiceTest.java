package yourssu.yourssu_onboarding.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;
import yourssu.yourssu_onboarding.DTO.CommentDTO;
import yourssu.yourssu_onboarding.DTO.CommentReturnDTO;
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
class CommentServiceTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;

    private User savedUser;
    private User savedUser2;
    private Article savedArticle;

    @InjectMocks
    private CommentService commentService;
    @Autowired
    private CommentFacadeService commentFacadeService;

    @BeforeEach
    void setup(){
        User user = User.builder()
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .email("sangwook@gmail.com")
                .password("$2a$10$UoZLU11psuxcu.y9Nk9syuShh2p6Cg9/ojvSKzTqAj0uJ0nelhtc2") // password123
                .username("sangwook")
                .build();
        savedUser = userRepository.save(user);

        User user2 = User.builder()
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .email("asdf@gmail.com")
                .password("$2a$10$UoZLU11psuxcu.y9Nk9syuShh2p6Cg9/ojvSKzTqAj0uJ0nelhtc2") // password123
                .username("asdf")
                .build();
        savedUser2 = userRepository.save(user2);

        Article article = Article.builder()
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .user(user)
                .title("hello title")
                .content("hello world!")
                .build();
        savedArticle = articleRepository.save(article);
    }

    @AfterEach
    void clean(){
        userRepository.delete(savedUser);
        userRepository.delete(savedUser2);
        articleRepository.delete(savedArticle);
    }

    @Test
    @Transactional
    @DisplayName("new comment")
    void testCreateComment() {
        // given
        CommentDTO comment = CommentDTO.builder()
                .content("content")
                .email("sangwook@gmail.com")
                .password("password123")
                .build();

        // when
        CommentReturnDTO commentReturnDTO = commentFacadeService.newComment(comment, savedArticle.getArticleId());

        // then
        assertEquals(commentReturnDTO.getContent(), comment.getContent());
    }

    @Test
    @Transactional
    @DisplayName("fix comment with improper fields")
    void testFixComment() {
        // given
        Comment comment = Comment.builder()
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .user(savedUser)
                .article(savedArticle)
                .content("hello comment")
                .build();
        Comment savedComment = commentRepository.save(comment);

        CommentDTO commentDTO = CommentDTO.builder()
                .content(null)
                .email("asdf@gmail.com")
                .password("password123")
                .build();

        // when, then
        assertThrows(IllegalArgumentException.class, ()->{
            commentFacadeService.fixComment(commentDTO, savedComment.getComment_id());
        });
    }

    @Test
    @DisplayName("delete comment with wrong ownership")
    void testDeleteCommentWithWrongOwnership() {
        // given
        Comment comment = Comment.builder()
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .user(savedUser)
                .article(savedArticle)
                .content("hello comment")
                .build();
        Comment savedComment = commentRepository.save(comment);

        Credential credential = Credential.builder()
                .email("asdf@gmail.com")
                .password("password123")
                .build();

        // when, then
        assertThrows(BadCredentialsException.class, ()->{
            commentFacadeService.deleteComment(credential, savedComment.getComment_id());
        });
    }
}