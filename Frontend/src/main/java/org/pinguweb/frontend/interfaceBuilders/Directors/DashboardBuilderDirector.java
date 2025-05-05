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
import org.pinguweb.frontend.mapObjects.Affected;
import org.pinguweb.frontend.mapObjects.Need;
import org.pinguweb.frontend.mapObjects.Task;
import org.pinguweb.frontend.mapObjects.Volunteer;
import org.example.coordinacionbdsolidarityhub.model.enums.ResourceType;

import java.util.*;

@Slf4j
public class DashboardBuilderDirector {
    private final DashboardBuilder builder = new DashboardBuilder();
    private Integer[] completedTasksPerDay = new Integer[7];
    private Integer[] needsByTaskType = new Integer[TaskType.values().length];
    private Integer[] completedTasks = new Integer[TaskType.values().length];
    private Integer[] volunteersCountByType = new Integer[TaskType.values().length];
    private Integer[] resourcesByType = new Integer[ResourceType.values().length];

    public Component buildCompletedTasksChart() {
        calculateCompletedTasksPerDay();

        String[] labels = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

        Dashboard d = Dashboard.createSimpleDashboard(
                "",
                ChartType.BAR,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );
        for (int i = 0; i < labels.length; i++) {
            int v = completedTasksPerDay[i] != null ? completedTasksPerDay[i] : 0;
            Color c = new Color(
                    (int)(Math.random()*256),
                    (int)(Math.random()*256),
                    (int)(Math.random()*256)
            );
            d.addData(
                    new Object[]{ labels[i] },
                    new Object[]{ labels[i] },
                    new Object[]{ v },
                    new Integer[]{ v },
                    labels[i],
                    new Color[]{ c }
            );
        }
        builder.reset();
        builder.setTile("Tareas Completadas");
        builder.setSubtitle("Por Día de la Semana");
        builder.addBelow(d);
        return builder.build().getInterface();
    }

    public Component buildCompletedTasksPieChart() {
        calculateCompletedTasksPerDay();

        String[] labels = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        Integer[] values = completedTasksPerDay;

        Dashboard dp = Dashboard.createSimpleDashboard(
                "",
                ChartType.PIE,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );
        dp.addData(
                labels,
                labels,
                values,
                values,
                "Tareas Completadas",
                generateColorPalette(labels.length)
        );
        builder.reset();
        builder.setTile("Tareas Completadas");
        builder.setSubtitle("Por Día de la Semana");
        builder.addBelow(dp);
        return builder.build().getInterface();
    }

    public Component buildUncoveredNeedsChart() {
        calculateNeedsPerType();

        String[] labels = Arrays.stream(TaskType.values())
                .map(TaskType::name)
                .toArray(String[]::new);

        Dashboard d = Dashboard.createSimpleDashboard(
                "",                       // <-- título interno vacío
                ChartType.BAR,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );

        for (int i = 0; i < labels.length; i++) {
            int v = needsByTaskType[i] != null ? needsByTaskType[i] : 0;
            Color c = new Color(
                    (int) (Math.random() * 256),
                    (int) (Math.random() * 256),
                    (int) (Math.random() * 256)
            );
            d.addData(
                    new Object[]{ labels[i] },
                    new Object[]{ labels[i] },
                    new Object[]{ v },
                    new Integer[]{ v },
                    labels[i],
                    new Color[]{ c }
            );
        }
        builder.reset();
        builder.setTile("Necesidades No Cubiertas");
        builder.setSubtitle("Por Tipo de Tarea");
        builder.addBelow(d);
        return builder.build().getInterface();
    }

    public Component buildUncoveredNeedsPieChart() {
        calculateNeedsPerType();

        String[] allLabels = Arrays.stream(TaskType.values())
                .map(TaskType::name)
                .toArray(String[]::new);
        List<String>  lbl = new ArrayList<>();
        List<Integer> val = new ArrayList<>();
        for (int i = 0; i < allLabels.length; i++) {
            int v = needsByTaskType[i] != null ? needsByTaskType[i] : 0;
            if (v > 0) {
                lbl.add(allLabels[i]);
                val.add(v);
            }
        }
        String[]  labels = lbl.toArray(new String[0]);
        Integer[] values = val.toArray(new Integer[0]);

        Dashboard dp = Dashboard.createSimpleDashboard(
                "",                       // <-- título interno vacío
                ChartType.PIE,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );
        dp.addData(
                labels,
                labels,
                values,
                values,
                "Necesidades No Cubiertas",
                generateColorPalette(labels.length)
        );
        builder.reset();
        builder.setTile("Necesidades No Cubiertas");
        builder.setSubtitle("Por Tipo de Tarea");
        builder.addBelow(dp);
        return builder.build().getInterface();
    }


