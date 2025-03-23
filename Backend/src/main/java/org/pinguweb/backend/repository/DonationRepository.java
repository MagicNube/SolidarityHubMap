package org.pinguweb.backend.repository;

import org.pinguweb.backend.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Integer> {
}
