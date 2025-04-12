package org.pinguweb.backend.repository;

import org.pinguweb.backend.model.Catastrophe;
import org.pinguweb.backend.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CatastropheRepository extends JpaRepository<Catastrophe, Integer> {
    Optional<Catastrophe> findByID(Integer id);
}
