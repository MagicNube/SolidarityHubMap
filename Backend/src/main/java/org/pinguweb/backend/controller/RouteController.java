package org.pinguweb.backend.controller;

import org.pingu.domain.DTO.RouteDTO;
import org.pingu.domain.DTO.RoutePointDTO;
import org.pinguweb.backend.DTO.BackendDTOFactory;
import org.pinguweb.backend.DTO.ModelDTOFactory;
import org.pinguweb.backend.controller.common.ServerException;
import org.pinguweb.backend.model.Route;
import org.pinguweb.backend.model.RoutePoint;
import org.pinguweb.backend.service.RoutePointService;
import org.pinguweb.backend.service.RouteService;
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
public class RouteController {
    @Autowired
    RouteService service;
    @Autowired
    RoutePointService routePointService;

    @Async
    @GetMapping("/route")
    public CompletableFuture<ResponseEntity<List<RouteDTO>>> getAll(){
        if (ServerException.isServerClosed(service.getRouteRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();

        List<RouteDTO> routes = service.findAll().stream().map(factory::createRouteDTO).collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok(routes));
    }

    @Async
    @GetMapping("/route/{ID}")
    public CompletableFuture<ResponseEntity<RouteDTO>> getRoute(@PathVariable int ID) {
        if (ServerException.isServerClosed(service.getRouteRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        Optional<Route> res = service.findByID(ID);
        if (res.isPresent()) {
            return CompletableFuture.completedFuture(ResponseEntity.ok(factory.createRouteDTO(res.get())));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

    @Async
    @GetMapping("/route/{ID}/points")
    public CompletableFuture<ResponseEntity<List<RoutePointDTO>>> getRoutePoints(@PathVariable int ID) {
        if (ServerException.isServerClosed(service.getRouteRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        BackendDTOFactory factory = new BackendDTOFactory();
        try {
            Optional<Route> res = service.findByID(ID);
            if (res.isPresent()) {
                List<RoutePoint> points = res.get().getPoints().stream().filter(x -> x.getRoute().getID() == ID).toList();
                return CompletableFuture.completedFuture(ResponseEntity.ok(points.stream().map(factory::createRoutePointDTO).toList()));
            }
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Async
    @PostMapping("/route")
    public CompletableFuture<ResponseEntity<RouteDTO>> addRoute(@RequestBody RouteDTO route) {
        if (ServerException.isServerClosed(service.getRouteRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        ModelDTOFactory factory = new ModelDTOFactory();
        BackendDTOFactory dtoFactory = new BackendDTOFactory();

        return CompletableFuture.completedFuture(ResponseEntity.ok(dtoFactory.createRouteDTO(service.saveRoute(factory.createFromDTO(route)))));
    }

    @Async
    @DeleteMapping("/route/{ID}")
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
    @PutMapping("/route")
    public CompletableFuture<ResponseEntity<RouteDTO>> updateRoute(@RequestBody RouteDTO route) {
        if (ServerException.isServerClosed(service.getRouteRepository())){return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());}

        Optional<Route> res = service.findByID(route.getID());
        if (res.isPresent()) {
            ModelDTOFactory factory = new ModelDTOFactory();
            BackendDTOFactory dtoFactory = new BackendDTOFactory();

            return CompletableFuture.completedFuture(ResponseEntity.ok(dtoFactory.createRouteDTO(service.saveRoute(factory.createFromDTO(route)))));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
    }

}
