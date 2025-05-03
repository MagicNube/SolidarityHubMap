package org.pinguweb.backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pingu.domain.enums.Priority;
import org.pingu.domain.enums.Status;
import org.pingu.domain.enums.TaskType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "ID"
)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    @Setter
    @OneToMany(mappedBy = "task")
    private List<Need> need;

    @Setter
    private String taskName;

    @Setter
    private String taskDescription;

    @Setter
    private LocalDateTime startTimeDate;

    @Setter
    private LocalDateTime estimatedEndTimeDate;

    @Setter
    @Enumerated(EnumType.STRING)
    private TaskType type;

    @Setter
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Setter
    @Enumerated(EnumType.STRING)
    private Status status;

    @Setter
    @ManyToMany(mappedBy = "tasks")
    private List<Volunteer> volunteers;

    @Setter
    @ManyToOne(cascade = CascadeType.ALL)
    private Zone zone;
  
    public Task(Need need, String taskName, String taskDescription, LocalDateTime startTimeDate,
                LocalDateTime estimatedEndTimeDate, Priority priority, Status status, Volunteer volunteer, GPSCoordinates gpsCoordinates) {
        this.need = List.of(need);
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.startTimeDate = startTimeDate;
        this.estimatedEndTimeDate = estimatedEndTimeDate;
        this.priority = priority;
        this.status = status;
        this.volunteers= List.of(volunteer);
        volunteer.getTasks().add(this);
        this.type = need.getTaskType();
    }
}
