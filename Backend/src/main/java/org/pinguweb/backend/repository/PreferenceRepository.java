package org.pinguweb.backend.repository;

import org.pinguweb.model.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceRepository extends JpaRepository<Preference, Integer> {
}
