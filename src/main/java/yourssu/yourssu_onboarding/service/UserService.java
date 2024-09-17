package yourssu.yourssu_onboarding.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yourssu.yourssu_onboarding.DTO.Credential;
import yourssu.yourssu_onboarding.DTO.RegisterDTO;
import yourssu.yourssu_onboarding.DTO.UserDTO;
import yourssu.yourssu_onboarding.entity.User;
import yourssu.yourssu_onboarding.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public UserDTO register(RegisterDTO registerDTO) {
        LocalDateTime now = LocalDateTime.now();
        String encodedPassword = bCryptPasswordEncoder.encode(registerDTO.getPassword());

        // Email already exists!
        if (existsByEmail(registerDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Create User from DTO
        User user = User.builder()
                .username(registerDTO.getUsername())
                .password(encodedPassword)
                .email(registerDTO.getEmail())
                .created_at(now)
                .updated_at(now)
                .build();

        // Save using repository
        userRepository.save(user);

        return UserDTO.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }

    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        return user.get();
    }

    public boolean existsByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

    public boolean authenticate(String email, String password) {
        User user = findByEmail(email);

        // encode input password for matching
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }
        return true;
    }

    @Transactional
    public void deleteAccount(Credential credential) {
        // authenticate user
        authenticate(credential.getEmail(), credential.getPassword());

        User user = findByEmail(credential.getEmail());

        userRepository.delete(user);
    }
}
