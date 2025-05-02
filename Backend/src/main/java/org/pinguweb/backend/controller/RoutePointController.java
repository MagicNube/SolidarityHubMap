package org.pinguweb.backend.controller;

import org.pingu.domain.DTO.RoutePointDTO;
import org.pingu.domain.DTO.factories.BackendDTOFactory;
import org.pingu.domain.DTO.factories.ModelDTOFactory;
import org.pingu.persistence.model.RoutePoint;
import org.pingu.persistence.service.RoutePointService;
import org.pinguweb.backend.controller.common.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RoutePointController {

    @Autowired
    RoutePointService service;

    @Async
    @GetMapping("/routepoint")
    public CompletableFuture<ResponseEntity<List<RoutePointDTO>>> getAll(){
        if (ServerException.isServerClosed(service.getRoutePointRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}
        BackendDTOFactory factory = new BackendDTOFactory();

        List<RoutePointDTO> routePoints = service.findAll().stream().map(factory::createDTO).collect(Collectors.toList());

        return CompletableFuture.completedFuture(ResponseEntity.ok(routePoints));
    }

    @Async
    @GetMapping("/routepoint/{ID}")
    public CompletableFuture<ResponseEntity<RoutePointDTO>> getRoutePoints(@PathVariable Integer ID) {
        if (ServerException.isServerClosed(service.getRoutePointRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        Optional<RoutePoint> res = service.findByID(ID);
        if (res.isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createDTO(res.get())));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @PostMapping("/routepoint")
    public CompletableFuture<ResponseEntity<RoutePointDTO>> addNeed(@RequestBody  RoutePointDTO routePointDTO) {
        if (ServerException.isServerClosed(service.getRoutePointRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        ModelDTOFactory factory = new ModelDTOFactory();
        BackendDTOFactory dtoFactory = new BackendDTOFactory();

        return CompletableFuture.completedFuture(ResponseEntity.ok(dtoFactory.createDTO(service.saveRoutePoint(factory.createFromDTO(routePointDTO)))));
    }

    @Async
    @DeleteMapping("/routepoint/{ID}")
    public CompletableFuture<ResponseEntity<Void>> deleteNeed(@PathVariable int ID) {
        if (ServerException.isServerClosed(service.getRoutePointRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        Optional<RoutePoint> res = service.findByID(ID);
        if (res.isPresent()) {
            service.delete(res.get());
            return CompletableFuture.completedFuture(ResponseEntity.ok().build());
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @PutMapping("/routepoint")
    public CompletableFuture<ResponseEntity<RoutePointDTO>> updateNeed(@RequestBody RoutePointDTO routePointDTO) {
        if (ServerException.isServerClosed(service.getRoutePointRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        Optional<RoutePoint> res = service.findByID(routePointDTO.getID());
        if (res.isPresent()) {
            ModelDTOFactory factory = new ModelDTOFactory();
            BackendDTOFactory dtoFactory = new BackendDTOFactory();

            return CompletableFuture.completedFuture(ResponseEntity.ok(dtoFactory.createDTO(service.saveRoutePoint(factory.createFromDTO(routePointDTO)))));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }
}
