package org.pinguweb.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.RouteDTO;
import org.pingu.domain.DTO.RoutePointDTO;
import org.pingu.domain.DTO.factories.BackendDTOFactory;
import org.pingu.domain.DTO.factories.ModelDTOFactory;
import org.pingu.persistence.model.Route;
import org.pingu.persistence.model.RoutePoint;
import org.pingu.persistence.service.RouteService;
import org.pinguweb.backend.controller.common.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
public class RouteController {
    @Autowired
    RouteService service;
    @Autowired
    BackendDTOFactory factory;
    @Autowired
    ModelDTOFactory dtoFactory;

    @Async
    @GetMapping("/routes")
    public CompletableFuture<ResponseEntity<List<RouteDTO>>> getAll(){
        if (ServerException.isServerClosed(service.getRouteRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        List<RouteDTO> routes = service.findAll().stream().map(factory::createDTO).collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok(routes));
    }

    @Async
    @GetMapping("/routes/{ID}")
    public CompletableFuture<ResponseEntity<RouteDTO>> getRoute(@PathVariable int ID) {
        if (ServerException.isServerClosed(service.getRouteRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        Optional<Route> res = service.findByID(ID);
        if (res.isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createDTO(res.get())));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @GetMapping("/routes/{ID}/points")
    public CompletableFuture<ResponseEntity<List<RoutePointDTO>>> getRoutePoints(@PathVariable int ID) {
        if (ServerException.isServerClosed(service.getRouteRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        try {
            Optional<Route> res = service.findByID(ID);
            if (res.isPresent()) {
                List<RoutePoint> points = res.get().getPoints().stream().toList();
                return CompletableFuture.completedFuture(ResponseEntity.ok(points.stream().map(factory::createDTO).toList()));
            }
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
        catch (Exception e){
            log.error(e.getMessage(),  Arrays.stream(e.getStackTrace()).toArray());
        }
        return null;
    }

    @Async
    @PostMapping("/routes")
    public CompletableFuture<ResponseEntity<RouteDTO>> addRoute(@RequestBody RouteDTO route) {
        if (ServerException.isServerClosed(service.getRouteRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createDTO(service.saveRoute(dtoFactory.createFromDTO(route)))));
    }

    @Async
    @DeleteMapping("/routes/{ID}")
    public CompletableFuture<ResponseEntity<Void>> deleteRoute(@PathVariable int ID) {
        if (ServerException.isServerClosed(service.getRouteRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        Optional<Route> res = service.findByID(ID);
        if (res.isPresent()) {
            service.delete(res.get());
            return CompletableFuture.completedFuture(ResponseEntity.ok().build());
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @PutMapping("/routes/{ID}")
    public CompletableFuture<ResponseEntity<RouteDTO>> updateRoute(@RequestBody RouteDTO route, @PathVariable int ID) {
        if (ServerException.isServerClosed(service.getRouteRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        Optional<Route> res = service.findByID(ID);
        if (res.isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createDTO(service.saveRoute(dtoFactory.createFromDTO(route)))));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

}
