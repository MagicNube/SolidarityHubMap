package org.pinguweb.backend.repository;

import org.pinguweb.model.Catastrophe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatastropheRepository extends JpaRepository<Catastrophe, Integer> {
}
