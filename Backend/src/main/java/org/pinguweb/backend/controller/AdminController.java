package org.pinguweb.backend.controller;

import org.pinguweb.DTO.AdminDTO;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.repository.AdminRepository;
import org.pinguweb.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AdminController {
    @Autowired
    AdminRepository repository;

    @GetMapping("/admin/{id}")
    public ResponseEntity<AdminDTO> getAdmin(@PathVariable String id) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            return ResponseEntity.ok(repository.getReferenceById(id).toDTO());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<AdminDTO> addAdmin(@RequestBody AdminDTO admin) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        // TODO: no funciona aun
        return ResponseEntity.ok(repository.save(Admin.fromDTO(admin)).toDTO());
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void>  deleteAdmin(@PathVariable String id) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/admin")
    public ResponseEntity<AdminDTO>  updateAdmin(@RequestBody AdminDTO admin) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        //TODO: Esto no funciona aun
        if (repository.existsById(admin.getDni())) {
            return ResponseEntity.ok(repository.save(Admin.fromDTO(admin)).toDTO());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/admin/login/")
    public ResponseEntity<Void> login(@RequestBody AdminDTO adminDto) {
        if (ServerException.isServerClosed(repository)){return ResponseEntity.internalServerError().build();}

        if (repository.existsById(adminDto.getDni())) {
            Admin admin = repository.getReferenceById(adminDto.getDni());
            if (admin.getPassword().equals(adminDto.getPassword()))
            {
                return ResponseEntity.accepted().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
