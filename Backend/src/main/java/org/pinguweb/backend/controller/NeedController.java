package org.pinguweb.backend.controller;

import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.model.Need;
import org.pinguweb.backend.repository.NeedRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NeedController {

    NeedRepository repository;

    @GetMapping("/need")
    public ResponseEntity<List<Need>> getAll(){
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        List<Need> needs = repository.findAll();
        return ResponseEntity.ok(needs);
    }

    @GetMapping("/need/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable Integer id) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            return ResponseEntity.ok(repository.getReferenceById(id));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/need")
    public ResponseEntity<Need> addNeed(@RequestBody Need need) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        return ResponseEntity.ok(repository.save(need));
    }

    @DeleteMapping("/need/{id}")
    public ResponseEntity<Void>  deleteNeed(@PathVariable Integer id) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/need")
    public ResponseEntity<Need>  updateNeed(@RequestBody Need need) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(need.getId())) {
            return ResponseEntity.ok(repository.save(need));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
