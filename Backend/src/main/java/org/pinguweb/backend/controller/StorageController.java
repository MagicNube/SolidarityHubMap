package org.pinguweb.backend.controller;

import org.pingu.domain.DTO.StorageDTO;
import org.pingu.domain.DTO.factories.BackendDTOFactory;
import org.pingu.domain.DTO.factories.ModelDTOFactory;
import org.pingu.persistence.model.Storage;
import org.pingu.persistence.service.StorageService;
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
public class StorageController {
    @Autowired
    StorageService service;
    @Autowired
    BackendDTOFactory factory;
    @Autowired
    ModelDTOFactory dtoFactory;

    @Async
    @GetMapping("/storages")
    public CompletableFuture<ResponseEntity<List<StorageDTO>>> getAll(){
        if (ServerException.isServerClosed(service.getStorageRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}


        List<StorageDTO> zones = service.findAll().stream().map(factory::createDTO).collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok(zones));
    }

    @Async
    @GetMapping("/storages/{ID}")
    public CompletableFuture<ResponseEntity<StorageDTO>> getStorage(@PathVariable int ID) {
        if (ServerException.isServerClosed(service.getStorageRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        Optional<Storage> res = service.findByID(ID);
        if (res.isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createDTO(res.get())));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @PostMapping("/storages")
    public CompletableFuture<ResponseEntity<StorageDTO>> addZone(@RequestBody StorageDTO storage) {
        if (ServerException.isServerClosed(service.getStorageRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createDTO(service.saveStorage(dtoFactory.createFromDTO(storage)))));
    }

    @Async
    @DeleteMapping("/storages/{ID}")
    public CompletableFuture<ResponseEntity<Void>> deleteZone(@PathVariable int ID) {
        if (ServerException.isServerClosed(service.getStorageRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        Optional<Storage> res = service.findByID(ID);
        if (res.isPresent()) {
            service.delete(res.get());
            return CompletableFuture.completedFuture(ResponseEntity.ok().build());
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @PutMapping("/storages")
    public CompletableFuture<ResponseEntity<StorageDTO>> updateZone(@RequestBody StorageDTO storage) {
        if (ServerException.isServerClosed(service.getStorageRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        Optional<Storage> res = service.findByID(storage.getID());
        if (res.isPresent()) {

            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createDTO(service.saveStorage(dtoFactory.createFromDTO(storage)))));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

}
