package org.pinguweb.frontend.objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
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
