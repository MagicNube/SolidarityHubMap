package org.pinguweb.backend.controller;

import org.pinguweb.backend.model.Task;
import org.pinguweb.backend.repository.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    TaskRepository repository;

    @GetMapping("/task")
    public ResponseEntity<List<Task>> getAll(){
        List<Task> tasks = repository.findAll();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Integer id) {
        if (repository.existsById(id)) {
            return ResponseEntity.ok(repository.getReferenceById(id));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/task")
    public ResponseEntity<Task> addTask(@RequestBody Task task) {
        return ResponseEntity.ok(repository.save(task));
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<Void>  deleteTask(@PathVariable Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/task")
    public ResponseEntity<Task>  updateTask(@RequestBody Task task) {
        if (repository.existsById(task.getId())) {
            return ResponseEntity.ok(repository.save(task));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
