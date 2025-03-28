package org.pinguweb.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.pinguweb.model.Need;
import org.pinguweb.model.Task;
import org.yaml.snakeyaml.util.Tuple;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class TaskDTO implements DTO{
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
    private Tuple<Double, Double> coordinates;

    public TaskDTO(Task task) {
        this.taskName = task.getTaskName();
        this.taskDescription = task.getTaskDescription();
        this.startTimeDate = task.getStartTimeDate();
        this.estimatedEndTimeDate = task.getEstimatedEndTimeDate();

        if (task.getNeed() != null) {
            this.need = task.getNeed().stream().map(Need::getId).collect(Collectors.toList());
        }
        if (task.getPriority() != null) {
            this.priority = task.getPriority().name();
        }
        if (task.getStatus() != null) {
            this.status = task.getStatus().name();
        }
        if (task.getType() != null) {
            this.type = task.getType().name();
        }
        if (task.getZone() != null) {
            this.zone = task.getZone().getId();
        }
    }
}
