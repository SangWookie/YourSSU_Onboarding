package yourssu.yourssu_onboarding.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import yourssu.yourssu_onboarding.entity.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    @DisplayName("User save")
    void createUser() {
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
        User resultUser = userRepository.save(user);

        // then
        Assertions.assertEquals(resultUser.getEmail(), user.getEmail());
        Assertions.assertNotNull(resultUser.getUser_id());
    }

    @Test
    @Transactional
    @DisplayName("User find")
    void findUser() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .created_at(now)
                .updated_at(now)
                .email("sangwook@gmail.com")
                .password("password123")
                .username("sangwook")
                .build();
        User savedUser = userRepository.save(user);

        // when
        User resultUser = userRepository.findByEmail("sangwook@gmail.com").get();

        // then
        Assertions.assertEquals(resultUser.getEmail(), savedUser.getEmail());
    }

    @Test
    @Transactional
    @DisplayName("amend User")
    void amendUser() throws InterruptedException {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .created_at(now)
                .updated_at(now)
                .email("sangwook@gmail.com")
                .password("password123")
                .username("sangwook")
                .build();
        User savedUser = userRepository.save(user);

        // when
        User searchUser = userRepository.findByEmail("sangwook@gmail.com").get();
        searchUser.setUsername("sangwookie");
        Thread.sleep(500);
        searchUser.setUpdated_at(LocalDateTime.now());

        User editedUser = userRepository.save(searchUser);

        // then
        Assertions.assertEquals(editedUser.getUsername(), "sangwookie");
        Assertions.assertNotEquals(editedUser.getUpdated_at(), now);
    }

    @Test
    @Transactional
    @DisplayName("User delete by entity")
    void deleteUser() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .created_at(now)
                .updated_at(now)
                .email("sangwook@gmail.com")
                .password("password123")
                .username("sangwook")
                .build();
        User savedUser = userRepository.save(user);

        // when
        userRepository.delete(savedUser);

        // then
        boolean userExists = userRepository.findByEmail("sangwook@gmail.com").isPresent();
        Assertions.assertFalse(userExists, "The user should have been deleted.");
    }

    @Test
    @Transactional
    @DisplayName("User delete by Email")
    void deleteUserByEmail() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .created_at(now)
                .updated_at(now)
                .email("sangwook@gmail.com")
                .password("password123")
                .username("sangwook")
                .build();
        User savedUser = userRepository.save(user);

        // when
        userRepository.deleteByEmail("sangwook@gmail.com");

        // then
        Assertions.assertTrue(userRepository.findByEmail("sangwook@gmail.com").isEmpty(), "The user should have been deleted.");
    }
}