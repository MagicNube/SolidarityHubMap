package org.pinguweb.backend.controller;

import org.pinguweb.DTO.TaskDTO;
import org.pinguweb.backend.DTO.BackendDTOFactory;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.model.Task;
import org.pinguweb.backend.repository.TaskRepository;
import org.pinguweb.backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TaskService service;

    @Async
    @GetMapping("/task")
    public CompletableFuture<ResponseEntity<List<TaskDTO>>> getAll(){
        if (ServerException.isServerClosed(service.getTaskRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();

        List<TaskDTO> tasks = service.findAll().stream().map(factory::createTaskDTO).collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok(tasks));
    }

    @Async
    @GetMapping("/task/{id}")
    public CompletableFuture<ResponseEntity<TaskDTO>> getTask(@PathVariable Integer id) {
        if (ServerException.isServerClosed(service.getTaskRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        Optional<Task> res = service.findByID(id);

        if (res.isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createTaskDTO(res.get())));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }
}