    public Component buildUncoveredTaskTypeChart() {
        calculateCompletedTasksPerType();

        String[] labels = Arrays.stream(TaskType.values())
                .map(TaskType::name)
                .toArray(String[]::new);

        Dashboard d = Dashboard.createSimpleDashboard(
                "",
                ChartType.BAR,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );
        for (int i = 0; i < labels.length; i++) {
            int v = completedTasks[i] != null ? completedTasks[i] : 0;
            Color c = new Color(
                    (int)(Math.random()*256),
                    (int)(Math.random()*256),
                    (int)(Math.random()*256)
            );
            d.addData(
                    new Object[]{ labels[i] },
                    new Object[]{ labels[i] },
                    new Object[]{ v },
                    new Integer[]{ v },
                    labels[i],
                    new Color[]{ c }
            );
        }
        builder.reset();
        builder.setTile("Tareas No Terminadas");
        builder.setSubtitle("Por Tipo de Tarea");
        builder.addBelow(d);
        return builder.build().getInterface();
    }

    public Component buildUncoveredTaskTypePieChart() {
        calculateCompletedTasksPerType();

        String[] allLabels = Arrays.stream(TaskType.values())
                .map(TaskType::name)
                .toArray(String[]::new);

        List<String>  lbl = new ArrayList<>();
        List<Integer> val = new ArrayList<>();
        for (int i = 0; i < allLabels.length; i++) {
            int v = completedTasks[i] != null ? completedTasks[i] : 0;
            if (v > 0) {
                lbl.add(allLabels[i]);
                val.add(v);
            }
        }
        String[]  labels = lbl.toArray(new String[0]);
        Integer[] values = val.toArray(new Integer[0]);

        Dashboard dp = Dashboard.createSimpleDashboard(
                "",
                ChartType.PIE,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );
        dp.addData(
                labels,
                labels,
                values,
                values,
                "Tareas No Terminadas",
                generateColorPalette(labels.length)
        );
        builder.reset();
        builder.setTile("Tareas No Terminadas");
        builder.setSubtitle("Por Tipo de Tarea");
        builder.addBelow(dp);
        return builder.build().getInterface();
    }



    public Component buildVolunteersByTaskTypeChart() {
        calculateVolunteersByTaskType();

        String[] labels = Arrays.stream(TaskType.values())
                .map(TaskType::name)
                .toArray(String[]::new);

        Dashboard d = Dashboard.createSimpleDashboard(
                "",
                ChartType.BAR,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );
        for (int i = 0; i < labels.length; i++) {
            int v = volunteersCountByType[i] != null ? volunteersCountByType[i] : 0;
            Color c = new Color(
                    (int)(Math.random()*256),
                    (int)(Math.random()*256),
                    (int)(Math.random()*256)
            );
            d.addData(
                    new Object[]{ labels[i] },
                    new Object[]{ labels[i] },
                    new Object[]{ v },
                    new Integer[]{ v },
                    labels[i],
                    new Color[]{ c }
            );
        }
        builder.reset();
        builder.setTile("Voluntarios");
        builder.setSubtitle("Por Tipo de Tarea");
        builder.addBelow(d);
        return builder.build().getInterface();
    }

    public Component buildVolunteersByTaskTypePieChart() {
        calculateVolunteersByTaskType();

        String[] labels = Arrays.stream(TaskType.values())
                .map(TaskType::name)
                .toArray(String[]::new);
        Integer[] values = volunteersCountByType;

        Dashboard dp = Dashboard.createSimpleDashboard(
                "",
                ChartType.PIE,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );
        dp.addData(
                labels,
                labels,
                values,
                values,
                "Voluntarios",
                generateColorPalette(labels.length)
        );
        builder.reset();
        builder.setTile("Voluntarios");
        builder.setSubtitle("Por Tipo de Tarea");
        builder.addBelow(dp);
        return builder.build().getInterface();
    }

