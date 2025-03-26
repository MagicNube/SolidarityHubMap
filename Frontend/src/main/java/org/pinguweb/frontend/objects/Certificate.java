package org.pinguweb.frontend.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class Certificate {

    private int id;

    @Setter
    private String title;
    @Setter
    private String description;

    @Setter
    private Volunteer volunteer;

    public Certificate(String title, String description, Volunteer volunteer){
        this.title = title;
        this.description = description;
        this.volunteer = volunteer;
    }
}
