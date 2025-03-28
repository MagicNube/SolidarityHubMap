package org.pinguweb.backend.controller;

import org.pinguweb.DTO.TaskDTO;
import org.pinguweb.DTO.ZoneDTO;
import org.pinguweb.backend.DTO.BackendDTOFactory;
import org.pinguweb.backend.model.GPSCoordinates;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.model.Zone;
import org.pinguweb.backend.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ZoneController {
    @Autowired
    ZoneRepository repository;
    @Autowired
    TaskController taskController;

    @Async
    @GetMapping("/zone")
    public CompletableFuture<ResponseEntity<List<ZoneDTO>>> getAll(){
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();

        List<ZoneDTO> zones = repository.findAll().stream().map(factory::createZoneDTO).collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok(zones));
    }

    @Async
    @GetMapping("/zone/{id}")
    public CompletableFuture<ResponseEntity<ZoneDTO>> getZone(@PathVariable int id) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        if (repository.existsById(id)) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createZoneDTO(repository.getReferenceById(id))));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @GetMapping("/zone/{id}/tasks")
    public CompletableFuture<ResponseEntity<List<TaskDTO>>> getZoneTasks(@PathVariable int id) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        try {
            if (repository.existsById(id)) {
                List<TaskDTO> tasks = taskController.getAll().get().getBody();
                Zone zone = repository.getReferenceById(id);

                if (tasks != null) {
                    List<TaskDTO> tasksInZone = tasks.stream()
                            .filter(task -> task.getZone() == zone.getId())
                            .collect(Collectors.toList());
                    return CompletableFuture.completedFuture(ResponseEntity.ok(tasksInZone));
                }
            }
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
        catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    // TODO: Esto no va
    @Async
    @GetMapping("/zone/{id}/points")
    public CompletableFuture<ResponseEntity<List<GPSCoordinates>>> getZonePoints(@PathVariable ZoneDTO id) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        if (repository.existsById(id.getId())) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(repository.getReferenceById(id.getId()).getPoints()));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @PostMapping("/zone")
    public CompletableFuture<ResponseEntity<ZoneDTO>> addZone(@RequestBody ZoneDTO zone) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        // TODO: Esto no funciona aun

        //return ResponseEntity.ok(repository.save(Zone.fromDTO(zone)).toDTO());
        return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
    }

    @Async
    @DeleteMapping("/zone/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteZone(@PathVariable int id) {
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
    @PutMapping("/zone")
    public CompletableFuture<ResponseEntity<ZoneDTO>> updateZone(@RequestBody ZoneDTO zone) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        // TODO: Esto no funciona aun

        if (repository.existsById(zone.getId())) {
            //return ResponseEntity.ok(repository.save(Zone.fromDTO(zone)).toDTO());
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

}
