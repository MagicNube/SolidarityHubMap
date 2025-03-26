package org.pinguweb.backend.DTO;

import lombok.Data;
import org.pinguweb.backend.model.Skill;
import org.pinguweb.backend.model.Task;

@Data
public class SkillDTO {
    private String name;

    public SkillDTO(Skill skill) {
        this.name = skill.getName();
    }
}
