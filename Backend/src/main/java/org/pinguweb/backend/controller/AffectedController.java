package org.pinguweb.backend.controller;

import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.model.Affected;
import org.pinguweb.backend.repository.AffectedRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AffectedController {

    AffectedRepository repository;

    @GetMapping("/affected/{id}")
    public ResponseEntity<Affected> getAffected(@PathVariable String id) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            return ResponseEntity.ok(repository.getReferenceById(id));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/affected")
    public ResponseEntity<Affected> addAffected(@RequestBody Affected affected) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        return ResponseEntity.ok(repository.save(affected));
    }

    @DeleteMapping("/affected/{id}")
    public ResponseEntity<Void>  deleteAffected(@PathVariable String id) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/affected")
    public ResponseEntity<Affected>  updateAffected(@RequestBody Affected affected) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(affected.getDNI())) {
            return ResponseEntity.ok(repository.save(affected));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
