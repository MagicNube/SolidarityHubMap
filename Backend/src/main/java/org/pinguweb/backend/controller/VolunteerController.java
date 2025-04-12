package org.pinguweb.backend.controller;

import org.pinguweb.DTO.VolunteerDTO;
import org.pinguweb.backend.DTO.BackendDTOFactory;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.model.Volunteer;
import org.pinguweb.backend.repository.VolunteerRepository;
import org.pinguweb.backend.service.VolunteerService;
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
public class VolunteerController {
    @Autowired
    VolunteerService service;

    @Async
    @GetMapping("/volunteer")
    public CompletableFuture<ResponseEntity<List<VolunteerDTO>>> getAll(){
        if (ServerException.isServerClosed(service.getVolunteerRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}
        BackendDTOFactory factory = new BackendDTOFactory();

        List<VolunteerDTO> volunteers = service.findAll().stream().map(factory::createVolunteerDTO).collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok(volunteers));
    }

    @Async
    @GetMapping("/volunteer/{id}")
    public CompletableFuture<ResponseEntity<VolunteerDTO>> getVolunteer(@PathVariable String id) {
        if (ServerException.isServerClosed(service.getVolunteerRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        Optional<Volunteer> res = service.findByID(id);
        if (res.isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createVolunteerDTO(res.get())));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }
}
