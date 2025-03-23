package org.pinguweb.backend.repository;

import org.pinguweb.backend.model.GPSCoordinates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GPSCoordinatesRepository extends JpaRepository<GPSCoordinates, Integer> {
}
