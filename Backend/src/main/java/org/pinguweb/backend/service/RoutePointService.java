package org.pinguweb.backend.service;

import lombok.Getter;
import org.pinguweb.backend.model.Route;
import org.pinguweb.backend.model.RoutePoint;
import org.pinguweb.backend.repository.RoutePointRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoutePointService {
    @Getter
    private final RoutePointRepository routePointRepository;
    public RoutePointService(RoutePointRepository routePointRepository) {this.routePointRepository = routePointRepository;}
    public RoutePoint saveRoutePoint(RoutePoint routePoint) {return routePointRepository.save(routePoint);}
    public List<RoutePoint> findAll(){return routePointRepository.findAll();}
    public Optional<RoutePoint> findByID(Integer ID){return routePointRepository.findById(ID);}
    public void delete(RoutePoint routePoint){this.routePointRepository.delete(routePoint);}
}
