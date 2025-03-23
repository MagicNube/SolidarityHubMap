package org.pinguweb.backend.controller;

import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.model.Zone;
import org.pinguweb.backend.repository.ZoneRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ZoneController {

    ZoneRepository repository;

    @GetMapping("/zone")
    public ResponseEntity<List<Zone>> getAll(){
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        List<Zone> zones = repository.findAll();
        return ResponseEntity.ok(zones);
    }

    @GetMapping("/zone/{id}")
    public ResponseEntity<Zone> getZone(@PathVariable Integer id) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            return ResponseEntity.ok(repository.getReferenceById(id));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/zone")
    public ResponseEntity<Zone> addZone(@RequestBody Zone zone) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        return ResponseEntity.ok(repository.save(zone));
    }

    @DeleteMapping("/zone/{id}")
    public ResponseEntity<Void>  deleteZone(@PathVariable Integer id) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/zone")
    public ResponseEntity<Zone>  updateZone(@RequestBody Zone zone) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(zone.getId())) {
            return ResponseEntity.ok(repository.save(zone));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

}
