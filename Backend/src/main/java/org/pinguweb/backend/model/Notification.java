package org.pinguweb.backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Setter
    private String title;
    @Setter
    private String body;

    @Setter
    @OneToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Setter
    @ManyToOne
    @JoinColumn(name = "volunteer_dni")
    private Volunteer volunteer;

    public Notification(String title, String body, Task task, Volunteer volunteer) {
        this.title = title;
        this.body = body;
        this.task = task;
        this.volunteer = volunteer;
    }
}
