package org.pinguweb.backend.controller;

import org.pingu.domain.DTO.CatastropheDTO;
import org.pingu.domain.DTO.ZoneDTO;
import org.pinguweb.backend.DTO.BackendDTOFactory;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.model.Catastrophe;
import org.pinguweb.backend.service.CatastropheService;
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
public class CatastropheController {
    @Autowired
    CatastropheService service;

    @Async
    @GetMapping("/catastrophe")
    public CompletableFuture<ResponseEntity<List<CatastropheDTO>>> getAll(){
        if (ServerException.isServerClosed(service.getCatastropheRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();

        List<CatastropheDTO> catastrophes = service.findAll().stream().map(factory::createCatastropheDTO).collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok(catastrophes));
    }

    @Async
    @GetMapping("/catastrophe/{ID}")
    public CompletableFuture<ResponseEntity<CatastropheDTO>> getCatastrophe(@PathVariable Integer ID) {
        if (ServerException.isServerClosed(service.getCatastropheRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        Optional<Catastrophe> res = service.findByID(ID);
        if (res.isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createCatastropheDTO(res.get())));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @GetMapping("/catastrophe/{ID}/zones")
    public CompletableFuture<ResponseEntity<List<ZoneDTO>>> getCatastropheZones(@PathVariable Integer ID) {
        if (ServerException.isServerClosed(service.getCatastropheRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();

        Optional<Catastrophe> res = service.findByID(ID);
        if (res.isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(
                    res.get().getZones().stream().map(factory::createZoneDTO).collect(Collectors.toList())));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }
}
