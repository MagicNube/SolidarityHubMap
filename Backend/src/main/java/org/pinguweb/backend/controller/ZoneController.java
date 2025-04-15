package org.pinguweb.backend.controller;

import org.pinguweb.DTO.TaskDTO;
import org.pinguweb.DTO.ZoneDTO;
import org.pinguweb.backend.DTO.BackendDTOFactory;
import org.pinguweb.backend.DTO.ModelDTOFactory;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.model.Task;
import org.pinguweb.backend.model.Zone;
import org.pinguweb.backend.service.TaskService;
import org.pinguweb.backend.service.ZoneService;
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
public class ZoneController {
    @Autowired
    ZoneService service;
    @Autowired
    TaskService taskService;

    @Async
    @GetMapping("/zone")
    public CompletableFuture<ResponseEntity<List<ZoneDTO>>> getAll(){
        if (ServerException.isServerClosed(service.getZoneRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();

        List<ZoneDTO> zones = service.findAll().stream().map(factory::createZoneDTO).collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok(zones));
    }

    @Async
    @GetMapping("/zone/{ID}")
    public CompletableFuture<ResponseEntity<ZoneDTO>> getZone(@PathVariable int ID) {
        if (ServerException.isServerClosed(service.getZoneRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        Optional<Zone> res = service.findByID(ID);
        if (res.isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createZoneDTO(res.get())));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @GetMapping("/zone/{ID}/tasks")
    public CompletableFuture<ResponseEntity<List<TaskDTO>>> getZoneTasks(@PathVariable int ID) {
        if (ServerException.isServerClosed(service.getZoneRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        try {
            Optional<Zone> res = service.findByID(ID);
            if (res.isPresent()) {
                List<Task> tasks = taskService.findAll();
                Zone zone = res.get();

                if (tasks != null) {
                    List<Task> tasksInZone = tasks.stream()
                            .filter(task -> task.getZone().getID() == zone.getID())
                            .collect(Collectors.toList());
                    return CompletableFuture.completedFuture(ResponseEntity.ok(tasksInZone.stream().map(factory::createTaskDTO).toList()));
                }
            }
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Async
    @PostMapping("/zone")
    public CompletableFuture<ResponseEntity<ZoneDTO>> addZone(@RequestBody ZoneDTO zone) {
        if (ServerException.isServerClosed(service.getZoneRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        ModelDTOFactory factory = new ModelDTOFactory();
        BackendDTOFactory dtoFactory = new BackendDTOFactory();

        return CompletableFuture.completedFuture(ResponseEntity.ok(dtoFactory.createZoneDTO(service.saveZone(factory.createFromDTO(zone)))));
    }

    @Async
    @DeleteMapping("/zone/{ID}")
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
    @PutMapping("/zone")
    public CompletableFuture<ResponseEntity<ZoneDTO>> updateZone(@RequestBody ZoneDTO zone) {
        if (ServerException.isServerClosed(service.getZoneRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        Optional<Zone> res = service.findByID(zone.getID());
        if (res.isPresent()) {
            ModelDTOFactory factory = new ModelDTOFactory();
            BackendDTOFactory dtoFactory = new BackendDTOFactory();

            return CompletableFuture.completedFuture(ResponseEntity.ok(dtoFactory.createZoneDTO(service.saveZone(factory.createFromDTO(zone)))));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

}
