package dev.emmanuelotoo.todolistapi.services;

import dev.emmanuelotoo.todolistapi.dtos.TodoItemDto;
import dev.emmanuelotoo.todolistapi.entities.TodoItem;
import dev.emmanuelotoo.todolistapi.entities.User;
import dev.emmanuelotoo.todolistapi.repositories.TodoItemRepository;
import dev.emmanuelotoo.todolistapi.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoItemService {
    private final TodoItemRepository todoItemRepository;
    private final UserRepository userRepository;

    public TodoItemService(TodoItemRepository todoItemRepository, UserRepository userRepository) {
        this.todoItemRepository = todoItemRepository;
        this.userRepository = userRepository;
    }

    // Creating a To-Do item for a specific user
    public ResponseEntity<TodoItem> saveTodoItem(TodoItemDto todoItemDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

        TodoItem todoItem = new TodoItem();
        todoItem.setTitle(todoItemDto.title());
        todoItem.setDescription(todoItemDto.description());
        todoItem.setUser(user);

        TodoItem savedTodoItem = todoItemRepository.save(todoItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTodoItem);
    }

    // Getting all To-Do items for a specific user
    public List<TodoItem> getAllTodoItemsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));
        return todoItemRepository.findByUser(user);
    }

    // Getting a To-Do item by its id for a specific user
    public Optional<TodoItem> getTodoItemByIdAndUserId(Long todoId, Long userId) {
        return todoItemRepository.findByIdAndUserId(todoId, userId);
    }

    // Deleting a To-Do item for a specific user
    public void deleteTodoItem(Long todoId, Long userId) {
        TodoItem todoItem = todoItemRepository.findByIdAndUserId(todoId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Todo item with id " + todoId + " not found for user " + userId));
        todoItemRepository.delete(todoItem);
    }

    // Updating a To-Do item for a specific user
    public TodoItem updateTodoItem(Long todoId, TodoItemDto todoItemDto, Long userId) {
        TodoItem todoItem = todoItemRepository.findByIdAndUserId(todoId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Todo item with id " + todoId + " not found for user " + userId));

        String title = todoItemDto.title().trim().isEmpty() ? todoItem.getTitle() : todoItemDto.title();
        todoItem.setTitle(title);

        String description = todoItemDto.description().trim().isEmpty() ? todoItem.getDescription() : todoItemDto.description();
        todoItem.setDescription(description);

        return todoItemRepository.save(todoItem);
    }
}
