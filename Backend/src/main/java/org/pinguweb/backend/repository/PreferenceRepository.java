package org.pinguweb.backend.repository;

import org.pinguweb.backend.model.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceRepository extends JpaRepository<Preference, Integer> {
}
