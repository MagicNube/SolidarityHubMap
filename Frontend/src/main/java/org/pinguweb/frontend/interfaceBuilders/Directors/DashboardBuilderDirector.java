package org.pinguweb.frontend.interfaceBuilders.Directors;

import com.storedobject.chart.*;
import com.vaadin.flow.component.Component;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.AffectedDTO;
import org.pingu.domain.DTO.NeedDTO;
import org.pingu.domain.DTO.TaskDTO;
import org.pingu.domain.DTO.VolunteerDTO;
import org.pingu.domain.enums.TaskType;
import org.pinguweb.frontend.interfaceBuilders.Builders.DashboardBuilder;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.ChartType;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.Dashboard;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData.Filters;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData.TestString;
import org.pinguweb.frontend.mapObjects.Affected;
import org.pinguweb.frontend.mapObjects.Need;
import org.pinguweb.frontend.mapObjects.Task;
import org.pinguweb.frontend.mapObjects.Volunteer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DashboardBuilderDirector {
    DashboardBuilder builder = new DashboardBuilder();
    Integer[] completedTasksPerDay = new Integer[7];
    Integer[] needsByTaskType = new Integer[TaskType.values().length];
    Integer[] completedTasks = new Integer[TaskType.values().length];

    public Component buildTest() {
        TestString fs = new TestString("TasksCr", LocalDate.now(), LocalDateTime.now(), 1, false);
        TestString sn = new TestString("TasksCo", LocalDate.now().minusDays(1), LocalDateTime.now().minusDays(1), 2, false);
        TestString tr = new TestString("NeedsCr", LocalDate.now().minusDays(2), LocalDateTime.now().minusDays(2), 3, true);
        TestString fr = new TestString("NeedsCo", LocalDate.now().minusDays(3), LocalDateTime.now().minusDays(3), 4, true);
        Filters firstFilter = Filters.builder().build();

        Dashboard first = Dashboard.createSimpleDashboard("Test 1", ChartType.PIE, new RectangularCoordinate(
                new XAxis(DataType.CATEGORY),
                new YAxis(DataType.NUMBER))
        );

        first.addData(
                new Object[]{fs.getName(), sn.getName(), tr.getName(), fr.getName()},
                new TestString[]{fs, sn, tr, fr},
                new Object[]{1, 2, 3, 4},
                new Integer[]{1, 2, 3, 4},
                "Mis datos",
                new Color[]{new Color(0, 0, 255)}
        );
        first.addData(
                new Object[]{fs.getName(), sn.getName(), tr.getName(), fr.getName()},
                new TestString[]{fs, sn, tr, fr},
                new Object[]{4, 3, 2, 1},
                new Integer[]{4, 3, 2, 1},
                "Mis datos",
                new Color[]{new Color(0, 255, 0)}
        );
        firstFilter.addDashboard(first);

        builder.reset();

        builder.setTile("Test");
        builder.setSubtitle("Doble test");
        builder.addBelow(firstFilter);
        builder.addBelow(first);
        return builder.build().getInterface();
    }

    //tareas completadas por dias
    public Component buildCompletedTasksChart() {
        calculatedDays();

        Dashboard completedTasksDashboard = Dashboard.createSimpleDashboard("Tareas Completadas por Día", ChartType.BAR,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER))
        );

        completedTasksDashboard.addData(
                new Object[]{"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"},
                new Object[]{"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"},
                new Object[]{completedTasksPerDay[0], completedTasksPerDay[1], completedTasksPerDay[2], completedTasksPerDay[3], completedTasksPerDay[4], completedTasksPerDay[5], completedTasksPerDay[6]},
                completedTasksPerDay,
                "Tareas Completadas",
                new Color[]{new Color(0, 128, 255), new Color(255, 128, 0), new Color(128, 255, 0), new Color(255, 0, 128), new Color(128, 0, 255), new Color(255, 255, 0), new Color(0, 255, 255)}
        );

        builder.reset();
        builder.setTile("Tareas Completadas");
        builder.setSubtitle("Por Día de la Semana");
        builder.addBelow(completedTasksDashboard);

        return builder.build().getInterface();
    }

    // Gnecesidades no cubiertas por tasktype
    public Component buildUncoveredNeedsChart() {
        needsPerType();

        TaskType[] taskTypes = TaskType.values();
        String[] taskTypeLabels = Arrays.stream(taskTypes).map(TaskType::name).toArray(String[]::new);

        Dashboard uncoveredNeedsDashboard = Dashboard.createSimpleDashboard("Necesidades No Cubiertas", ChartType.BAR,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER))
        );

        uncoveredNeedsDashboard.addData(
                taskTypeLabels,
                taskTypeLabels,
                needsByTaskType,
                needsByTaskType,
                "Necesidades No Cubiertas",
                new Color[]{new Color(255, 0, 0), new Color(0, 255, 0), new Color(0, 0, 255), new Color(255, 255, 0), new Color(255, 0, 255)}
        );

        builder.reset();
        builder.setTile("Necesidades No Cubiertas");
        builder.setSubtitle("Por Tipo de Tarea");
        builder.addBelow(uncoveredNeedsDashboard);

        return builder.build().getInterface();
    }
