package org.pinguweb.frontend.DTO;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

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
    private List<String> volunteers;
    private Integer zone;
}
