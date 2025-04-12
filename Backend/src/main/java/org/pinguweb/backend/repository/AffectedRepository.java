package org.pinguweb.backend.repository;

import org.pinguweb.backend.model.Admin;
import org.pinguweb.backend.model.Affected;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AffectedRepository extends JpaRepository<Affected, String> {
    Optional<Affected> findByDni(String username);
}