// Tareas no terminadas por tasktype
public Component buildUncoveredTaskTypeChart() {
    TasksperType();

    TaskType[] taskTypes = TaskType.values();
    String[] taskTypeLabels = Arrays.stream(taskTypes).map(TaskType::name).toArray(String[]::new);

    Dashboard uncoveredTasksDashboard = Dashboard.createSimpleDashboard("Tareas No Terminadas", ChartType.BAR,
            new RectangularCoordinate(
                    new XAxis(DataType.CATEGORY),
                    new YAxis(DataType.NUMBER))
    );

    uncoveredTasksDashboard.addData(
            taskTypeLabels,
            taskTypeLabels,
            completedTasks,
            completedTasks,
            "Tareas No Terminadas",
            new Color[]{new Color(128, 0, 128), new Color(0, 128, 128), new Color(128, 128, 0), new Color(255, 128, 128), new Color(128, 255, 128)}
    );

    builder.reset();
    builder.setTile("Tareas No Terminadas");
    builder.setSubtitle("Por Tipo de Tarea");
    builder.addBelow(uncoveredTasksDashboard);

    return builder.build().getInterface();
}
    public Component buildVolunteersByTaskTypeChart(){
        VolunteerPorSkill();

        TaskType[] taskTypes = TaskType.values();
        String[] taskTypeLabels = Arrays.stream(taskTypes).map(TaskType::name).toArray(String[]::new);

        Dashboard volunteersDashboard = Dashboard.createSimpleDashboard("Voluntarios por Tipo de Tarea", ChartType.BAR,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER))
        );

        volunteersDashboard.addData(
                taskTypeLabels,
                taskTypeLabels,
                needsByTaskType,
                needsByTaskType,
                "Voluntarios",
                new Color[]{new Color(0, 255, 128), new Color(128, 0, 255), new Color(255, 128, 0), new Color(128, 128, 255), new Color(255, 255, 128)}
        );

        builder.reset();
        builder.setTile("Voluntarios");
        builder.setSubtitle("Por Tipo de Tarea");
        builder.addBelow(volunteersDashboard);

        return builder.build().getInterface();
    }

    public Component buildVolunteersVSAffected() {
        // Obtener datos de voluntarios y afectados desde el servidor
        List<VolunteerDTO> volunteers = Volunteer.getAllFromServer();
        List<AffectedDTO> affected = Affected.getAllFromServer();

        // Contar el número total de voluntarios y afectados
        int totalVolunteers = volunteers.size();
        int totalAffected = affected.size();

        // Crear etiquetas y datos para el gráfico
        String[] labels = {"Voluntarios", "Afectados"};
        Integer[] counts = {totalVolunteers, totalAffected};

        // Crear el dashboard con un gráfico de barras apiladas
        Dashboard volunteersDashboard = Dashboard.createSimpleDashboard("Voluntarios vs Afectados", ChartType.STACKED_BAR,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER))
        );
