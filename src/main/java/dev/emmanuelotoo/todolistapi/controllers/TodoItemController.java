package dev.emmanuelotoo.todolistapi.controllers;

import dev.emmanuelotoo.todolistapi.dtos.TodoItemDto;
import dev.emmanuelotoo.todolistapi.entities.TodoItem;
import dev.emmanuelotoo.todolistapi.entities.User;
import dev.emmanuelotoo.todolistapi.services.TodoItemService;
import dev.emmanuelotoo.todolistapi.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
public class TodoItemController {
    private final TodoItemService todoItemService;
    private final UserService userService;

    public TodoItemController(TodoItemService todoItemService, UserService userService) {
        this.todoItemService = todoItemService;
        this.userService = userService;
    }

    private User extractUserFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return token != null ? userService.getUserByToken(token).orElse(null) : null;
    }

    @PostMapping
    public ResponseEntity<?> createTodoItem(@RequestBody TodoItemDto todoItemDto, HttpServletRequest request) {
        User user = extractUserFromRequest(request);
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        return todoItemService.saveTodoItem(todoItemDto, user.getId());
    }

    @GetMapping
    public ResponseEntity<?> getAllTodoItems(HttpServletRequest request) {
        User user = extractUserFromRequest(request);
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired token"));
        }
        List<TodoItem> items = todoItemService.getAllTodoItemsByUserId(user.getId());
        return ResponseEntity.ok(items);
    }


    @PutMapping("/{todoId}")
    public ResponseEntity<?> updateTodoItem(@PathVariable Long todoId, @RequestBody TodoItemDto todoItemDto, HttpServletRequest request) {
        User user = extractUserFromRequest(request);
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired token"));
        }
        TodoItem updated = todoItemService.updateTodoItem(todoId, todoItemDto, user.getId());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<?> deleteTodoItem(@PathVariable Long todoId, HttpServletRequest request) {
        User user = extractUserFromRequest(request);
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired token"));
        }
        todoItemService.deleteTodoItem(todoId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
