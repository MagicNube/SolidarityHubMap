package org.pinguweb.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.pinguweb.model.Skill;

@NoArgsConstructor
@Data
public class SkillDTO implements DTO{
    private String name;

    public SkillDTO(Skill skill) {
        this.name = skill.getName();
    }
}
