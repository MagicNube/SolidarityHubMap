package org.pinguweb.backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pinguweb.backend.model.enums.Status;
import org.pinguweb.backend.model.enums.TaskType;
import org.pinguweb.backend.model.enums.UrgencyLevel;

@Getter
@Entity
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "ID"
)
public class Need {
    @ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("ID")
    private int ID;

    @Setter
    @ManyToOne
    @JoinColumn(name = "affected_dni")
    private Affected affected;

    @Setter
    private String description;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UrgencyLevel urgency;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "location_id")
    @Setter
    private GPSCoordinates location;

    @ManyToOne
    @JoinColumn(name = "catastrophe_id")
    @Setter
    private Catastrophe catastrophe;

    @Setter
    @Enumerated(EnumType.STRING)
    private Status status;

    @Setter
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;


    public Need(Affected affected, String description, UrgencyLevel urgency, TaskType needType, GPSCoordinates location, Catastrophe catastrophe) {
        this.affected = affected;
        this.description = description;
        this.urgency = urgency;
        this.taskType = needType;
        this.location = location;
        this.affected.addNeed(this);
        this.catastrophe = catastrophe;
    }
}
