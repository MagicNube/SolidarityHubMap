package org.pinguweb.backend.controller;

import org.pinguweb.DTO.SkillDTO;
import org.pinguweb.backend.DTO.BackendDTOFactory;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SkillController {
    @Autowired
    SkillRepository repository;

    @Async
    @GetMapping("/skill")
    public CompletableFuture<ResponseEntity<List<SkillDTO>>> getAll(){
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}
        BackendDTOFactory factory = new BackendDTOFactory();

        List<SkillDTO> skills = repository.findAll().stream().map(factory::createSkillDTO).collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok(skills));
    }


    @Async
    @GetMapping("/skill/{id}")
    public CompletableFuture<ResponseEntity<SkillDTO>> getSkill(@PathVariable String id) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        if (repository.existsById(id)) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createSkillDTO(repository.getReferenceById(id))));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @PostMapping("/skill")
    public CompletableFuture<ResponseEntity<SkillDTO>> addSkill(@RequestBody SkillDTO skill) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        // TODO: No funciona aun
        //return ResponseEntity.ok(repository.save(Skill.fromDTO(skill)).toDTO());
        return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
    }

    @Async
    @DeleteMapping("/skill/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteSkill(@PathVariable String id) {
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
    @PutMapping("/skill")
    public CompletableFuture<ResponseEntity<SkillDTO>> updateSkill(@RequestBody SkillDTO skill) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        if (repository.existsById(skill.getName())) {
            //return ResponseEntity.ok(repository.save(Skill.fromDTO(skill)).toDTO());
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }
}
