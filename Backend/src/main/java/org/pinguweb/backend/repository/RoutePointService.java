package org.pinguweb.backend.repository;

import org.pinguweb.backend.model.Resource;
import org.pinguweb.backend.model.RoutePoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutePointService extends JpaRepository<RoutePoint, Integer> {
}
