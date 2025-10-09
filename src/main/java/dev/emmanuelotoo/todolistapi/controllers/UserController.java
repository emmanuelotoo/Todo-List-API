package dev.emmanuelotoo.todolistapi.controllers;

import dev.emmanuelotoo.todolistapi.dtos.LoginRequest;
import dev.emmanuelotoo.todolistapi.dtos.RegisterRequest;
import dev.emmanuelotoo.todolistapi.entities.User;
import dev.emmanuelotoo.todolistapi.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
        return ResponseEntity.ok(Map.of("token", savedUser.getToken()));
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest.email(), loginRequest.passwordHash())
                .map(token -> ResponseEntity.ok(Map.of("token", token)))
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Invalid credentials")));
    }


}
