package org.pinguweb.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.TaskDTO;
import org.pingu.domain.DTO.ZoneDTO;
import org.pingu.domain.DTO.factories.BackendDTOFactory;
import org.pingu.domain.DTO.factories.ModelDTOFactory;
import org.pingu.persistence.model.Task;
import org.pingu.persistence.model.Zone;
import org.pingu.persistence.service.TaskService;
import org.pingu.persistence.service.ZoneService;
import org.pinguweb.backend.controller.common.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
public class ZoneController {
    @Autowired
    ZoneService service;
    @Autowired
    TaskService taskService;
    @Autowired
    ModelDTOFactory dtoFactory;
    @Autowired
    BackendDTOFactory factory;

    @Async
    @GetMapping("/zones")
    public CompletableFuture<ResponseEntity<List<ZoneDTO>>> getAll(){
        if (ServerException.isServerClosed(service.getZoneRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}


        List<ZoneDTO> zones = service.findAll().stream().map(factory::createDTO).collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok(zones));
    }

    @Async
    @GetMapping("/zones/{ID}")
    public CompletableFuture<ResponseEntity<ZoneDTO>> getZone(@PathVariable int ID) {
        if (ServerException.isServerClosed(service.getZoneRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        Optional<Zone> res = service.findByID(ID);
        if (res.isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createDTO(res.get())));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @GetMapping("/zones/{ID}/tasks")
    public CompletableFuture<ResponseEntity<List<TaskDTO>>> getZoneTasks(@PathVariable int ID) {
        if (ServerException.isServerClosed(service.getZoneRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        try {
            Optional<Zone> res = service.findByID(ID);
            if (res.isPresent()) {
                List<Task> tasks = taskService.findAll();
                Zone zone = res.get();

                if (tasks != null) {
                    List<Task> tasksInZone = tasks.stream()
                            .filter(task -> task.getZone().getID() == zone.getID())
                            .toList();
                    return CompletableFuture.completedFuture(ResponseEntity.ok(tasksInZone.stream().map(factory::createDTO).toList()));
                }
            }
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
        catch (Exception e){
            log.error(e.getMessage(),  Arrays.stream(e.getStackTrace()).toArray());
        }
        return null;
    }

    @Async
    @PostMapping("/zones")
    public CompletableFuture<ResponseEntity<ZoneDTO>> addZone(@RequestBody ZoneDTO zone) {
        if (ServerException.isServerClosed(service.getZoneRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createDTO(service.saveZone(dtoFactory.createFromDTO(zone)))));
    }

    @Async
    @DeleteMapping("/zones/{ID}")
    public CompletableFuture<ResponseEntity<Void>> deleteZone(@PathVariable int ID) {
        if (ServerException.isServerClosed(service.getZoneRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        Optional<Zone> res = service.findByID(ID);
        if (res.isPresent()) {
            service.delete(res.get());
            return CompletableFuture.completedFuture(ResponseEntity.ok().build());
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @PutMapping("/zones/{ID}")
    public CompletableFuture<ResponseEntity<ZoneDTO>> updateZone(@RequestBody ZoneDTO zone, @PathVariable int ID) {
        if (ServerException.isServerClosed(service.getZoneRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        Optional<Zone> res = service.findByID(ID);
        if (res.isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createDTO(service.saveZone(dtoFactory.createFromDTO(zone)))));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

}
