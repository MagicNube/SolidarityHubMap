package org.pinguweb.backend.repository;

import org.pinguweb.model.GPSCoordinates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GPSCoordinatesRepository extends JpaRepository<GPSCoordinates, Integer> {
}
