package org.pinguweb.backend.controller;

import org.pingu.domain.DTO.ResourceDTO;
import org.pingu.domain.DTO.factories.BackendDTOFactory;
import org.pingu.domain.DTO.factories.ModelDTOFactory;
import org.pingu.persistence.service.ResourceService;
import org.pinguweb.backend.controller.common.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ResourceController {

    @Autowired
    ResourceService service;
    @Autowired
    BackendDTOFactory factory;
    @Autowired
    ModelDTOFactory modelFactory;

    @Async
    @GetMapping("/resources")
    public CompletableFuture<ResponseEntity<List<ResourceDTO>>> getAll(){
        if (ServerException.isServerClosed(service.getResourceRepository())){
            return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());
        }

        List<ResourceDTO> resources = service.findAll().stream()
                .map(factory::createDTO)
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(ResponseEntity.ok(resources));
    }

}