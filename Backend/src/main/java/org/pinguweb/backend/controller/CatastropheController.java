package org.pinguweb.backend.controller;

import org.pinguweb.DTO.CatastropheDTO;
import org.pinguweb.DTO.ZoneDTO;
import org.pinguweb.backend.DTO.BackendDTOFactory;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.repository.CatastropheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CatastropheController {
    @Autowired
    CatastropheRepository repository;

    @Async
    @GetMapping("/catastrophe")
    public CompletableFuture<ResponseEntity<List<CatastropheDTO>>> getAll(){
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();

        List<CatastropheDTO> catastrophes = repository.findAll().stream().map(factory::createCatastropheDTO).collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok(catastrophes));
    }

    @Async
    @GetMapping("/catastrophe/{id}")
    public CompletableFuture<ResponseEntity<CatastropheDTO>> getCatastrophe(@PathVariable Integer id) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        if (repository.existsById(id)) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createCatastropheDTO(repository.getReferenceById(id))));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @GetMapping("/catastrophe/{id}/zones")
    public CompletableFuture<ResponseEntity<List<ZoneDTO>>> getCatastropheZones(@PathVariable Integer id) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();

        if (repository.existsById(id)) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(
                    repository.getReferenceById(id).getZones().stream().map(factory::createZoneDTO).collect(Collectors.toList())));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @PostMapping("/catastrophe")
    public CompletableFuture<ResponseEntity<CatastropheDTO>> addCatastrophe(@RequestBody CatastropheDTO catastrophe) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        // TODO: Esto no funciona
        //return ResponseEntity.ok(repository.save(Catastrophe.fromDTO(catastrophe)).toDTO());
        return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
    }

    @Async
    @DeleteMapping("/catastrophe/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteCatastrophe(@PathVariable int id) {
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
    @PutMapping("/catastrophe")
    public CompletableFuture<ResponseEntity<CatastropheDTO>> updateCatastrophe(@RequestBody CatastropheDTO catastrophe) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        if (repository.existsById(catastrophe.getId())) {
            //return ResponseEntity.ok(repository.save(Catastrophe.fromDTO(catastrophe)).toDTO());
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }
}
