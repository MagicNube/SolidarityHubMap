package org.pinguweb.backend.controller;

import org.pingu.domain.DTO.AdminDTO;
import org.pingu.domain.DTO.factories.BackendDTOFactory;
import org.pingu.domain.DTO.factories.ModelDTOFactory;
import org.pingu.persistence.model.Admin;
import org.pingu.persistence.service.AdminService;
import org.pinguweb.backend.controller.common.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class AdminController {
    @Autowired
    AdminService service;
    @Autowired
    BackendDTOFactory factory;
    @Autowired
    ModelDTOFactory dtoFactory;

    @Async
    @GetMapping("/admins/{ID}")
    public CompletableFuture<ResponseEntity<AdminDTO>> getAdmin(@PathVariable String ID) {
        if (ServerException.isServerClosed(service.getAdminRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        try {
            Optional<Admin> res = service.findByDni(ID);
            if (res.isPresent()) {
                return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createDTO(res.get())));
            } else {
                return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
            }
        }
        catch (Exception e) {
            return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());
        }
    }
}
