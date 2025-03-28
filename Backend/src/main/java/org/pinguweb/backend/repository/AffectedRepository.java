package org.pinguweb.backend.repository;

import org.pinguweb.backend.model.Affected;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AffectedRepository extends JpaRepository<Affected, String> {
}
