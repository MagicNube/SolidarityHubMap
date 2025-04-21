package org.pinguweb.backend.controller;

import org.pingu.domain.DTO.RoutePointDTO;
import org.pinguweb.backend.DTO.BackendDTOFactory;
import org.pinguweb.backend.DTO.ModelDTOFactory;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.model.RoutePoint;
import org.pinguweb.backend.service.RoutePointService;
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

        List<RoutePointDTO> routePoints = service.findAll().stream().map(factory::createRoutePointDTO).collect(Collectors.toList());

        return CompletableFuture.completedFuture(ResponseEntity.ok(routePoints));
    }

    @Async
    @GetMapping("/routepoint/{ID}")
    public CompletableFuture<ResponseEntity<RoutePointDTO>> getRoutePoints(@PathVariable Integer ID) {
        if (ServerException.isServerClosed(service.getRoutePointRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        Optional<RoutePoint> res = service.findByID(ID);
        if (res.isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createRoutePointDTO(res.get())));
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

        return CompletableFuture.completedFuture(ResponseEntity.ok(dtoFactory.createRoutePointDTO(service.saveRoutePoint(factory.createFromDTO(routePointDTO)))));
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

            return CompletableFuture.completedFuture(ResponseEntity.ok(dtoFactory.createRoutePointDTO(service.saveRoutePoint(factory.createFromDTO(routePointDTO)))));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }
}
