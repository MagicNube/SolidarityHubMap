package org.pinguweb.backend.controller;

import org.pingu.domain.DTO.NeedDTO;
import org.pingu.domain.DTO.factories.BackendDTOFactory;
import org.pingu.domain.DTO.factories.ModelDTOFactory;
import org.pingu.persistence.model.Need;
import org.pingu.persistence.service.NeedService;
import org.pinguweb.backend.controller.common.ServerException;
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
public class NeedController {

    @Autowired
    NeedService service;
    @Autowired
    BackendDTOFactory factory;
    @Autowired
    ModelDTOFactory dtoFactory;

    @Async
    @GetMapping("/needs")
    public CompletableFuture<ResponseEntity<List<NeedDTO>>> getAll(){
        if (ServerException.isServerClosed(service.getNeedRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        List<NeedDTO> needs = service.findAll().stream().map(factory::createDTO).collect(Collectors.toList());

        return CompletableFuture.completedFuture(ResponseEntity.ok(needs));
    }

    @Async
    @GetMapping("/needs/{ID}")
    public CompletableFuture<ResponseEntity<NeedDTO>> getNeed(@PathVariable Integer ID) {
        if (ServerException.isServerClosed(service.getNeedRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        Optional<Need> res = service.findByID(ID);
        if (res.isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createDTO(res.get())));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @PostMapping("/needs")
    public CompletableFuture<ResponseEntity<NeedDTO>> addNeed(@RequestBody NeedDTO need) {
        if (ServerException.isServerClosed(service.getNeedRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createDTO(service.saveNeed(dtoFactory.createFromDTO(need)))));
    }

    @Async
    @DeleteMapping("/needs/{ID}")
    public CompletableFuture<ResponseEntity<Void>> deleteNeed(@PathVariable int ID) {
        if (ServerException.isServerClosed(service.getNeedRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        Optional<Need> res = service.findByID(ID);
        if (res.isPresent()) {
            service.deleteNeed(res.get());
            return CompletableFuture.completedFuture(ResponseEntity.ok().build());
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @PutMapping("/needs")
    public CompletableFuture<ResponseEntity<NeedDTO>> updateNeed(@RequestBody NeedDTO need) {
        if (ServerException.isServerClosed(service.getNeedRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        Optional<Need> res = service.findByID(need.getID());
        if (res.isPresent()) {

            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createDTO(service.saveNeed(dtoFactory.createFromDTO(need)))));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }
}
