package yourssu.yourssu_onboarding.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {
    @Test
    @DisplayName("Comment create with builder")
    void testCreateComment() {
        // given
        Comment comment = Comment.builder()
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .content("content")
                .user(new User())
                .article(new Article())
                .build();

        // when, then
        assertEquals("content", comment.getContent());
    }
}