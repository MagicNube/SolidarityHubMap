package org.pinguweb.frontend.objects;

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
public class Task {

    private int id;

    @Setter
    private List<Need> needs;

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


    public Task(List <Need> needs, String taskName, String taskDescription, LocalDateTime startTimeDate,
                LocalDateTime estimatedEndTimeDate, Priority priority, Status status, List<Volunteer> volunteers) {
        this.needs = needs;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.startTimeDate = startTimeDate;
        this.estimatedEndTimeDate = estimatedEndTimeDate;
        this.priority = priority;
        this.status = status;
        this.volunteers= volunteers;
        this.type = needs.get(0).getNeedType();
    }
}
