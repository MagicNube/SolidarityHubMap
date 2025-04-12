package org.pinguweb.backend.controller;

import org.pinguweb.DTO.AffectedDTO;
import org.pinguweb.backend.DTO.BackendDTOFactory;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.model.Affected;
import org.pinguweb.backend.repository.AffectedRepository;
import org.pinguweb.backend.service.AffectedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AffectedController {
    @Autowired
    AffectedService service;

    @Async
    @GetMapping("/affected")
    public CompletableFuture<ResponseEntity<List<AffectedDTO>>> getAll(){
        if (ServerException.isServerClosed(service.getAffectedRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();

        List<AffectedDTO> catastrophes = service.findAll().stream().map(factory::createAffectedDTO).collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok(catastrophes));
    }

    @Async
    @GetMapping("/affected/{id}")
    public CompletableFuture<ResponseEntity<AffectedDTO>> getAffected(@PathVariable String id) {
        if (ServerException.isServerClosed(service.getAffectedRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        Optional<Affected> res = service.findByDni(id);
        if (res.isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createAffectedDTO(res.get())));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }
}
