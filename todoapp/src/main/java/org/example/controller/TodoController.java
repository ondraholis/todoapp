package org.example.controller;

import org.example.model.Todo;
import org.example.service.TodoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class TodoController {

    private final TodoService todoService;

    @Value("${azure.storage.cdn-base-url:}")
    private String cdnBaseUrl;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public String index(Model model) {
        List<Todo> todos = todoService.findAll();
        model.addAttribute("todos", todos);
        model.addAttribute("pendingCount", todos.stream().filter(t -> !t.isCompleted()).count());
        model.addAttribute("cdnBaseUrl", cdnBaseUrl);
        return "index";
    }

    @PostMapping("/add")
    public String add(@RequestParam String title,
                      @RequestParam(required = false) String description) {
        if (title != null && !title.isBlank()) {
            todoService.create(title.trim(), description);
        }
        return "redirect:/";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id) {
        todoService.toggleComplete(id);
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        todoService.delete(id);
        return "redirect:/";
    }
}
