package org.pinguweb.backend.controller;

import org.pinguweb.DTO.CatastropheDTO;
import org.pinguweb.DTO.DTOFactory;
import org.pinguweb.DTO.ZoneDTO;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.repository.CatastropheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CatastropheController {
    @Autowired
    CatastropheRepository repository;

    @GetMapping("/catastrophe")
    public ResponseEntity<List<CatastropheDTO>> getAll(){
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        List<CatastropheDTO> catastrophes = repository.findAll().stream()
                .map(x-> DTOFactory.createDTO(CatastropheDTO.class, x)).collect(Collectors.toList());
        return ResponseEntity.ok(catastrophes);
    }

    @GetMapping("/catastrophe/{id}")
    public ResponseEntity<CatastropheDTO> getCatastrophe(@PathVariable Integer id) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            return ResponseEntity.ok(DTOFactory.createDTO(CatastropheDTO.class, repository.findById(id)));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/catastrophe/{id}/zones")
    public ResponseEntity<List<ZoneDTO>> getCatastropheZones(@PathVariable Integer id) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            return ResponseEntity.ok(repository.getReferenceById(id).getZones().stream().map(x -> DTOFactory.createDTO(ZoneDTO.class, x)).collect(Collectors.toList()));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/catastrophe")
    public ResponseEntity<CatastropheDTO> addCatastrophe(@RequestBody CatastropheDTO catastrophe) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        // TODO: Esto no funciona
        //return ResponseEntity.ok(repository.save(Catastrophe.fromDTO(catastrophe)).toDTO());
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/catastrophe/{id}")
    public ResponseEntity<Void>  deleteCatastrophe(@PathVariable int id) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/catastrophe")
    public ResponseEntity<CatastropheDTO>  updateCatastrophe(@RequestBody CatastropheDTO catastrophe) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(catastrophe.getId())) {
            //return ResponseEntity.ok(repository.save(Catastrophe.fromDTO(catastrophe)).toDTO());
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
