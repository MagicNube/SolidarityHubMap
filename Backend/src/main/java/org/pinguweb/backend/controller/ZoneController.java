package org.pinguweb.backend.controller;

import org.pinguweb.DTO.AffectedDTO;
import org.pinguweb.DTO.DTOFactory;
import org.pinguweb.DTO.TaskDTO;
import org.pinguweb.DTO.ZoneDTO;
import org.pinguweb.model.GPSCoordinates;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.model.Task;
import org.pinguweb.model.Zone;
import org.pinguweb.backend.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ZoneController {
    @Autowired
    ZoneRepository repository;
    @Autowired
    TaskController taskController;

    @GetMapping("/zone")
    public ResponseEntity<List<ZoneDTO>> getAll(){
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        List<ZoneDTO> zones = repository.findAll().stream().map(x -> DTOFactory.createDTO(ZoneDTO.class, x)).collect(Collectors.toList());
        return ResponseEntity.ok(zones);
    }

    @GetMapping("/zone/{id}")
    public ResponseEntity<ZoneDTO> getZone(@PathVariable int id) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            return ResponseEntity.ok(DTOFactory.createDTO(ZoneDTO.class, repository.findById(id)));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/zone/{id}/tasks")
    public ResponseEntity<List<TaskDTO>> getZoneTasks(@PathVariable int id) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            List<TaskDTO> tasks = taskController.getAll().getBody();
            Zone zone = repository.getReferenceById(id);

            if (tasks != null) {
                List<TaskDTO> tasksInZone = tasks.stream()
                                              .filter(task -> task.getZone() == zone.getId())
                                              .collect(Collectors.toList());
                return ResponseEntity.ok(tasksInZone);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // TODO: Esto no va
    @GetMapping("/zone/{id}/points")
    public ResponseEntity<List<GPSCoordinates>> getZonePoints(@PathVariable ZoneDTO id) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id.getId())) {
            return ResponseEntity.ok(repository.getReferenceById(id.getId()).getPoints());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/zone")
    public ResponseEntity<ZoneDTO> addZone(@RequestBody ZoneDTO zone) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        // TODO: Esto no funciona aun

        //return ResponseEntity.ok(repository.save(Zone.fromDTO(zone)).toDTO());
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/zone/{id}")
    public ResponseEntity<Void>  deleteZone(@PathVariable int id) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/zone")
    public ResponseEntity<ZoneDTO>  updateZone(@RequestBody ZoneDTO zone) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        // TODO: Esto no funciona aun

        if (repository.existsById(zone.getId())) {
            //return ResponseEntity.ok(repository.save(Zone.fromDTO(zone)).toDTO());
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

}
