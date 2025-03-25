package org.pinguweb.frontend.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Skill {

    private String name;

    public Skill(String name) {
        this.name = name;
    }
}

