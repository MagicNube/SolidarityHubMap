package org.pinguweb.DTO;

import lombok.Data;
import org.pinguweb.model.Need;
import org.pinguweb.model.Task;
import org.yaml.snakeyaml.util.Tuple;

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
    private Tuple<Double, Double> coordinates;

    public TaskDTO(Task task) {
        this.need = task.getNeed().stream().map(Need::getId).collect(Collectors.toList());
        this.taskName = task.getTaskName();
        this.taskDescription = task.getTaskDescription();
        this.startTimeDate = task.getStartTimeDate();
        this.estimatedEndTimeDate = task.getEstimatedEndTimeDate();
        this.priority = task.getPriority().name();
        this.status = task.getStatus().name();
        this.type = task.getType().name();
        this.coordinates = new Tuple<>(task.getGpsCoordinates().getLatitude(), task.getGpsCoordinates().getLongitude());
    }
}
