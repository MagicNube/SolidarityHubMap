package org.pinguweb.backend.DTO;

import lombok.Data;
import org.pinguweb.backend.model.Need;
import org.pinguweb.backend.model.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TaskDTO {
    private int id;
    private List<Integer> need;
    private String taskName;
    private String taskDescription;
    private LocalDateTime startTimeDate;
    private LocalDateTime estimatedEndTimeDate;
    private String type;
    private String priority;
    private String status;
    private Integer zone;

    public TaskDTO(Task task) {
        this.need = task.getNeed().stream().map(Need::getId).collect(Collectors.toList());
        this.taskName = task.getTaskName();
        this.taskDescription = task.getTaskDescription();
        this.startTimeDate = task.getStartTimeDate();
        this.estimatedEndTimeDate = task.getEstimatedEndTimeDate();
        this.priority = task.getPriority().name();
        this.status = task.getStatus().name();
        this.type = task.getType().name();
    }
}
