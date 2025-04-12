package org.pinguweb.backend.repository;

import org.pinguweb.backend.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Integer>{
}
