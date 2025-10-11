package dev.emmanuelotoo.todolistapi.controllers;

import dev.emmanuelotoo.todolistapi.dtos.LoginRequest;
import dev.emmanuelotoo.todolistapi.dtos.RegisterRequest;
import dev.emmanuelotoo.todolistapi.entities.User;
import dev.emmanuelotoo.todolistapi.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Register user
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegisterRequest registerRequest) {
        User savedUser = userService.registerUser(registerRequest);
        return ResponseEntity.ok(Map.of(
                "token", savedUser.getToken(),
                "refreshToken", savedUser.getRefreshToken()
        ));
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest.email(), loginRequest.passwordHash())
                .flatMap(token -> userService.getUserByToken(token)
                        .map(user -> ResponseEntity.ok(Map.of(
                                "token", token,
                                "refreshToken", user.getRefreshToken()
                        ))))
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Invalid credentials")));
    }



    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        Optional<String> newAccessToken = userService.refreshAccessToken(refreshToken);

        if (newAccessToken.isPresent()) {
            return ResponseEntity.ok(Map.of("token", newAccessToken.get()));
        }
        return ResponseEntity.status(401)
                .body(Map.of("error", "Invalid or expired refresh token"));
    }

}
