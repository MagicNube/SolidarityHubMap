package org.pinguweb.backend.controller;

import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.model.Skill;
import org.pinguweb.backend.repository.SkillRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SkillController {

    SkillRepository repository;

    @GetMapping("/skill/{id}")
    public ResponseEntity<Skill> getSkill(@PathVariable String id) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            return ResponseEntity.ok(repository.getReferenceById(id));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/skill")
    public ResponseEntity<Skill> addSkill(@RequestBody Skill skill) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        return ResponseEntity.ok(repository.save(skill));
    }

    @DeleteMapping("/skill/{id}")
    public ResponseEntity<Void>  deleteSkill(@PathVariable String id) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/skill")
    public ResponseEntity<Skill>  updateSkill(@RequestBody Skill skill) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(skill.getName())) {
            return ResponseEntity.ok(repository.save(skill));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
