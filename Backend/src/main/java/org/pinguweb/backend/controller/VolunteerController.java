package org.pinguweb.backend.controller;

import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.model.Volunteer;
import org.pinguweb.backend.repository.VolunteerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VolunteerController {
    
    VolunteerRepository repository;

    @GetMapping("/volunteer")
    public ResponseEntity<List<Volunteer>> getAll(){
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        List<Volunteer> volunteers = repository.findAll();
        return ResponseEntity.ok(volunteers);
    }

    @GetMapping("/volunteer/{id}")
    public ResponseEntity<Volunteer> getVolunteer(@PathVariable String id) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            return ResponseEntity.ok(repository.getReferenceById(id));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/volunteer")
    public ResponseEntity<Volunteer> addVolunteer(@RequestBody Volunteer volunteer) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        return ResponseEntity.ok(repository.save(volunteer));
    }

    @DeleteMapping("/volunteer/{id}")
    public ResponseEntity<Void>  deleteVolunteer(@PathVariable String id) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/volunteer")
    public ResponseEntity<Volunteer>  updateVolunteer(@RequestBody Volunteer volunteer) {
        if (!ServerException.isServerConnected(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(volunteer.getDNI())) {
            return ResponseEntity.ok(repository.save(volunteer));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
