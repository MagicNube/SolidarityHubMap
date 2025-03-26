package org.pinguweb.frontend.DTO;

import lombok.Data;

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
}
