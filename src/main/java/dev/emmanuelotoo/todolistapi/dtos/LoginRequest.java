package dev.emmanuelotoo.todolistapi.dtos;

public record LoginRequest(
        String email,
        String passwordHash
) {}
