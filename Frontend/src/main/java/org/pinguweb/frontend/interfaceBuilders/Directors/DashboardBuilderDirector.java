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
import org.pinguweb.frontend.interfaceBuilders.Builders.Interface;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.ChartType;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.Dashboard;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData.Filters;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;
import org.pinguweb.frontend.mapObjects.Affected;
import org.pinguweb.frontend.mapObjects.Need;
import org.pinguweb.frontend.mapObjects.Task;
import org.pinguweb.frontend.mapObjects.Volunteer;
import org.example.coordinacionbdsolidarityhub.model.enums.ResourceType;
import org.pinguweb.frontend.view.DashboardNewView;
import org.pinguweb.frontend.view.DashboardView;

import java.util.*;

@Slf4j
public class DashboardBuilderDirector {
    private final DashboardBuilder builder = new DashboardBuilder();
    private Integer[] completedTasksPerDay = new Integer[7];
    private Integer[] needsByTaskType = new Integer[TaskType.values().length];
    private Integer[] completedTasks = new Integer[TaskType.values().length];
    private Integer[] volunteersCountByType = new Integer[TaskType.values().length];
    private Integer[] resourcesByType = new Integer[ResourceType.values().length];

    public Interface get(){
        return builder.build();
    }

    public void buildComplete(){
        builder.reset();
        builder.setTile("Dashboards pingu web");
        builder.setSubtitle("Solidarity Hub");

        List<InterfaceComponent> fisrtPair = List.of(new Dashboard[]{buildCompletedTasksChart(), buildCompletedTasksPieChart()});
        List<InterfaceComponent> secondPair = List.of(new Dashboard[]{buildUncoveredNeedsChart(), buildUncoveredNeedsPieChart()});
        List<InterfaceComponent> thirdPair = List.of(new Dashboard[]{buildUncoveredTaskTypeChart(), buildUncoveredTaskTypePieChart()});
        List<InterfaceComponent> fourthPair = List.of(new Dashboard[]{buildVolunteersByTaskTypeChart(), buildVolunteersByTaskTypePieChart()});
        List<InterfaceComponent> fifthPair = List.of(new Dashboard[]{buildVolunteersVSAffectedChart(), buildVolunteersVSAffectedPieChart()});

        Filters firstPairFilters = Filters.builder().build();
        Filters secondPairFilters = Filters.builder().build();
        Filters thirdPairFilters = Filters.builder().build();
        Filters fourthPairFilters = Filters.builder().build();
        //Filters fifthPairFilters = Filters.builder().build();

        firstPairFilters.addDashboard(fisrtPair);
        secondPairFilters.addDashboard(secondPair);
        thirdPairFilters.addDashboard(thirdPair);
        fourthPairFilters.addDashboard(fourthPair);
        //fifthPairFilters.addDashboard(fifthPair); <--- Habría que ver como apañarlo

        builder.addBelow(firstPairFilters);
        builder.addSide(fisrtPair);
        builder.addBelow(secondPairFilters);
        builder.addSide(secondPair);
        builder.addBelow(thirdPairFilters);;
        builder.addSide(thirdPair);
        builder.addBelow(fourthPairFilters);
        builder.addSide(fourthPair);
        //builder.addBelow(fifthPairFilters);
        builder.addSide(fifthPair);
    }

