package org.pinguweb.frontend.objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pinguweb.frontend.objects.enums.NeedType;
import org.pinguweb.frontend.objects.enums.Priority;
import org.pinguweb.frontend.objects.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

@Getter

@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Task {
    
    
    private int id;

    @Setter
    
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
    private NeedType type;

    @Setter
    
    private Priority priority;

    @Setter
    
    private Status status;

    @Setter
    
    private List<Volunteer> volunteers;

    @Setter
    private Zone zone;

    @Setter
    private Notification notification;


    public Task(Need need, String taskName, String taskDescription, LocalDateTime startTimeDate,
                LocalDateTime estimatedEndTimeDate, Priority priority, Status status, Volunteer volunteer) {
        this.need = List.of(need);
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.startTimeDate = startTimeDate;
        this.estimatedEndTimeDate = estimatedEndTimeDate;
        this.priority = priority;
        this.status = status;
        this.volunteers= List.of(volunteer);
        volunteer.getTasks().add(this);
        this.type = need.getNeedType();
    }
}
