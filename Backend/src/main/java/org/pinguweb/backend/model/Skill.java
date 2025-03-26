package org.pinguweb.backend.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pinguweb.backend.DTO.SkillDTO;

@Getter
@Entity
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "name"
)
public class Skill {
    @Id
    private String name;

    public Skill(String name) {
        this.name = name;
    }

    public SkillDTO toDTO() {
        return new SkillDTO(this);
    }

    public static Skill fromDTO(SkillDTO dto) {
        Skill skill = new Skill();
        skill.name = dto.getName();
        return skill;
    }
}

