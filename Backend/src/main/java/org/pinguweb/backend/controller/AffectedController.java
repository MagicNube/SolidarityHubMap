package org.pinguweb.backend.controller;

import org.pinguweb.DTO.AffectedDTO;
import org.pinguweb.backend.DTO.BackendDTOFactory;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.repository.AffectedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AffectedController {
    @Autowired
    AffectedRepository repository;

    @Async
    @GetMapping("/affected")
    public CompletableFuture<ResponseEntity<List<AffectedDTO>>> getAll(){
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();

        List<AffectedDTO> catastrophes = repository.findAll().stream().map(factory::createAffectedDTO).collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok(catastrophes));
    }

    @Async
    @GetMapping("/affected/{id}")
    public CompletableFuture<ResponseEntity<AffectedDTO>> getAffected(@PathVariable String id) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        if (repository.existsById(id)) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createAffectedDTO(repository.getReferenceById(id))));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @PostMapping("/affected")
    public CompletableFuture<ResponseEntity<AffectedDTO>> addAffected(@RequestBody AffectedDTO affected) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        // TODO: Esto no funciona
        //return ResponseEntity.ok(repository.save(Affected.fromDTO(affected)).toDTO());

        return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
    }

    @Async
    @DeleteMapping("/affected/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteAffected(@PathVariable String id) {
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
    @PutMapping("/affected")
    public CompletableFuture<ResponseEntity<AffectedDTO>> updateAffected(@RequestBody AffectedDTO affected) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        // TODO: Esto aun no funciona
        if (repository.existsById(affected.getDni())) {
            //return ResponseEntity.ok(repository.save(Affected.fromDTO(affected)).toDTO());
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }
}
