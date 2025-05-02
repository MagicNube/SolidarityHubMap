package org.pinguweb.backend.controller;

import org.pingu.domain.DTO.AffectedDTO;
import org.pingu.domain.DTO.factories.BackendDTOFactory;
import org.pingu.persistence.model.Affected;
import org.pingu.persistence.service.AffectedService;
import org.pinguweb.backend.controller.common.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        List<AffectedDTO> catastrophes = service.findAll().stream().map(factory::createDTO).collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok(catastrophes));
    }

    @Async
    @GetMapping("/affected/{ID}")
    public CompletableFuture<ResponseEntity<AffectedDTO>> getAffected(@PathVariable String ID) {
        if (ServerException.isServerClosed(service.getAffectedRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        Optional<Affected> res = service.findByDni(ID);
        if (res.isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createDTO(res.get())));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }
}