    public Component buildVolunteersVSAffectedChart() {
        List<VolunteerDTO> volunteers = Volunteer.getAllFromServer();
        List<AffectedDTO> affected = Affected.getAllFromServer();

        String[] labels = {"Voluntarios", "Afectados"};
        Integer[] values = {volunteers.size(), affected.size()};

        Dashboard d = Dashboard.createSimpleDashboard(
                "",
                ChartType.BAR,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );
        for (int i = 0; i < labels.length; i++) {
            Color c = new Color(
                    (int)(Math.random()*256),
                    (int)(Math.random()*256),
                    (int)(Math.random()*256)
            );
            d.addData(
                    new Object[]{ labels[i] },
                    new Object[]{ labels[i] },
                    new Object[]{ values[i] },
                    new Integer[]{ values[i] },
                    labels[i],
                    new Color[]{ c }
            );
        }
        builder.reset();
        builder.setTile("Voluntarios vs Afectados");
        builder.setSubtitle("Comparación Total");
        builder.addBelow(d);
        return builder.build().getInterface();
    }

    public Component buildVolunteersVSAffectedPieChart() {
        List<VolunteerDTO> volunteers = Volunteer.getAllFromServer();
        List<AffectedDTO> affected = Affected.getAllFromServer();

        String[] labels = {"Voluntarios", "Afectados"};
        Integer[] values = {volunteers.size(), affected.size()};

        Dashboard dp = Dashboard.createSimpleDashboard(
                "",
                ChartType.PIE,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );
        dp.addData(
                labels,
                labels,
                values,
                values,
                "Comparación",
                generateColorPalette(labels.length)
        );
        builder.reset();
        builder.setTile("Voluntarios vs Afectados");
        builder.setSubtitle("Comparación Total");
        builder.addBelow(dp);
        return builder.build().getInterface();
    }
    //TODO: Nico necesitamos los controles de Resource que no están
    public Component buildResourcesByTypeChart() {
        calculateResourcesByType();

        String[] labels = Arrays.stream(ResourceType.values())
                .map(ResourceType::name)
                .toArray(String[]::new);

        Dashboard d = Dashboard.createSimpleDashboard(
                "",
                ChartType.BAR,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );
        for (int i = 0; i < labels.length; i++) {
            int v = resourcesByType[i] != null ? resourcesByType[i] : 0;
            Color c = new Color(
                    (int) (Math.random() * 256),
                    (int) (Math.random() * 256),
                    (int) (Math.random() * 256)
            );
            d.addData(
                    new Object[]{labels[i]},
                    new Object[]{labels[i]},
                    new Object[]{v},
                    new Integer[]{v},
                    labels[i],
                    new Color[]{c}
            );
        }
        builder.reset();
        builder.setTile("Recursos por Tipo");
        builder.setSubtitle("Distribución de Recursos");
        builder.addBelow(d);
        return builder.build().getInterface();
    }
//Todo: Nico necesitamos los controler de Resource que no están
    public Component buildResourcesByTypePieChart() {
        calculateResourcesByType();

        List<String> lbl = new ArrayList<>();
        List<Integer> val = new ArrayList<>();
        for (int i = 0; i < ResourceType.values().length; i++) {
            int count = resourcesByType[i] != null ? resourcesByType[i] : 0;
            if (count > 0) {
                lbl.add(ResourceType.values()[i].name());
                val.add(count);
            }
        }
        String[] labels = lbl.toArray(new String[0]);
        Integer[] values = val.toArray(new Integer[0]);

        Dashboard dp = Dashboard.createSimpleDashboard(
                "",
                ChartType.PIE,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );
        dp.addData(
                labels,
                labels,
                values,
                values,
                "Recursos por Tipo",
                generateColorPalette(labels.length)
        );
        builder.reset();
        builder.setTile("Recursos por Tipo");
        builder.setSubtitle("Distribución de Recursos");
        builder.addBelow(dp);
        return builder.build().getInterface();
    }

//TODO: Nico necesitamos los controles de Resource que no están
    public void calculateResourcesByType() {
        Arrays.fill(resourcesByType, 0);
        Map<ResourceType, Integer> map = new EnumMap<>(ResourceType.class);
        List<ResourceDTO> recursos = Resource.getAllFromServer();
        for (ResourceDTO recurso : recursos) {
            ResourceType type = ResourceType.valueOf(recurso.getType());
            map.put(type, map.getOrDefault(type, 0) + 1);
        }
        for (int i = 0; i < ResourceType.values().length; i++) {
            resourcesByType[i] = map.getOrDefault(ResourceType.values()[i], 0);
        }
    }

