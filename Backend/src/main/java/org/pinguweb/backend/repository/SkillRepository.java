package org.pinguweb.backend.repository;

import org.pinguweb.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, String> {
}
