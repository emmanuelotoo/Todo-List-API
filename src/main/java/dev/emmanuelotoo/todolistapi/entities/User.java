package dev.emmanuelotoo.todolistapi.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    //my email
    //password

    //Register
    //insert raw email
    //encode password using bse64

    //Login
    //email
    //Take raw password and encode using base64
    //compare with passwordHash


    @Column(unique = true)
    private String token;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodoItem> todoItems;
}