//a
        // Agregar datos al gráfico
        volunteersDashboard.addData(
                labels,
                labels,
                counts,
                counts,
                "Comparación",
                new Color[]{new Color(0, 128, 255), new Color(255, 0, 0)}
        );

        // Configurar el builder y devolver el componente
        builder.reset();
        builder.setTile("Voluntarios vs Afectados");
        builder.setSubtitle("Comparación Total");
        builder.addBelow(volunteersDashboard);

        return builder.build().getInterface();
    }

    public void calculatedDays() {
        // Inicializar el conteo de tareas completadas por día (0 = Lunes, 6 = Domingo)
        List<TaskDTO> list = Task.getAllFromServer();
        for (TaskDTO task : list) {
            if (task.getStatus().equals("FINISHED") && task.getEstimatedEndTimeDate() != null) {
                //(1 = Lunes, 7 = Domingo)
                int dayOfWeek = task.getEstimatedEndTimeDate().getDayOfWeek().getValue();
                //(0 = Lunes, 6 = Domingo)
                int index = (dayOfWeek - 1);
                completedTasksPerDay[index]++;
            }
        }

    }

    //PRUEBAS PARA GRAFICAS CON BUILDER
    public void needsPerType() {
        Map<TaskType, Integer> needsByTaskTypeMap = new HashMap<>();
        for (TaskType taskType : TaskType.values()) {
            needsByTaskTypeMap.put(taskType, 0); // Inicializar el mapa con 0 para cada tipo de tarea
        }
        List<NeedDTO> needs = Need.getAllFromServer();
        for (NeedDTO need : needs) {
            if (!need.getStatus().equals("FINISHED")) {
                TaskType taskType = TaskType.valueOf(need.getNeedType());
                needsByTaskTypeMap.put(taskType, needsByTaskTypeMap.get(taskType) + 1);
            }
        }
        // Convertir el mapa a un arreglo si es necesario
        for (int i = 0; i < TaskType.values().length; i++) {
            needsByTaskType[i] = needsByTaskTypeMap.get(TaskType.values()[i]);
        }

    }

    public void TasksperType() {
        // Inicializar el mapa con todos los tipos de tarea y un conteo inicial de 0
        Map<TaskType, Integer> completedTasksMap = new HashMap<>();
        for (TaskType taskType : TaskType.values()) {
            completedTasksMap.put(taskType, 0);
        }
        // Iterar sobre las tareas obtenidas del servidor
        for (TaskDTO taskk : Task.getAllFromServer()) {
            if (!taskk.getStatus().equals("FINISHED")) {
                TaskType taskType = TaskType.valueOf(taskk.getType());
                completedTasksMap.put(taskType, completedTasksMap.get(taskType) + 1);
            }
        }
        // Convertir el mapa a un arreglo si es necesario
        for (int i = 0; i < TaskType.values().length; i++) {
            completedTasks[i] = completedTasksMap.get(TaskType.values()[i]);
        }
    }
    public void VolunteerPorSkill(){
        Map<TaskType, Integer> volunteersByTaskType = new HashMap<>();
        for (TaskType taskType : TaskType.values()) {
            volunteersByTaskType.put(taskType, 0);
        }

        // Contar voluntarios por TaskType
        for (VolunteerDTO volunteer : Volunteer.getAllFromServer()) {
            for (String taskPreference : volunteer.getTaskPreferences()) {
                TaskType taskType = TaskType.valueOf(taskPreference);
                volunteersByTaskType.put(taskType, volunteersByTaskType.get(taskType) + 1);
            }
        }
    }
}


