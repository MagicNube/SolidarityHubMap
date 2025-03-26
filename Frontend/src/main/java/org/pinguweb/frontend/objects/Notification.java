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
public class Notification {
    
    
    private int id;
    @Setter
    private String title;
    @Setter
    private String body;

    @Setter
    private Task task;

    @Setter
    private Volunteer volunteer;

    public Notification(String title, String body, Task task, Volunteer volunteer) {
        this.title = title;
        this.body = body;
        this.task = task;
        this.volunteer = volunteer;
    }
}
