package org.pinguweb.backend.controller;

import org.pinguweb.backend.model.Admin;
import org.pinguweb.backend.model.Catastrophe;
import org.pinguweb.backend.model.Need;
import org.pinguweb.backend.repository.AdminRepository;
import org.pinguweb.backend.repository.CatastropheRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CatastropheController {

    CatastropheRepository repository;

    @GetMapping("/catastrophe")
    public ResponseEntity<List<Catastrophe>> getAll(){
        List<Catastrophe> catastrophes = repository.findAll();
        return ResponseEntity.ok(catastrophes);
    }

    @GetMapping("/catastrophe/{id}")
    public ResponseEntity<Catastrophe> getCatastrophe(@PathVariable Integer id) {
        if (repository.existsById(id)) {
            return ResponseEntity.ok(repository.getReferenceById(id));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/catastrophe")
    public ResponseEntity<Catastrophe> addCatastrophe(@RequestBody Catastrophe catastrophe) {
        return ResponseEntity.ok(repository.save(catastrophe));
    }

    @DeleteMapping("/catastrophe/{id}")
    public ResponseEntity<Void>  deleteCatastrophe(@PathVariable Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/catastrophe")
    public ResponseEntity<Catastrophe>  updateCatastrophe(@RequestBody Catastrophe catastrophe) {
        if (repository.existsById(catastrophe.getID())) {
            return ResponseEntity.ok(repository.save(catastrophe));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
