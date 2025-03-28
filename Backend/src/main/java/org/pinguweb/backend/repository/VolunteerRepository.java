package org.pinguweb.backend.repository;

import org.pinguweb.backend.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerRepository extends JpaRepository<Volunteer, String> {
}
