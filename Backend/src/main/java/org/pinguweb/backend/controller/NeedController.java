package org.pinguweb.backend.controller;

import org.pinguweb.backend.DTO.NeedDTO;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.model.Need;
import org.pinguweb.backend.repository.NeedRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class NeedController {

    NeedRepository repository;

    @GetMapping("/need")
    public ResponseEntity<List<NeedDTO>> getAll(){
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        List<NeedDTO> needs = repository.findAll().stream().map(NeedDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(needs);
    }

    @GetMapping("/need/{id}")
    public ResponseEntity<NeedDTO> getNeed(@PathVariable Integer id) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            return ResponseEntity.ok(repository.getReferenceById(id).toDTO());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/need")
    public ResponseEntity<NeedDTO> addNeed(@RequestBody NeedDTO need) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        // TODO: Esto aun no funciona
        return ResponseEntity.ok(repository.save(Need.fromDTO(need)).toDTO());
    }

    @DeleteMapping("/need/{id}")
    public ResponseEntity<Void>  deleteNeed(@PathVariable NeedDTO id) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id.getId())) {
            repository.deleteById(id.getId());
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/need")
    public ResponseEntity<NeedDTO>  updateNeed(@RequestBody NeedDTO need) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        //TODO: Esto no funciona
        if (repository.existsById(need.getId())) {
            return ResponseEntity.ok(repository.save(Need.fromDTO(need)).toDTO());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
