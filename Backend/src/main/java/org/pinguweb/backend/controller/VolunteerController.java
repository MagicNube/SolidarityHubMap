package org.pinguweb.backend.controller;

import org.pinguweb.DTO.VolunteerDTO;
import org.pinguweb.backend.DTO.BackendDTOFactory;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.repository.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class VolunteerController {
    @Autowired
    VolunteerRepository repository;

    @Async
    @GetMapping("/volunteer")
    public CompletableFuture<ResponseEntity<List<VolunteerDTO>>> getAll(){
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}
        BackendDTOFactory factory = new BackendDTOFactory();

        List<VolunteerDTO> volunteers = repository.findAll().stream().map(factory::createVolunteerDTO).collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok(volunteers));
    }

    @Async
    @GetMapping("/volunteer/{id}")
    public CompletableFuture<ResponseEntity<VolunteerDTO>> getVolunteer(@PathVariable String id) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        if (repository.existsById(id)) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createVolunteerDTO(repository.getReferenceById(id))));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @PostMapping("/volunteer")
    public CompletableFuture<ResponseEntity<VolunteerDTO>> addVolunteer(@RequestBody VolunteerDTO volunteer) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        // TODO: Esto aun no funciona
        //return ResponseEntity.ok(repository.save(Volunteer.fromDTO(volunteer)).toDTO());
        return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
    }

    @Async
    @DeleteMapping("/volunteer/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteVolunteer(@PathVariable String id) {
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
    @PutMapping("/volunteer")
    public CompletableFuture<ResponseEntity<VolunteerDTO>> updateVolunteer(@RequestBody VolunteerDTO volunteer) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        if (repository.existsById(volunteer.getDni())) {
            //return ResponseEntity.ok(repository.save(Volunteer.fromDTO(volunteer)).toDTO());
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }
}
