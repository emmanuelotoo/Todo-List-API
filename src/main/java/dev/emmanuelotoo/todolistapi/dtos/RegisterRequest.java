package dev.emmanuelotoo.todolistapi.dtos;

public record RegisterRequest(
         String name,
         String email,
         String passwordHash
) {}
