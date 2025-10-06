package dev.emmanuelotoo.todolistapi.controllers;

import dev.emmanuelotoo.todolistapi.dtos.TodoItemDto;
import dev.emmanuelotoo.todolistapi.entities.TodoItem;
import dev.emmanuelotoo.todolistapi.services.TodoItemService;
import jakarta.persistence.EntityNotFoundException;
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


    // Creating a To-Do item
    @PostMapping()
    public ResponseEntity<TodoItem> createTodoItem(@RequestBody TodoItemDto todoItemDto) {
        return todoItemService.saveTodoItem(todoItemDto);
    }


    // Getting all To-Do items
    @GetMapping()
    public ResponseEntity<List<TodoItem>> getAllTodoItems() {
        List<TodoItem> todoItems = todoItemService.getAllTodoItems();
        return ResponseEntity.ok(todoItems);
    }


    // Deleting a To-Do item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoItem(@PathVariable Long id) {
        if (todoItemService.getTodoItemById(id).isPresent()) {
            todoItemService.deleteTodoItem(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Updating a To-Do item
    @PutMapping("/{id}")
    public ResponseEntity<TodoItem> updateTodoItem(@PathVariable Long id, @RequestBody TodoItemDto todoItemDto) {
        try {
            TodoItem updatedTodoItem = todoItemService.updateTodoItem(id, todoItemDto);
            return ResponseEntity.ok(updatedTodoItem);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
