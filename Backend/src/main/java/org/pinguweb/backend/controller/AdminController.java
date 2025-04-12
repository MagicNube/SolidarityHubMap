package org.pinguweb.backend.controller;

import org.pinguweb.DTO.AdminDTO;
import org.pinguweb.backend.DTO.BackendDTOFactory;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.model.Admin;
import org.pinguweb.backend.repository.AdminRepository;
import org.pinguweb.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class AdminController {
    @Autowired
    AdminService service;

    @Async
    @GetMapping("/admin/{id}")
    public CompletableFuture<ResponseEntity<AdminDTO>> getAdmin(@PathVariable String id) {
        if (ServerException.isServerClosed(service.getAdminRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}
        BackendDTOFactory factory = new BackendDTOFactory();

        try {
            Optional<Admin> res = service.findByDni(id);
            if (res.isPresent()) {
                return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createAdminDTO(res.get())));
            } else {
                return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
            }
        }
        catch (Exception e) {
            return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());
        }
    }
}
