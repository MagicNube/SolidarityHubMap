package org.pinguweb.backend.controller;

import org.pingu.domain.DTO.DonationDTO;
import org.pingu.domain.DTO.factories.BackendDTOFactory;
import org.pingu.domain.DTO.factories.ModelDTOFactory;
import org.pingu.persistence.model.Donation;
import org.pingu.persistence.service.DonationService;
import org.pinguweb.backend.controller.common.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class DonationController {

    @Autowired
    DonationService service;
    @Autowired
    BackendDTOFactory factory;
    @Autowired
    ModelDTOFactory modelFactory;

    @Async
    @GetMapping("/donations")
    public CompletableFuture<ResponseEntity<List<DonationDTO>>> getAll() {
        if (ServerException.isServerClosed(service.getDonationRepository())) {
            return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());
        }

        List<DonationDTO> donations = service.findAll().stream()
                .map(factory::createDTO)
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(ResponseEntity.ok(donations));
    }
}