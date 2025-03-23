package org.pinguweb.backend.repository;

import org.pinguweb.backend.model.Need;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NeedRepository extends JpaRepository<Need, Integer> {
}
