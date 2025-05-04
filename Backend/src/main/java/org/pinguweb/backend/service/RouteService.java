package org.pinguweb.backend.service;

import lombok.Getter;
import org.pinguweb.backend.model.Route;
import org.pinguweb.backend.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService {
    @Getter
    private final RouteRepository routeRepository;

    public RouteService(RouteRepository  routeRepository) {this.routeRepository = routeRepository;}
    public List<Route> findAll() {return this.routeRepository.findAll();}
    public Optional<Route> findByID(Integer ID){return this.routeRepository.findById(ID);}
    public void delete(Route route){this.routeRepository.delete(route);}
    public Route saveRoute(Route route){return this.routeRepository.save(route);}
}