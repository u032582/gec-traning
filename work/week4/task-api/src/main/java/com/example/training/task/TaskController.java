package com.example.training.task;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> list() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
public Task getById(@PathVariable("id") Long id) {
    return taskService.findById(id);
}

@PostMapping
public ResponseEntity<Task> create(@Valid @RequestBody TaskRequest request) {
    Task created = taskService.create(request);
    URI location = URI.create("/api/tasks/" + created.getId());
    return ResponseEntity.created(location).body(created);
}

@PutMapping("/{id}")
public Task update(@PathVariable("id") Long id,
                   @Valid @RequestBody TaskRequest request) {
    return taskService.update(id, request);
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    taskService.delete(id);
    return ResponseEntity.noContent().build(); // 204
}
}