package org.pinguweb.backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter
    private String title;
    @Setter
    private String description;

    @Setter
    @ManyToOne
    @JoinColumn(name = "volunteer_dni")
    private Volunteer volunteer;

    public Certificate(String title, String description, Volunteer volunteer){
        this.title = title;
        this.description = description;
        this.volunteer = volunteer;
    }
}
