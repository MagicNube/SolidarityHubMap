package org.pinguweb.backend.controller;

import org.pinguweb.backend.DTO.NeedDTO;
import org.pinguweb.backend.DTO.SkillDTO;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.model.Skill;
import org.pinguweb.backend.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SkillController {
    @Autowired
    SkillRepository repository;

    @GetMapping("/skill")
    public ResponseEntity<List<SkillDTO>> getAll(){
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        List<SkillDTO> skills = repository.findAll().stream().map(SkillDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(skills);
    }


    @GetMapping("/skill/{id}")
    public ResponseEntity<SkillDTO> getSkill(@PathVariable String id) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            return ResponseEntity.ok(repository.getReferenceById(id).toDTO());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/skill")
    public ResponseEntity<SkillDTO> addSkill(@RequestBody SkillDTO skill) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        // TODO: No funciona aun
        return ResponseEntity.ok(repository.save(Skill.fromDTO(skill)).toDTO());
    }

    @DeleteMapping("/skill/{id}")
    public ResponseEntity<Void>  deleteSkill(@PathVariable SkillDTO id) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id.getName())) {
            repository.deleteById(id.getName());
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/skill")
    public ResponseEntity<SkillDTO>  updateSkill(@RequestBody SkillDTO skill) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(skill.getName())) {
            return ResponseEntity.ok(repository.save(Skill.fromDTO(skill)).toDTO());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
