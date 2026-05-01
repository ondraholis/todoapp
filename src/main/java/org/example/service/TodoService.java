package org.example.service;

import org.example.model.Todo;
import org.example.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TodoService {

    private final TodoRepository repo;

    public TodoService(TodoRepository repo) {
        this.repo = repo;
    }

    public List<Todo> findAll() {
        return repo.findAllByOrderByCreatedAtDesc();
    }

    public Todo create(String title, String description) {
        return repo.save(new Todo(title, description));
    }

    public void toggleComplete(Long id) {
        Todo todo = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found: " + id));
        todo.setCompleted(!todo.isCompleted());
        repo.save(todo);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
