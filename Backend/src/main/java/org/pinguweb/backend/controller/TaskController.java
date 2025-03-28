package org.pinguweb.backend.controller;

import org.pinguweb.DTO.TaskDTO;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.model.Task;
import org.pinguweb.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TaskRepository repository;

    @GetMapping("/task")
    public ResponseEntity<List<TaskDTO>> getAll(){
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        List<TaskDTO> tasks = repository.findAll().stream().map(TaskDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Integer id) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            return ResponseEntity.ok(repository.getReferenceById(id).toDTO());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/task")
    public ResponseEntity<TaskDTO> addTask(@RequestBody TaskDTO task) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        //TODO: Los posts no funcionan muy bien
        return ResponseEntity.ok(repository.save(Task.fromDTO(task)).toDTO());
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<Void>  deleteTask(@PathVariable int id) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/task")
    public ResponseEntity<TaskDTO>  updateTask(@RequestBody TaskDTO task) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        //TODO: Los posts no funcionan muy bien
        if (repository.existsById(task.getId())) {
            return ResponseEntity.ok(repository.save(Task.fromDTO(task)).toDTO());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
