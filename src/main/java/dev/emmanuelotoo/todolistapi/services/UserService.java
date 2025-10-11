package dev.emmanuelotoo.todolistapi.services;

import dev.emmanuelotoo.todolistapi.dtos.RegisterRequest;
import dev.emmanuelotoo.todolistapi.entities.User;
import dev.emmanuelotoo.todolistapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Register user
    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        User user = new User();
        user.setName(registerRequest.name());
        user.setEmail(registerRequest.email());

        // Encode password with Base64
        String encodedPassword = Base64.getEncoder().encodeToString(registerRequest.passwordHash().getBytes(StandardCharsets.UTF_8));
        user.setPasswordHash(encodedPassword);

        // Generating a random token for the user with 5-minute expiration
        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiresAt(LocalDateTime.now().plusMinutes(5));

        // Generate refresh token valid for 7 days
        user.setRefreshToken(UUID.randomUUID().toString());
        user.setRefreshTokenExpiresAt(LocalDateTime.now().plusDays(7));

        return userRepository.save(user);
    }


    // Login user
    public Optional<String> login(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Encoding the raw password to compare with the stored hash; returns the token if auth is successful
            String encodedPassword = Base64.getEncoder().encodeToString(rawPassword.getBytes(StandardCharsets.UTF_8));
            if (user.getPasswordHash().equals(encodedPassword)) {

                // Generating a new access token on each login
                user.setToken(UUID.randomUUID().toString());
                user.setTokenExpiresAt(LocalDateTime.now().plusMinutes(5));

                // Refresh the refresh token expiration
                user.setRefreshToken(UUID.randomUUID().toString());
                user.setRefreshTokenExpiresAt(LocalDateTime.now().plusDays(7));

                userRepository.save(user);
                return Optional.of(user.getToken());
            }
        }
        return Optional.empty();
    }


    // validate if the token is valid and not expired
    public boolean isTokenValid(String token) {
        Optional<User> userOpt = userRepository.findByToken(token);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.getTokenExpiresAt() != null &&
                    LocalDateTime.now().isBefore(user.getTokenExpiresAt());
        }
        return false;
    }

    // Get user by token
    public Optional<User> getUserByToken(String token) {
        if (!isTokenValid(token)) {
            return Optional.empty();
        }
        return userRepository.findByToken(token);
    }

    public Optional<String> refreshAccessToken(String refreshToken) {
        Optional<User> userOpt = userRepository.findByRefreshToken(refreshToken);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Check if the refresh token is still valid
            if (user.getRefreshTokenExpiresAt() != null &&
                LocalDateTime.now().isBefore(user.getRefreshTokenExpiresAt())) {


                // Generate a new access token
                user.setToken(UUID.randomUUID().toString());
                user.setTokenExpiresAt(LocalDateTime.now().plusMinutes(5));
                userRepository.save(user);
                return Optional.of(user.getToken());
            }
        }
        return Optional.empty();
    }

}
