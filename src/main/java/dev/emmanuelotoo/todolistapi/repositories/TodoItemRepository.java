package dev.emmanuelotoo.todolistapi.repositories;

import dev.emmanuelotoo.todolistapi.entities.TodoItem;
import dev.emmanuelotoo.todolistapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    List<TodoItem> findByUser(User user);
    Optional<TodoItem> findByIdAndUserId(Long id, Long userId);
}
