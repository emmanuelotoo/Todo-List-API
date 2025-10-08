package dev.emmanuelotoo.todolistapi.controllers;

import dev.emmanuelotoo.todolistapi.dtos.TodoItemDto;
import dev.emmanuelotoo.todolistapi.entities.TodoItem;
import dev.emmanuelotoo.todolistapi.services.TodoItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoItemController {
    private final TodoItemService todoItemService;

    public TodoItemController(TodoItemService todoItemService) {
        this.todoItemService = todoItemService;
    }

    @PostMapping
    public ResponseEntity<TodoItem> createTodoItem(@RequestBody TodoItemDto todoItemDto, @RequestParam Long userId) {
        return todoItemService.saveTodoItem(todoItemDto, userId);
    }

    @GetMapping
    public ResponseEntity<List<TodoItem>> getAllTodoItems(@RequestParam Long userId) {
        return ResponseEntity.ok(todoItemService.getAllTodoItemsByUserId(userId));
    }


    @PutMapping("/{todoId}")
    public ResponseEntity<TodoItem> updateTodoItem(@PathVariable Long todoId, @RequestBody TodoItemDto todoItemDto, @RequestParam Long userId) {
        return ResponseEntity.ok(todoItemService.updateTodoItem(todoId, todoItemDto, userId));
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodoItem(@PathVariable Long todoId, @RequestParam Long userId) {
        todoItemService.deleteTodoItem(todoId, userId);
        return ResponseEntity.noContent().build();
    }
}
