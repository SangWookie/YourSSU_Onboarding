package yourssu.yourssu_onboarding.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import yourssu.yourssu_onboarding.DTO.Credential;
import yourssu.yourssu_onboarding.DTO.RegisterDTO;
import yourssu.yourssu_onboarding.entity.Article;
import yourssu.yourssu_onboarding.entity.Comment;
import yourssu.yourssu_onboarding.entity.User;
import yourssu.yourssu_onboarding.repository.ArticleRepository;
import yourssu.yourssu_onboarding.repository.CommentRepository;
import yourssu.yourssu_onboarding.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Register")
    void testRegister() {
        RegisterDTO user = RegisterDTO.builder()
                .email("sangwook@gmail.com")
                .password("password123")
                .username("sangwook")
                .build();

        RegisterDTO user2 = RegisterDTO.builder()
                .email("sangwook@gmail.com")
                .password("password123")
                .username("sangwook")
                .build();

        User savedUser = User.builder()
                .email("sangwook@gmail.com")
                .password("password123")
                .username("sangwook")
                .build();

        // when
        when(userRepository.findByEmail("sangwook@gmail.com"))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(savedUser));

        // then
        userService.register(user);
        assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user2);
        });
    }

    @Test
    @DisplayName("Find by Email")
    void testFindByEmail() {
        // given
        //when
        when(userRepository.findByEmail("sangwook@gmail.com")).thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> {
            userService.findByEmail("sangwook@gmail.com");
        });
    }

    @Test
    @DisplayName("Authenticate - Success")
    void authenticateSuccess() {
        // given
        String email = "sangwook@gmail.com";
        String rawPassword = "password123";
        String encodedPassword = "$2a$10$7QhxXYJqvDYPUNghqTg.WO/EcUzLv6mvgV9B8uBVOJlyRVqF3KoxW"; // bcrypt hashed password

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        // when
        boolean result = userService.authenticate(email, rawPassword);

        // then
        assertTrue(result);
        verify(userRepository, times(1)).findByEmail(email);
        verify(bCryptPasswordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }

    @Test
    @DisplayName("Authenticate - Wrong password")
    void authenticateWrongPassword() {
        // given
        String email = "sangwook@gmail.com";
        String rawPassword = "wrongPassword";
        String encodedPassword = "$2a$10$7QhxXYJqvDYPUNghqTg.WO/EcUzLv6mvgV9B8uBVOJlyRVqF3KoxW"; // bcrypt hashed password

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        // when, then
        Exception exception = assertThrows(BadCredentialsException.class, () -> {
            userService.authenticate(email, rawPassword);
        });

        assertEquals("Wrong password", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
        verify(bCryptPasswordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }
}