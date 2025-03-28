package org.pinguweb.backend.repository;

import org.pinguweb.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository extends JpaRepository<Certificate, Integer> {
}
