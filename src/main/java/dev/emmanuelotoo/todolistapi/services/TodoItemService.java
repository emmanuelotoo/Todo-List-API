package dev.emmanuelotoo.todolistapi.services;

import dev.emmanuelotoo.todolistapi.dtos.TodoItemDto;
import dev.emmanuelotoo.todolistapi.entities.TodoItem;
import dev.emmanuelotoo.todolistapi.repositories.TodoItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoItemService {
    private final TodoItemRepository todoItemRepository;

    public TodoItemService(TodoItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }

    // Creating a To-Do item
    public ResponseEntity<TodoItem> saveTodoItem(TodoItemDto todoItemDto) {
        TodoItem todoItem =  new TodoItem();
        todoItem.setTitle(todoItemDto.title());
        todoItem.setDescription(todoItemDto.description());

        TodoItem savedTodoItem = todoItemRepository.save(todoItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTodoItem);
    }


    // Getting all To-Do items
    public List<TodoItem> getAllTodoItems() {
        return todoItemRepository.findAll();
    }


    // Getting a To-Do item by its id
    public Optional<TodoItem> getTodoItemById(Long id) {
        return todoItemRepository.findById(id);
    }


    // Deleting a To-Do item
    public void deleteTodoItem(Long id) {
        todoItemRepository.deleteById(id);
    }


    // Updating a To-Do item
    public TodoItem updateTodoItem(Long id, TodoItemDto todoItemDto) {
        TodoItem todoItem = todoItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Todo item with id " + id + " not found"));


        // Check if title is empty or null, if so, retain the existing title
        String title = todoItemDto.title().trim().isEmpty() ? todoItem.getTitle() : todoItemDto.title();
        todoItem.setTitle(title);


        // Check if description is empty or null, if so, retain the existing description
        String description = todoItemDto.description().trim().isEmpty() ? todoItem.getDescription() : todoItemDto.description();
        todoItem.setDescription(description);


        return todoItemRepository.save(todoItem);
    }

}
