package dev.emmanuelotoo.todolistapi.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class TodoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
}