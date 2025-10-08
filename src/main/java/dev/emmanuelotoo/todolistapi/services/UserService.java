package dev.emmanuelotoo.todolistapi.services;

import dev.emmanuelotoo.todolistapi.dtos.RegisterRequest;
import dev.emmanuelotoo.todolistapi.entities.User;
import dev.emmanuelotoo.todolistapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Register user
    public User registerUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setName(registerRequest.name());
        user.setEmail(registerRequest.email());

        // Encode password with Base64
        String encodedPassword = Base64.getEncoder().encodeToString(registerRequest.password().getBytes(StandardCharsets.UTF_8));
        user.setPasswordHash(encodedPassword);

        return userRepository.save(user);
    }

    // Login user
    public boolean login(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Encode the raw password and compare with the stored password
            String encodedPassword = Base64.getEncoder().encodeToString(rawPassword.getBytes(StandardCharsets.UTF_8));
            return user.getPasswordHash().equals(encodedPassword);
        }
        return false;
    }
}
