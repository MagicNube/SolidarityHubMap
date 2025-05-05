package org.pinguweb.frontend.interfaceBuilders.Directors;

import com.storedobject.chart.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

    public Component buildCompletedTasksChart() {
        calculatedDays();

        String[] daysOfWeek = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

        Dashboard completedTasksDashboard = Dashboard.createSimpleDashboard(
                "Tareas Completadas por Día",
                ChartType.BAR,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );

        for (int i = 0; i < daysOfWeek.length; i++) {
            Color randomColor = new Color(
                    (int) (Math.random() * 256),
                    (int) (Math.random() * 256),
                    (int) (Math.random() * 256)
            );

            completedTasksDashboard.addData(
                    new Object[]{daysOfWeek[i]},
                    new Object[]{daysOfWeek[i]},
                    new Object[]{completedTasksPerDay[i]},
                    new Integer[]{completedTasksPerDay[i]},
                    daysOfWeek[i],
                    new Color[]{randomColor}
            );
        }
        builder.reset();
        builder.setTile("Tareas Completadas");
        builder.setSubtitle("Por Día de la Semana");
        builder.addBelow(completedTasksDashboard);

        return builder.build().getInterface();
    }

    public Component buildCompletedTasksPieChart() {
        calculatedDays();

        Dashboard completedTasksPieDashboard = Dashboard.createSimpleDashboard("Tareas Completadas por Día", ChartType.PIE,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER))
        );

        completedTasksPieDashboard.addData(
                new Object[]{"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"},
                new Object[]{"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"},
                completedTasksPerDay,
                completedTasksPerDay,
                "Tareas Completadas",
                new Color[]{new Color(0, 128, 255), new Color(255, 128, 0), new Color(128, 255, 0), new Color(255, 0, 128), new Color(128, 0, 255), new Color(255, 255, 0), new Color(0, 255, 255)}
        );
        builder.reset();
        builder.setTile("Tareas Completadas");
        builder.setSubtitle("Por Día de la Semana");
        builder.addBelow(completedTasksPieDashboard);

        return builder.build().getInterface();
    }

    public Component buildUncoveredNeedsChart() {
        needsPerType();

        String[] taskTypeLabels = {
                "MEDICAL", "SEARCH", "SAFETY", "LOGISTICS", "COMMUNICATION", "PSYCHOLOGICAL",
                "FEED", "MOBILITY", "REFUGE", "OTHER", "PEOPLEMANAGEMENT", "CLOTHING",
                "BUILDING", "CLEANING", "FIREFIGHTERS", "POLICE"
        };

        // Configuramos el dashboard
        Dashboard uncoveredNeedsDashboard = Dashboard.createSimpleDashboard(
                "Necesidades No Cubiertas",
                ChartType.BAR,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );

        for (int i = 0; i < taskTypeLabels.length; i++) {
            Color randomColor = new Color(
                    (int) (Math.random() * 256),
                    (int) (Math.random() * 256),
                    (int) (Math.random() * 256)
            );

            uncoveredNeedsDashboard.addData(
                    // XData y XObjects: etiqueta horizontal
                    new Object[]{ taskTypeLabels[i] },
                    new Object[]{ taskTypeLabels[i] },

                    // YData y YObjects: valor de la barra
                    new Object[]{ needsByTaskType[i] },
                    new Integer[]{ needsByTaskType[i] },

                    // Nombre de la serie = la propia etiqueta
                    taskTypeLabels[i],

                    // Color para esa barra/serie
                    new Color[]{ randomColor }
            );
        }

        builder.reset();
        builder.setTile("Necesidades No Cubiertas");
        builder.setSubtitle("Por Tipo de Tarea");
        builder.addBelow(uncoveredNeedsDashboard);

        return builder.build().getInterface();
    }

    public Component buildUncoveredNeedsPieChart() {
        needsPerType();

        String[] taskTypeLabels = {
                "MEDICAL", "SEARCH", "SAFETY", "LOGISTICS", "COMMUNICATION", "PSYCHOLOGICAL",
                "FEED", "MOBILITY", "REFUGE", "OTHER", "PEOPLEMANAGEMENT", "CLOTHING",
                "BUILDING", "CLEANING", "FIREFIGHTERS", "POLICE"
        };

        Dashboard uncoveredNeedsPieDashboard = Dashboard.createSimpleDashboard("Necesidades No Cubiertas", ChartType.PIE,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER))
        );

        uncoveredNeedsPieDashboard.addData(
                taskTypeLabels,
                taskTypeLabels,
                needsByTaskType,
                needsByTaskType,
                "",
                new Color[]{
                        new Color(255, 0, 0), new Color(0, 255, 0), new Color(0, 0, 255),
                        new Color(255, 255, 0), new Color(255, 0, 255), new Color(0, 255, 255),
                        new Color(128, 0, 128), new Color(128, 128, 0), new Color(0, 128, 128),
                        new Color(128, 128, 128), new Color(255, 128, 0), new Color(128, 255, 0),
                        new Color(0, 128, 255), new Color(255, 0, 128), new Color(128, 0, 255),
                        new Color(0, 255, 128)
                }
        );
        builder.reset();
        builder.setTile("Necesidades No Cubiertas");
        builder.setSubtitle("Por Tipo de Tarea");
        builder.addBelow(uncoveredNeedsPieDashboard);

        return builder.build().getInterface();
    }

