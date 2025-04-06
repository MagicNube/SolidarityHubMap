package org.pinguweb.backend.controller;

import org.pinguweb.DTO.TaskDTO;
import org.pinguweb.backend.DTO.BackendDTOFactory;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TaskRepository repository;

    @Async
    @GetMapping("/task")
    public CompletableFuture<ResponseEntity<List<TaskDTO>>> getAll(){
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();

        List<TaskDTO> tasks = repository.findAll().stream().map(factory::createTaskDTO).collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok(tasks));
    }

    @Async
    @GetMapping("/task/{id}")
    public CompletableFuture<ResponseEntity<TaskDTO>> getTask(@PathVariable Integer id) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        if (repository.existsById(id)) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createTaskDTO(repository.getReferenceById(id))));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @PostMapping("/task")
    public CompletableFuture<ResponseEntity<TaskDTO>> addTask(@RequestBody TaskDTO task) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        //TODO: Los posts no funcionan muy bien
        //return ResponseEntity.ok(repository.save(Task.fromDTO(task)).toDTO());
        return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
    }

    @Async
    @DeleteMapping("/task/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteTask(@PathVariable int id) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        if (repository.existsById(id)) {
            repository.deleteById(id);
            return CompletableFuture.completedFuture(ResponseEntity.ok().build());
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @PutMapping("/task")
    public CompletableFuture<ResponseEntity<TaskDTO>> updateTask(@RequestBody TaskDTO task) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        //TODO: Los posts no funcionan muy bien
        if (repository.existsById(task.getId())) {
            //return ResponseEntity.ok(repository.save(Task.fromDTO(task)).toDTO());
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }
}
