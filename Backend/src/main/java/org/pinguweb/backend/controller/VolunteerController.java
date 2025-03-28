package org.pinguweb.backend.controller;

import org.pinguweb.DTO.AffectedDTO;
import org.pinguweb.DTO.DTOFactory;
import org.pinguweb.DTO.VolunteerDTO;
import org.pinguweb.backend.DTO.BackendDTOFactory;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.repository.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class VolunteerController {
    @Autowired
    VolunteerRepository repository;

    @GetMapping("/volunteer")
    public ResponseEntity<List<VolunteerDTO>> getAll(){
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}
        BackendDTOFactory factory = new BackendDTOFactory();

        List<VolunteerDTO> volunteers = repository.findAll().stream().map(factory::createVolunteerDTO).collect(Collectors.toList());
        return ResponseEntity.ok(volunteers);
    }

    @GetMapping("/volunteer/{id}")
    public ResponseEntity<VolunteerDTO> getVolunteer(@PathVariable String id) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        BackendDTOFactory factory = new BackendDTOFactory();
        if (repository.existsById(id)) {
            return ResponseEntity.ok(factory.createVolunteerDTO(repository.getReferenceById(id)));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/volunteer")
    public ResponseEntity<VolunteerDTO> addVolunteer(@RequestBody VolunteerDTO volunteer) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        // TODO: Esto aun no funciona
        //return ResponseEntity.ok(repository.save(Volunteer.fromDTO(volunteer)).toDTO());
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/volunteer/{id}")
    public ResponseEntity<Void>  deleteVolunteer(@PathVariable String id) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/volunteer")
    public ResponseEntity<VolunteerDTO>  updateVolunteer(@RequestBody VolunteerDTO volunteer) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(volunteer.getDni())) {
            //return ResponseEntity.ok(repository.save(Volunteer.fromDTO(volunteer)).toDTO());
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
