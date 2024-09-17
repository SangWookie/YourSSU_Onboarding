package yourssu.yourssu_onboarding.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class UserTest {
    @Test
    @DisplayName("사용자가 생성되는지 테스트")
    void createUser(){
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .created_at(now)
                .updated_at(now)
                .email("sangwook@gmail.com")
                .password("password123")
                .username("sangwook")
                .build();

        // when
        // then
        Assertions.assertEquals(now, user.getCreated_at());
        Assertions.assertEquals("password123", user.getPassword());
    }
}