    public Dashboard buildCompletedTasksChart() {
        List<TaskDTO> tasks = calculateCompletedTasksPerDay();

        String[] labels = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

        Dashboard d = Dashboard.createSimpleDashboard(
                "Tareas Completadas",
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
                    tasks.toArray(),
                    labels[i],
                    new Color[]{ c }
            );
        }
        return d;
    }

    public Dashboard buildCompletedTasksPieChart() {
        List<TaskDTO> tasks = calculateCompletedTasksPerDay();

        String[] labels = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        Integer[] values = completedTasksPerDay;

        Dashboard d = Dashboard.createSimpleDashboard(
                "Tareas Completadas",
                ChartType.PIE,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );
        d.addData(
                labels,
                labels,
                values,
                tasks.toArray(),
                "Tareas Completadas",
                generateColorPalette(labels.length)
        );
        return d;
    }

    public Dashboard buildUncoveredNeedsChart() {
        List<NeedDTO> needs = calculateNeedsPerType();

        String[] labels = Arrays.stream(TaskType.values())
                .map(TaskType::name)
                .toArray(String[]::new);

        Dashboard d = Dashboard.createSimpleDashboard(
                "Necesidades cubiertas",                       // <-- título interno vacío
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
                    new Object[]{v},
                    needs.toArray(),
                    labels[i],
                    new Color[]{ c }
            );
        }
        return d;
    }

    public Dashboard buildUncoveredNeedsPieChart() {
        List<NeedDTO> needs = calculateNeedsPerType();

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

        Dashboard d = Dashboard.createSimpleDashboard(
                "Necesidades no cubiertas",                       // <-- título interno vacío
                ChartType.PIE,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );
        d.addData(
                labels,
                labels,
                values,
                needs.toArray(),
                "Necesidades No Cubiertas",
                generateColorPalette(labels.length)
        );
        return d;
    }

    public Dashboard buildUncoveredTaskTypeChart() {
        List<TaskDTO> tasks = calculateCompletedTasksPerType();

        String[] labels = Arrays.stream(TaskType.values())
                .map(TaskType::name)
                .toArray(String[]::new);

        Dashboard d = Dashboard.createSimpleDashboard(
                "Tareas no terminadas por tipo",
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
                    tasks.toArray(),
                    labels[i],
                    new Color[]{ c }
            );
        }
        return d;
    }

    public Dashboard buildUncoveredTaskTypePieChart() {
        List<TaskDTO> tasks = calculateCompletedTasksPerType();

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

        Dashboard d = Dashboard.createSimpleDashboard(
                "Tareas no terminadas",
                ChartType.PIE,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );
        d.addData(
                labels,
                labels,
                values,
                tasks.toArray(),
                "Tareas No Terminadas",
                generateColorPalette(labels.length)
        );
        return d;
    }

    public Dashboard buildVolunteersByTaskTypeChart() {
        List<VolunteerDTO> volunteer = calculateVolunteersByTaskType();

        String[] labels = Arrays.stream(TaskType.values())
                .map(TaskType::name)
                .toArray(String[]::new);

        Dashboard d = Dashboard.createSimpleDashboard(
                "Voluntarios por tipo de tarea",
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
                    volunteer.toArray(),
                    labels[i],
                    new Color[]{ c }
            );
        }
        return d;
    }

    public Dashboard buildVolunteersByTaskTypePieChart() {
        List<VolunteerDTO> volunteer = calculateVolunteersByTaskType();

        String[] labels = Arrays.stream(TaskType.values())
                .map(TaskType::name)
                .toArray(String[]::new);
        Integer[] values = volunteersCountByType;

        Dashboard d = Dashboard.createSimpleDashboard(
                "Voluntarios por tipo de tarea",
                ChartType.PIE,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );
        d.addData(
                labels,
                labels,
                values,
                volunteer.toArray(),
                "Voluntarios",
                generateColorPalette(labels.length)
        );
        return d;
    }

    public Dashboard buildVolunteersVSAffectedChart() {
        List<VolunteerDTO> volunteers = Volunteer.getAllFromServer();
        List<AffectedDTO> affected = Affected.getAllFromServer();

        String[] labels = {"Voluntarios", "Afectados"};
        Integer[] values = {volunteers.size(), affected.size()};

        Dashboard d = Dashboard.createSimpleDashboard(
                "Voluntarios frente a afectados",
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
        return d;
    }

    public Dashboard buildVolunteersVSAffectedPieChart() {
        List<VolunteerDTO> volunteers = Volunteer.getAllFromServer();
        List<AffectedDTO> affected = Affected.getAllFromServer();

        String[] labels = {"Voluntarios", "Afectados"};
        Integer[] values = {volunteers.size(), affected.size()};

        Dashboard d = Dashboard.createSimpleDashboard(
                "Voluntarios frente a afectados",
                ChartType.PIE,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );
        d.addData(
                labels,
                labels,
                values,
                values,
                "Comparación",
                generateColorPalette(labels.length)
        );
        return d;
    }
//    //*
//    //TODO: Nico necesitamos los controles de Resource que no están
//    public Component buildResourcesByTypeChart() {
//        //calculateResourcesByType();
//
//        String[] labels = Arrays.stream(ResourceType.values())
//                .map(ResourceType::name)
//                .toArray(String[]::new);
//
//        Dashboard d = Dashboard.createSimpleDashboard(
//                "",
//                ChartType.BAR,
//                new RectangularCoordinate(
//                        new XAxis(DataType.CATEGORY),
//                        new YAxis(DataType.NUMBER)
//                )
//        );
//        for (int i = 0; i < labels.length; i++) {
//            int v = resourcesByType[i] != null ? resourcesByType[i] : 0;
//            Color c = new Color(
//                    (int) (Math.random() * 256),
//                    (int) (Math.random() * 256),
//                    (int) (Math.random() * 256)
//            );
//            d.addData(
//                    new Object[]{labels[i]},
//                    new Object[]{labels[i]},
//                    new Object[]{v},
//                    new Integer[]{v},
//                    labels[i],
//                    new Color[]{c}
//            );
//        }
//        builder.reset();
//        builder.setTile("Recursos por Tipo");
//        builder.setSubtitle("Distribución de Recursos");
//        builder.addBelow(d);
//        return builder.build().getInterface();
//    }
//
////Todo: Nico necesitamos los controler de Resource que no están
//    public Component buildResourcesByTypePieChart() {
//        //calculateResourcesByType();
//
//        List<String> lbl = new ArrayList<>();
//        List<Integer> val = new ArrayList<>();
//        for (int i = 0; i < ResourceType.values().length; i++) {
//            int count = resourcesByType[i] != null ? resourcesByType[i] : 0;
//            if (count > 0) {
//                lbl.add(ResourceType.values()[i].name());
//                val.add(count);
//            }
//        }
//        String[] labels = lbl.toArray(new String[0]);
//        Integer[] values = val.toArray(new Integer[0]);
//
//        Dashboard dp = Dashboard.createSimpleDashboard(
//                "",
//                ChartType.PIE,
//                new RectangularCoordinate(
//                        new XAxis(DataType.CATEGORY),
//                        new YAxis(DataType.NUMBER)
//                )
//        );
//        dp.addData(
//                labels,
//                labels,
//                values,
//                values,
//                "Recursos por Tipo",
//                generateColorPalette(labels.length)
//        );
//        builder.reset();
//        builder.setTile("Recursos por Tipo");
//        builder.setSubtitle("Distribución de Recursos");
//        builder.addBelow(dp);
//        return builder.build().getInterface();
//    }
//
////TODO: Nico necesitamos los controles de Resource que no están
//    /*public void calculateResourcesByType() {
//        Arrays.fill(resourcesByType, 0);
//        Map<ResourceType, Integer> map = new EnumMap<>(ResourceType.class);
//        List<ResourceDTO> recursos = Resource.getAllFromServer();
//        for (ResourceDTO recurso : recursos) {
//            ResourceType type = ResourceType.valueOf(recurso.getType());
//            map.put(type, map.getOrDefault(type, 0) + 1);
//        }
//        for (int i = 0; i < ResourceType.values().length; i++) {
//            resourcesByType[i] = map.getOrDefault(ResourceType.values()[i], 0);
//        }
//    }*/
//
//    */
    public List<TaskDTO> calculateCompletedTasksPerDay() {
        List<TaskDTO> list = Task.getAllFromServer();
        Arrays.fill(completedTasksPerDay, 0);
        for (TaskDTO task : list) {
            if ("FINISHED".equals(task.getStatus()) && task.getEstimatedEndTimeDate() != null) {
                int index = task.getEstimatedEndTimeDate().getDayOfWeek().getValue() - 1;
                completedTasksPerDay[index]++;
            }
        }
        return list;
    }

    public List<NeedDTO> calculateNeedsPerType() {
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
        return needs;
    }

    public List<TaskDTO> calculateCompletedTasksPerType() {
        Arrays.fill(completedTasks, 0);
        Map<TaskType, Integer> map = new EnumMap<>(TaskType.class);
        List<TaskDTO> tasks = Task.getAllFromServer();
        for (TaskDTO task : tasks) {
            if (!"FINISHED".equals(task.getStatus())) {
                TaskType type = TaskType.valueOf(task.getType());
                map.put(type, map.getOrDefault(type, 0) + 1);
            }
        }
        for (int i = 0; i < TaskType.values().length; i++) {
            completedTasks[i] = map.getOrDefault(TaskType.values()[i], 0);
        }
        return tasks;
    }

    public List<VolunteerDTO> calculateVolunteersByTaskType() {
        Arrays.fill(volunteersCountByType, 0);
        Map<TaskType, Integer> map = new EnumMap<>(TaskType.class);
        List<VolunteerDTO> volunteers = Volunteer.getAllFromServer();
        for (VolunteerDTO v : volunteers) {
            for (String pref : v.getTaskPreferences()) {
                TaskType type = TaskType.valueOf(pref);
                map.put(type, map.getOrDefault(type, 0) + 1);
            }
        }
        for (int i = 0; i < TaskType.values().length; i++) {
            volunteersCountByType[i] = map.getOrDefault(TaskType.values()[i], 0);
        }
        return volunteers;
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

}
