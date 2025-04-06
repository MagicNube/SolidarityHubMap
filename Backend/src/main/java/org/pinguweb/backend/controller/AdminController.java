package org.pinguweb.backend.controller;

import org.pinguweb.DTO.AdminDTO;
import org.pinguweb.backend.DTO.BackendDTOFactory;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.repository.AdminRepository;
import org.pinguweb.backend.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class AdminController {
    @Autowired
    AdminRepository repository;

    @Async
    @GetMapping("/admin/{id}")
    public CompletableFuture<ResponseEntity<AdminDTO>> getAdmin(@PathVariable String id) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();

        try {
            if (repository.existsById(id)) {
                return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createAdminDTO(repository.getReferenceById(id))));
            } else {
                return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
            }
        }
        catch (Exception e) {
            return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());
        }
    }

    @Async
    @PostMapping("/admin")
    public CompletableFuture<ResponseEntity<AdminDTO>> addAdmin(@RequestBody AdminDTO admin) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        // TODO: no funciona aun
        //return ResponseEntity.ok(repository.save(Admin.fromDTO(admin)).toDTO());

        return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
    }

    @Async
    @DeleteMapping("/admin/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteAdmin(@PathVariable String id) {
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
    @PutMapping("/admin")
    public CompletableFuture<ResponseEntity<AdminDTO>> updateAdmin(@RequestBody AdminDTO admin) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        //TODO: Esto no funciona aun
        if (repository.existsById(admin.getDni())) {
            //return ResponseEntity.ok(repository.save(Admin.fromDTO(admin)).toDTO());
        }
        else {
        }
        return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
    }

    @Async
    @GetMapping("/admin/login/")
    public CompletableFuture<ResponseEntity<Void>> login(@RequestBody AdminDTO adminDto) {
        if (ServerException.isServerClosed(repository)){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        if (repository.existsById(adminDto.getDni())) {
            Admin admin = repository.getReferenceById(adminDto.getDni());
            if (admin.getPassword().equals(adminDto.getPassword()))
            {
                return CompletableFuture.completedFuture(ResponseEntity.accepted().build());
            }

            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
        return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
    }
}
