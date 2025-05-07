package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData;

import com.storedobject.chart.*;
import org.pingu.domain.DTO.AffectedDTO;
import org.pingu.domain.DTO.NeedDTO;
import org.pingu.domain.DTO.TaskDTO;
import org.pingu.domain.DTO.VolunteerDTO;
import org.pingu.domain.enums.TaskType;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.ChartType;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.Dashboard;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;
import org.pinguweb.frontend.mapObjects.Affected;
import org.pinguweb.frontend.mapObjects.Need;
import org.pinguweb.frontend.mapObjects.Task;
import org.pinguweb.frontend.mapObjects.Volunteer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChartGenerator {

    private final Integer[] completedTasksPerDay = new Integer[7];
    private final Integer[] needsByTaskType = new Integer[TaskType.values().length];
    private final Integer[] completedTasks = new Integer[TaskType.values().length];
    private final Integer[] volunteersCountByType = new Integer[TaskType.values().length];
    private final Integer[] resourcesByType = new Integer[org.example.coordinacionbdsolidarityhub.model.enums.ResourceType.values().length];

    private final List<TaskDTO> tasks;
    private final List<VolunteerDTO> volunteers;
    private final List<AffectedDTO> affecteds;
    private final List<NeedDTO> needs;
    private final ChartDatasetGenerator dataGenerator;

    public ChartGenerator(){
        volunteers = Volunteer.getAllFromServer();
        affecteds = Affected.getAllFromServer();
        needs = Need.getAllFromServer();
        tasks = Task.getAllFromServer();
        dataGenerator = new ChartDatasetGenerator();
    }

    public List<InterfaceComponent> buildCompletedTasksChart(ChartType[] types){
        List<List<TaskDTO>> tasks = dataGenerator.calculateCompletedTasksPerDay(completedTasksPerDay, this.tasks);

        String[] labels = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        Integer[] values = completedTasksPerDay;

        List<InterfaceComponent> dashboards = new ArrayList<>();

        Color[] palette = dataGenerator.generateColorPalette(labels.length);

        for (ChartType type : types) {
            Dashboard d = Dashboard.createSimpleDashboard(
                    "Tareas Completadas",
                    type,
                    new RectangularCoordinate(
                            new XAxis(DataType.CATEGORY),
                            new YAxis(DataType.NUMBER)
                    )
            );
            d.addData(
                    labels,
                    Arrays.stream(labels)
                            .map(Etiqueta::new)
                            .map(fs -> new Etiqueta[]{fs})
                            .toArray(Etiqueta[][]::new),
                    values,
                    tasks.stream()
                            .map(lista -> lista.toArray(TaskDTO[]::new))
                            .toArray(TaskDTO[][]::new),
                    "Tareas Completadas",
                    palette
            );
            dashboards.add(d);
        }
        return dashboards;
    }

    public List<InterfaceComponent> buildUncoveredNeedsChart(ChartType[] types) {
        List<List<NeedDTO>> needs = dataGenerator.calculateNeedsPerType(needsByTaskType, this.needs);

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

        Color[] palette = dataGenerator.generateColorPalette(labels.length);
        List<InterfaceComponent> dashboards = new ArrayList<>();
        for (ChartType type : types) {
            Dashboard d = Dashboard.createSimpleDashboard(
                    "Necesidades no cubiertas",
                    type,
                    new RectangularCoordinate(
                            new XAxis(DataType.CATEGORY),
                            new YAxis(DataType.NUMBER)
                    )
            );
            d.addData(
                    labels,
                    Arrays.stream(labels)
                            .map(Etiqueta::new)
                            .map(fs -> new Etiqueta[]{fs})
                            .toArray(Etiqueta[][]::new),
                    values,
                    needs.stream()
                            .map(lista -> lista.toArray(NeedDTO[]::new))
                            .toArray(NeedDTO[][]::new),
                    "Necesidades No Cubiertas",
                    palette
            );
            dashboards.add(d);
        }
        return dashboards;
    }

    public List<InterfaceComponent> buildUncoveredTaskTypeChart(ChartType[] types) {
        List<List<TaskDTO>> tasks = dataGenerator.calculateCompletedTasksPerType(completedTasks, this.tasks);

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

        Color[] palette = dataGenerator.generateColorPalette(labels.length);
        List<InterfaceComponent> dashboards = new ArrayList<>();
        for (ChartType type : types) {
            Dashboard d = Dashboard.createSimpleDashboard(
                    "Tareas no terminadas",
                    type,
                    new RectangularCoordinate(
                            new XAxis(DataType.CATEGORY),
                            new YAxis(DataType.NUMBER)
                    )
            );
            d.addData(
                    labels,
                    Arrays.stream(labels)
                            .map(Etiqueta::new)
                            .map(fs -> new Etiqueta[]{fs})
                            .toArray(Etiqueta[][]::new),
                    values,
                    tasks.stream()
                            .map(lista -> lista.toArray(TaskDTO[]::new))
                            .toArray(TaskDTO[][]::new),
                    "Tareas No Terminadas",
                    palette
            );
            dashboards.add(d);
        }
        return dashboards;
    }

    public List<InterfaceComponent> buildVolunteersByTaskTypeChart(ChartType[] types) {
        List<List<VolunteerDTO>> volunteers = dataGenerator.calculateVolunteersByTaskType(volunteersCountByType, this.volunteers);
        String[] allLabels = Arrays.stream(TaskType.values())
                .map(TaskType::name)
                .toArray(String[]::new);

        List<String> filteredLabels = new ArrayList<>();
        List<Integer> filteredValues = new ArrayList<>();
        List<Color> filteredColors = new ArrayList<>();

        for (int i = 0; i < allLabels.length; i++) {
            int value = volunteersCountByType[i] != null ? volunteersCountByType[i] : 0;
            if (value > 0) {
                filteredLabels.add(allLabels[i]);
                filteredValues.add(value);
                filteredColors.add(dataGenerator.generateColorForType(allLabels[i]));
            }
        }

        List<InterfaceComponent> dashboards = new ArrayList<>();
        for (ChartType type : types) {
            Dashboard d = Dashboard.createSimpleDashboard(
                    "Voluntarios por tipo de tarea",
                    type,
                    new RectangularCoordinate(
                            new XAxis(DataType.CATEGORY),
                            new YAxis(DataType.NUMBER)
                    )
            );

            d.addData(
                    filteredLabels.toArray(new String[0]),
                    filteredLabels.stream()
                            .map(Etiqueta::new)
                            .map(e -> new Etiqueta[]{e})
                            .toArray(Etiqueta[][]::new),
                    filteredValues.toArray(new Integer[0]),
                    volunteers.stream()
                            .map(lista -> lista.toArray(VolunteerDTO[]::new))
                            .toArray(VolunteerDTO[][]::new),
                    "Distribución de voluntarios",
                    filteredColors.toArray(new Color[0])
            );

            dashboards.add(d);
        }
        return dashboards;
    }

    public List<InterfaceComponent> buildVolunteersVSAffectedChart(ChartType[] types) {

        List<VolunteerDTO> volunteers = this.volunteers;

        String[] labels = {"Voluntarios", "Afectados"};
        Integer[] values = {volunteers.size(), this.affecteds.size()};

        Color[] palette = dataGenerator.generateColorPalette(labels.length);
        List<InterfaceComponent> dashboards = new ArrayList<>();
        for (ChartType type : types) {
            Dashboard d = Dashboard.createSimpleDashboard(
                    "Voluntarios frente a afectados",
                    type,
                    new RectangularCoordinate(
                            new XAxis(DataType.CATEGORY),
                            new YAxis(DataType.NUMBER)
                    )
            );
            d.addData(
                    labels,
                    Arrays.stream(labels)
                            .map(Etiqueta::new)
                            .map(fs -> new Etiqueta[]{fs})
                            .toArray(Etiqueta[][]::new),
                    values,
                    Arrays.stream(values)
                            .map(i -> new Integer[]{i})
                            .toArray(Integer[][]::new),
                    "Comparación",
                    palette
            );
            dashboards.add(d);
        }
        return dashboards;
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

    // Método auxiliar para generar colores consistentes

}
