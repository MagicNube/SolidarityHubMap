package org.pinguweb.backend.controller;

import org.pinguweb.backend.DTO.AffectedDTO;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.model.Affected;
import org.pinguweb.backend.model.Catastrophe;
import org.pinguweb.backend.repository.AffectedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AffectedController {
    @Autowired
    AffectedRepository repository;

    @GetMapping("/affeected")
    public ResponseEntity<List<AffectedDTO>> getAll(){
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        List<AffectedDTO> catastrophes = repository.findAll().stream().map(AffectedDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(catastrophes);
    }

    @GetMapping("/affected/{id}")
    public ResponseEntity<AffectedDTO> getAffected(@PathVariable String id) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            return ResponseEntity.ok(repository.getReferenceById(id).toDTO());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/affected")
    public ResponseEntity<AffectedDTO> addAffected(@RequestBody AffectedDTO affected) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        // TODO: Esto no funciona
        return ResponseEntity.ok(repository.save(Affected.fromDTO(affected)).toDTO());
    }

    @DeleteMapping("/affected/{id}")
    public ResponseEntity<Void>  deleteAffected(@PathVariable AffectedDTO id) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id.getDni())) {
            repository.deleteById(id.getDni());
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/affected")
    public ResponseEntity<AffectedDTO>  updateAffected(@RequestBody AffectedDTO affected) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        // TODO: Esto aun no funciona
        if (repository.existsById(affected.getDni())) {
            return ResponseEntity.ok(repository.save(Affected.fromDTO(affected)).toDTO());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