public Component buildUncoveredTaskTypeChart() {
    TasksperType();

    TaskType[] taskTypes = TaskType.values();
    String[] taskTypeLabels = Arrays.stream(taskTypes).map(TaskType::name).toArray(String[]::new);

    Dashboard uncoveredTasksDashboard = Dashboard.createSimpleDashboard(
            "Tareas No Terminadas",
            ChartType.BAR,
            new RectangularCoordinate(
                    new XAxis(DataType.CATEGORY),
                    new YAxis(DataType.NUMBER)
            )
    );

    for (int i = 0; i < taskTypeLabels.length; i++) {
        Color randomColor = new Color(
                (int) (Math.random() * 256),
                (int) (Math.random() * 256),
                (int) (Math.random() * 256)
        );

        uncoveredTasksDashboard.addData(
                new Object[]{taskTypeLabels[i]},
                new Object[]{taskTypeLabels[i]},
                new Object[]{completedTasks[i]},
                new Integer[]{completedTasks[i]},
                taskTypeLabels[i],
                new Color[]{randomColor}
        );
    }
    builder.reset();
    builder.setTile("Tareas No Terminadas");
    builder.setSubtitle("Por Tipo de Tarea");
    builder.addBelow(uncoveredTasksDashboard);

    return builder.build().getInterface();
}

    public Component buildUncoveredTaskTypePieChart() {
        TasksperType();

        TaskType[] taskTypes = TaskType.values();
        String[] taskTypeLabels = Arrays.stream(taskTypes).map(TaskType::name).toArray(String[]::new);

        Dashboard uncoveredTasksPieDashboard = Dashboard.createSimpleDashboard("Tareas No Terminadas", ChartType.PIE,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER))
        );

        uncoveredTasksPieDashboard.addData(
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
        builder.addBelow(uncoveredTasksPieDashboard);

        return builder.build().getInterface();
    }
    public Component buildVolunteersByTaskTypeChart() {
        VolunteerPorSkill();

        TaskType[] taskTypes = TaskType.values();
        String[] taskTypeLabels = Arrays.stream(taskTypes).map(TaskType::name).toArray(String[]::new);

        Dashboard volunteersDashboard = Dashboard.createSimpleDashboard(
                "Voluntarios por Tipo de Tarea",
                ChartType.BAR,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );

        for (int i = 0; i < taskTypeLabels.length; i++) {
            Color randomColor = new Color(
                    (int) (Math.random() * 256),
                    (int) (Math.random() * 256),
                    (int) (Math.random() * 256)
            );

            volunteersDashboard.addData(
                    new Object[]{taskTypeLabels[i]},
                    new Object[]{taskTypeLabels[i]},
                    new Object[]{needsByTaskType[i]},
                    new Integer[]{needsByTaskType[i]},
                    taskTypeLabels[i],
                    new Color[]{randomColor}
            );
        }
        builder.reset();
        builder.setTile("Voluntarios");
        builder.setSubtitle("Por Tipo de Tarea");
        builder.addBelow(volunteersDashboard);

        return builder.build().getInterface();
    }

    public Component buildVolunteersByTaskTypePieChart() {
        VolunteerPorSkill();

        TaskType[] taskTypes = TaskType.values();
        String[] taskTypeLabels = Arrays.stream(taskTypes).map(TaskType::name).toArray(String[]::new);

        Dashboard volunteersPieDashboard = Dashboard.createSimpleDashboard("Voluntarios por Tipo de Tarea", ChartType.PIE,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER))
        );

        volunteersPieDashboard.addData(
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
        builder.addBelow(volunteersPieDashboard);

        return builder.build().getInterface();
    }

    public Component buildVolunteersVSAffected() {
        List<VolunteerDTO> volunteers = Volunteer.getAllFromServer();
        List<AffectedDTO> affected = Affected.getAllFromServer();

        int totalVolunteers = volunteers.size();
        int totalAffected = affected.size();

        String[] labels = {"Voluntarios", "Afectados"};
        Integer[] counts = {totalVolunteers, totalAffected};

        Dashboard volunteersDashboard = Dashboard.createSimpleDashboard(
                "Voluntarios vs Afectados",
                ChartType.BAR,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );

        for (int i = 0; i < labels.length; i++) {
            Color randomColor = new Color(
                    (int) (Math.random() * 256),
                    (int) (Math.random() * 256),
                    (int) (Math.random() * 256)
            );

            volunteersDashboard.addData(
                    new Object[]{labels[i]},
                    new Object[]{labels[i]},
                    new Object[]{counts[i]},
                    new Integer[]{counts[i]},
                    labels[i],
                    new Color[]{randomColor}
            );
        }
        builder.reset();
        builder.setTile("Voluntarios vs Afectados");
        builder.setSubtitle("Comparación Total");
        builder.addBelow(volunteersDashboard);

        return builder.build().getInterface();
    }

    public Component buildVolunteersVSAffectedPieChart() {
        List<VolunteerDTO> volunteers = Volunteer.getAllFromServer();
        List<AffectedDTO> affected = Affected.getAllFromServer();

        int totalVolunteers = volunteers.size();
        int totalAffected = affected.size();
        String[] labels = {"Voluntarios", "Afectados"};
        Integer[] counts = {totalVolunteers, totalAffected};
        Dashboard volunteersPieDashboard = Dashboard.createSimpleDashboard("Voluntarios vs Afectados", ChartType.PIE,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER))
        );
        volunteersPieDashboard.addData(
                labels,
                labels,
                counts,
                counts,
                "Comparación",
                new Color[]{new Color(0, 128, 255), new Color(255, 0, 0)}
        );
        builder.reset();
        builder.setTile("Voluntarios vs Afectados");
        builder.setSubtitle("Comparación Total");
        builder.addBelow(volunteersPieDashboard);

        return builder.build().getInterface();
    }

    public void calculatedDays() {
        List<TaskDTO> list = Task.getAllFromServer();
        completedTasksPerDay = new Integer[]{0,0,0,0,0,0,0};
        for (TaskDTO task : list) {
            if (task.getStatus().equals("FINISHED") && task.getEstimatedEndTimeDate() != null) {
                //(1 = Lunes, 7 = Domingo)
                int dayOfWeek = task.getEstimatedEndTimeDate().getDayOfWeek().getValue();
                //(0 = Lunes, 6 = Domingo)
                int index = (dayOfWeek - 1);
                completedTasksPerDay[index] = completedTasksPerDay[index] + 1;
            }
        }

    }

    public void needsPerType() {
        Map<TaskType, Integer> needsByTaskTypeMap = new HashMap<>();
        List<NeedDTO> needs = Need.getAllFromServer();
        for (NeedDTO need : needs) {
            if (!need.getStatus().equals("FINISHED")) {
                TaskType taskType = TaskType.valueOf(need.getNeedType());
                needsByTaskTypeMap.put(taskType, needsByTaskTypeMap.get(taskType) + 1);
            }
        }
        for (int i = 0; i < TaskType.values().length; i++) {
            needsByTaskType[i] = needsByTaskTypeMap.get(TaskType.values()[i]);
        }
    }
    public void TasksperType() {
        Map<TaskType, Integer> completedTasksMap = new HashMap<>();
        for (TaskDTO taskk : Task.getAllFromServer()) {
            if (!taskk.getStatus().equals("FINISHED")) {
                TaskType taskType = TaskType.valueOf(taskk.getType());
                completedTasksMap.put(taskType, completedTasksMap.get(taskType) + 1);
            }
        }
        for (int i = 0; i < TaskType.values().length; i++) {
            completedTasks[i] = completedTasksMap.get(TaskType.values()[i]);
        }
    }
    public void VolunteerPorSkill(){
        Map<TaskType, Integer> volunteersByTaskType = new HashMap<>();
        for (TaskType taskType : TaskType.values()) {
            volunteersByTaskType.put(taskType, 0);
        }
        for (VolunteerDTO volunteer : Volunteer.getAllFromServer()) {
            for (String taskPreference : volunteer.getTaskPreferences()) {
                TaskType taskType = TaskType.valueOf(taskPreference);
                volunteersByTaskType.put(taskType, volunteersByTaskType.get(taskType) + 1);
            }
        }
    }
    private HorizontalLayout twoColumns(Component left, Component right) {
        HorizontalLayout row = new HorizontalLayout(left, right);
        row.setWidthFull();
        row.setSpacing(true);
        row.setPadding(false);
        // si quieres que cada columna crezca al 50%:
        row.expand(left, right);
        return row;
    }

    public Component buildAllChartsGrid() {
        // construyo cada gráfico por separado, sin modificar sus métodos
        Component tareasBar   = buildCompletedTasksChart();
        Component tareasPie   = buildCompletedTasksPieChart();
        Component needsBar    = buildUncoveredNeedsChart();
        Component needsPie    = buildUncoveredNeedsPieChart();
        Component unctaskBar  = buildUncoveredTaskTypeChart();
        Component unctaskPie  = buildUncoveredTaskTypePieChart();
        Component volBar      = buildVolunteersByTaskTypeChart();
        Component volPie      = buildVolunteersByTaskTypePieChart();
        Component vsBar       = buildVolunteersVSAffected();
        Component vsPie       = buildVolunteersVSAffectedPieChart();

        VerticalLayout grid = new VerticalLayout();
        grid.setWidthFull();
        grid.setPadding(false);
        grid.setSpacing(true);

        grid.add(twoColumns(tareasBar,   tareasPie));
        grid.add(twoColumns(needsBar,    needsPie));
        grid.add(twoColumns(unctaskBar,  unctaskPie));
        grid.add(twoColumns(volBar,      volPie));
        grid.add(twoColumns(vsBar,       vsPie));

        return grid;
    }
}