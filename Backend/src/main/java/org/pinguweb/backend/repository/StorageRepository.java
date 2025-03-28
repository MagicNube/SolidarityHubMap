package org.pinguweb.backend.repository;

import org.pinguweb.model.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<Storage, Integer> {
}
