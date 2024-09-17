package yourssu.yourssu_onboarding.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import yourssu.yourssu_onboarding.DTO.ArticleDTO;
import yourssu.yourssu_onboarding.entity.User;
import yourssu.yourssu_onboarding.repository.ArticleRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @Mock
    private ArticleRepository articleRepository;
    @InjectMocks
    private ArticleService articleService;

    private User user;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        user = User.builder()
                .created_at(now)
                .updated_at(now)
                .email("sangwook@gmail.com")
                .password("password123")
                .username("sangwook")
                .build();
    }

    @Test
    @DisplayName("Save new article with improper title, content")
    void saveNewArticle() {
        // given
        ArticleDTO articleDTO = ArticleDTO.builder()
                .title("")
                .content("content")
                .email("sangwook@gmail.com")
                .password("password123")
                .build();

        ArticleDTO articleDTO2 = ArticleDTO.builder()
                .title("title")
                .email("sangwook@gmail.com")
                .password("password123")
                .build();

        ArticleDTO articleDTO3 = ArticleDTO.builder()
                .title("title")
                .content(" ")
                .email("sangwook@gmail.com")
                .password("password123")
                .build();

        // when, then
        assertThrows(IllegalArgumentException.class, () -> {
            articleService.newArticle(articleDTO, user);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            articleService.newArticle(articleDTO2, user);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            articleService.newArticle(articleDTO3, user);
        });
    }
}