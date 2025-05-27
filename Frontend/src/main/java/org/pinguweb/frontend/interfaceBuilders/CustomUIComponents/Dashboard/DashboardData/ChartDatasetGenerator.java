package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData;

import com.storedobject.chart.Color;
import org.pingu.domain.DTO.NeedDTO;
import org.pingu.domain.DTO.ResourceDTO;
import org.pingu.domain.DTO.TaskDTO;
import org.pingu.domain.DTO.VolunteerDTO;
import org.pingu.domain.enums.ResourceType;
import org.pingu.domain.enums.TaskType;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;

public class ChartDatasetGenerator {
    public Color generateColorForType(String type) {
        return generateColorPalette(1)[0];
    }

    public List<List<TaskDTO>> calculateTaskPerUrgency(Integer[] taskPerUrgency, List<TaskDTO> tasks) {
        Arrays.fill(taskPerUrgency, 0);
        List<List<TaskDTO>> tasksByUrgency = new ArrayList<>(3);


        for (int i = 0; i < 3; i++) {
            tasksByUrgency.add(new ArrayList<>());
        }

        ChronoLocalDateTime today = LocalDate.now().atStartOfDay();

        for (TaskDTO task : tasks) {
            if (task.getEstimatedEndTimeDate() != null && !task.getEstimatedEndTimeDate().isAfter(today)) {
                switch (task.getPriority().toUpperCase()) {
                    case "URGENT":
                        taskPerUrgency[0]++;
                        tasksByUrgency.get(0).add(task);
                        break;
                    case "MODERATE":
                        taskPerUrgency[1]++;
                        tasksByUrgency.get(1).add(task);
                        break;
                    case "LOW":
                        taskPerUrgency[2]++;
                        tasksByUrgency.get(2).add(task);
                        break;
                    default:
                        break;
                }
            }
        }

        return tasksByUrgency;
    }


    public List<List<ResourceDTO>> calculateResourcesByType(Integer[] resourcesByType, List<ResourceDTO> resources) {
        Arrays.fill(resourcesByType, 0);

        int typesCount = ResourceType.values().length;
        List<List<ResourceDTO>> resourcesByTypeList = new ArrayList<>(typesCount);
        for (int i = 0; i < typesCount; i++) {
            resourcesByTypeList.add(new ArrayList<>());
        }

        for (ResourceDTO resource : resources) {
            if (resource.getType() != null) {
                ResourceType type = ResourceType.valueOf(resource.getType());
                int idx = type.ordinal();
                resourcesByType[idx]++;
                resourcesByTypeList.get(idx).add(resource);
            }
        }

        return resourcesByTypeList;
    }

    public List<List<NeedDTO>> calculateNeedsPerType(Integer[] needsByTaskType, List<NeedDTO> needs) {
        Arrays.fill(needsByTaskType, 0);

        int typesCount = TaskType.values().length;
        List<List<NeedDTO>> needsPerType = new ArrayList<>(typesCount);
        for (int i = 0; i < typesCount; i++) {
            needsPerType.add(new ArrayList<>());
        }

        for (NeedDTO need : needs) {
            if (!"FINISHED".equals(need.getStatus())
                    && need.getNeedType() != null) {

                TaskType type = TaskType.valueOf(need.getNeedType());
                int idx = type.ordinal();

                needsByTaskType[idx]++;
                needsPerType.get(idx).add(need);
            }
        }
        return needsPerType;
    }

    public List<List<TaskDTO>> calculateUnfinishedTasksByType(Integer[] unfinishedTasks, List<TaskDTO> tasks) {
        Arrays.fill(unfinishedTasks, 0);
        int typesCount = TaskType.values().length;
        List<List<TaskDTO>> tasksByType = new ArrayList<>(typesCount);
        for (int i = 0; i < typesCount; i++) {
            tasksByType.add(new ArrayList<>());
        }

        for (TaskDTO task : tasks) {
            if (task.getStatus() != null
                    && !"FINISHED".equalsIgnoreCase(task.getStatus().trim())
                    && task.getType() != null) {
                TaskType type = TaskType.valueOf(task.getType());
                int idx = type.ordinal();
                unfinishedTasks[idx]++;
                tasksByType.get(idx).add(task);
            }
        }

        return tasksByType;
    }

    public List<List<TaskDTO>> calculateCompletedTasksPerType(Integer[] completedTasks, List<TaskDTO> tasks) {
        Arrays.fill(completedTasks, 0);
        int typesCount = TaskType.values().length;
        List<List<TaskDTO>> tasksByType = new ArrayList<>(typesCount);
        for (int i = 0; i < typesCount; i++) {
            tasksByType.add(new ArrayList<>());
        }
        for (TaskDTO task : tasks) {
            if ("FINISHED".equals(task.getStatus())
                    && task.getType() != null) {
                TaskType type = TaskType.valueOf(task.getType());
                int idx = type.ordinal();
                completedTasks[idx]++;
                tasksByType.get(idx).add(task);
            }
        }
        return tasksByType;
    }

    public List<List<VolunteerDTO>> calculateVolunteersByTaskType(Integer[] volunteersCountByType, List<VolunteerDTO> volunteers) {
        Arrays.fill(volunteersCountByType, 0);
        int typesCount = TaskType.values().length;
        List<List<VolunteerDTO>> volunteersByType = new ArrayList<>(typesCount);
        for (int i = 0; i < typesCount; i++) {
            volunteersByType.add(new ArrayList<>());
        }
        for (VolunteerDTO v : volunteers) {
            if (v.getTaskPreferences() != null) {
                for (String pref : v.getTaskPreferences()) {
                    TaskType type = TaskType.valueOf(pref);
                    int idx = type.ordinal();
                    volunteersCountByType[idx]++;
                    volunteersByType.get(idx).add(v);
                }
            }
        }
        return volunteersByType;
    }

    public Color[] generateColorPalette(int size) {
        Color[] palette = new Color[size];
        for (int i = 0; i < size; i++) {
            palette[i] = new Color(
                    (int) (Math.random() * 256),
                    (int) (Math.random() * 256),
                    (int) (Math.random() * 256)
            );
        }
        return palette;
    }
}