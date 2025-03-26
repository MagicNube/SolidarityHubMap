package org.pinguweb.frontend.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Preference {

    private String name;

    public Preference(String name) {
        this.name = name;
    }
}
