package org.pinguweb.backend.controller;

import org.pingu.domain.DTO.VolunteerDTO;
import org.pingu.domain.DTO.factories.BackendDTOFactory;
import org.pingu.persistence.model.Volunteer;
import org.pingu.persistence.service.VolunteerService;
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
public class VolunteerController {
    @Autowired
    VolunteerService service;

    @Async
    @GetMapping("/volunteer")
    public CompletableFuture<ResponseEntity<List<VolunteerDTO>>> getAll(){
        if (ServerException.isServerClosed(service.getVolunteerRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}
        BackendDTOFactory factory = new BackendDTOFactory();

        List<VolunteerDTO> volunteers = service.findAll().stream().map(factory::createDTO).collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok(volunteers));
    }

    @Async
    @GetMapping("/volunteer/{ID}")
    public CompletableFuture<ResponseEntity<VolunteerDTO>> getVolunteer(@PathVariable String ID) {
        if (ServerException.isServerClosed(service.getVolunteerRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        Optional<Volunteer> res = service.findByID(ID);
        if (res.isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createDTO(res.get())));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }
}
