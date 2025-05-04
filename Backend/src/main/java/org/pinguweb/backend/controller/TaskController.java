package org.pinguweb.backend.controller;

import org.pingu.domain.DTO.TaskDTO;
import org.pingu.domain.DTO.factories.BackendDTOFactory;
import org.pingu.persistence.model.Task;
import org.pingu.persistence.service.TaskService;
import org.pinguweb.backend.controller.common.ServerException;
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

    @Autowired
    BackendDTOFactory factory;

    @Async
    @GetMapping("/tasks")
    public CompletableFuture<ResponseEntity<List<TaskDTO>>> getAll(){
        if (ServerException.isServerClosed(service.getTaskRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        List<TaskDTO> tasks = service.findAll().stream().map(factory::createDTO).collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok(tasks));
    }

    @Async
    @GetMapping("/tasks/{ID}")
    public CompletableFuture<ResponseEntity<TaskDTO>> getTask(@PathVariable Integer ID) {
        if (ServerException.isServerClosed(service.getTaskRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        Optional<Task> res = service.findByID(ID);

        if (res.isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createDTO(res.get())));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }
}
