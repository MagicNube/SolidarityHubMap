package org.pinguweb.backend.service;

import org.pinguweb.backend.model.Skill;
import org.pinguweb.backend.repository.SkillRepository;
import org.springframework.stereotype.Service;

@Service
public class SkillService {
    private final SkillRepository skillRepository;
    public SkillService(SkillRepository skillRepository) {this.skillRepository = skillRepository;}
    public Skill saveSkill(Skill skill) {return skillRepository.save(skill);}
}
