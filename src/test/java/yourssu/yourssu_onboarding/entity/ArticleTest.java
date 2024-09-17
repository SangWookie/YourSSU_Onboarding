package yourssu.yourssu_onboarding.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {
    @Test
    @DisplayName("Test creating instance Article with builder")
    void testCreateArticle() {
        // given
        Article article = Article.builder()
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .title("title")
                .content("content")
                .user(new User())
                .build();
        // when, then
        assertEquals("title", article.getTitle());
    }

}