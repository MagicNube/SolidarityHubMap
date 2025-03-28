package org.pinguweb.backend.repository;

import org.pinguweb.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Integer> {
}
