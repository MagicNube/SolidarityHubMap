package org.pinguweb.DTO;

import lombok.Data;
import org.pinguweb.model.Skill;

@Data
public class SkillDTO {
    private String name;

    public SkillDTO(Skill skill) {
        this.name = skill.getName();
    }
}
