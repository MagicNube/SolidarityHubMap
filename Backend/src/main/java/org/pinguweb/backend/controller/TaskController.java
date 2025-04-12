package org.pinguweb.backend.controller;

import org.pinguweb.DTO.TaskDTO;
import org.pinguweb.backend.DTO.BackendDTOFactory;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.model.Task;
import org.pinguweb.backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/task/{ID}")
    public CompletableFuture<ResponseEntity<TaskDTO>> getTask(@PathVariable Integer ID) {
        if (ServerException.isServerClosed(service.getTaskRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        Optional<Task> res = service.findByID(ID);

        if (res.isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createTaskDTO(res.get())));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }
}