    public void calculateCompletedTasksPerDay() {
        List<TaskDTO> list = Task.getAllFromServer();
        Arrays.fill(completedTasksPerDay, 0);
        for (TaskDTO task : list) {
            if ("FINISHED".equals(task.getStatus()) && task.getEstimatedEndTimeDate() != null) {
                int index = task.getEstimatedEndTimeDate().getDayOfWeek().getValue() - 1;
                completedTasksPerDay[index]++;
            }
        }
    }

    public void calculateNeedsPerType() {
        Arrays.fill(needsByTaskType, 0);
        Map<TaskType, Integer> map = new EnumMap<>(TaskType.class);
        List<NeedDTO> needs = Need.getAllFromServer();
        for (NeedDTO need : needs) {
            if (!"FINISHED".equals(need.getStatus())) {
                TaskType type = TaskType.valueOf(need.getNeedType());
                map.put(type, map.getOrDefault(type, 0) + 1);
            }
        }
        for (int i = 0; i < TaskType.values().length; i++) {
            needsByTaskType[i] = map.getOrDefault(TaskType.values()[i], 0);
        }
    }

    public void calculateCompletedTasksPerType() {
        Arrays.fill(completedTasks, 0);
        Map<TaskType, Integer> map = new EnumMap<>(TaskType.class);
        for (TaskDTO task : Task.getAllFromServer()) {
            if (!"FINISHED".equals(task.getStatus())) {
                TaskType type = TaskType.valueOf(task.getType());
                map.put(type, map.getOrDefault(type, 0) + 1);
            }
        }
        for (int i = 0; i < TaskType.values().length; i++) {
            completedTasks[i] = map.getOrDefault(TaskType.values()[i], 0);
        }
    }

    public void calculateVolunteersByTaskType() {
        Arrays.fill(volunteersCountByType, 0);
        Map<TaskType, Integer> map = new EnumMap<>(TaskType.class);
        for (VolunteerDTO v : Volunteer.getAllFromServer()) {
            for (String pref : v.getTaskPreferences()) {
                TaskType type = TaskType.valueOf(pref);
                map.put(type, map.getOrDefault(type, 0) + 1);
            }
        }
        for (int i = 0; i < TaskType.values().length; i++) {
            volunteersCountByType[i] = map.getOrDefault(TaskType.values()[i], 0);
        }
    }

    private Color[] generateColorPalette(int size) {
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

    private HorizontalLayout twoColumns(Component left, Component right) {
        HorizontalLayout row = new HorizontalLayout(left, right);
        row.setWidthFull();
        row.setSpacing(true);
        row.setPadding(false);
        row.expand(left, right);
        return row;
    }

    public Component buildAllChartsGrid() {
        Component tareasBar   = buildCompletedTasksChart();
        Component tareasPie   = buildCompletedTasksPieChart();
        Component needsBar    = buildUncoveredNeedsChart();
        Component needsPie    = buildUncoveredNeedsPieChart();
        Component unctaskBar  = buildUncoveredTaskTypeChart();
        Component unctaskPie  = buildUncoveredTaskTypePieChart();
        Component volBar      = buildVolunteersByTaskTypeChart();
        Component volPie      = buildVolunteersByTaskTypePieChart();
        Component vsBar       = buildVolunteersVSAffectedChart();
        Component vsPie       = buildVolunteersVSAffectedPieChart();
        Component resourcesBar = buildResourcesByTypeChart();
        Component resourcesPie = buildResourcesByTypePieChart();

        VerticalLayout grid = new VerticalLayout();
        grid.setWidthFull();
        grid.setPadding(false);
        grid.setSpacing(true);

        grid.add(twoColumns(tareasBar,   tareasPie));
        grid.add(twoColumns(unctaskBar,  unctaskPie));
        grid.add(twoColumns(needsBar,    needsPie));
        grid.add(twoColumns(volBar,      volPie));
        grid.add(twoColumns(vsBar,       vsPie));
        grid.add(twoColumns(resourcesBar, resourcesPie));

        return grid;
    }
}
