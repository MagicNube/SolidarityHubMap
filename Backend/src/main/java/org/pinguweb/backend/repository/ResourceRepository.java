package org.pinguweb.backend.repository;

import org.pinguweb.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Integer> {
}
