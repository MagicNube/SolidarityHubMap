package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData;

import com.storedobject.chart.*;
import org.pingu.domain.DTO.AffectedDTO;
import org.pingu.domain.DTO.NeedDTO;
import org.pingu.domain.DTO.TaskDTO;
import org.pingu.domain.DTO.VolunteerDTO;
import org.pingu.domain.enums.TaskType;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.ChartType;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.Dashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChartGenerator {

    private Integer[] completedTasksPerDay = new Integer[7];
    private Integer[] needsByTaskType = new Integer[TaskType.values().length];
    private Integer[] completedTasks = new Integer[TaskType.values().length];
    private Integer[] volunteersCountByType = new Integer[TaskType.values().length];
    private Integer[] resourcesByType = new Integer[org.example.coordinacionbdsolidarityhub.model.enums.ResourceType.values().length];

    private List<TaskDTO> tasks = new ArrayList<>();
    private List<VolunteerDTO> volunteers = new ArrayList<>();
    private List<AffectedDTO> affecteds = new ArrayList<>();
    private List<NeedDTO> needs = new ArrayList<>();

    public Dashboard buildCompletedTasksChart() {
        List<List<TaskDTO>> tasks = calculateCompletedTasksPerDay();

        String[] labels = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        Integer[] values = completedTasksPerDay;

        Dashboard d = Dashboard.createSimpleDashboard(
                "Tareas Completadas",
                ChartType.BAR,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );
        d.addData(
                labels,
                Arrays.stream(labels)
                        .map(Etiqueta::new)
                        .map(fs -> new Etiqueta[]{ fs })
                        .toArray(Etiqueta[][]::new),
                values,
                tasks.stream()
                        .map(lista -> lista.toArray(TaskDTO[]::new))
                        .toArray(TaskDTO[][]::new),
                "Tareas Completadas",
                generateColorPalette(labels.length)
        );
        return d;
    }

    public Dashboard buildCompletedTasksPieChart() {
        List<List<TaskDTO>> tasks = calculateCompletedTasksPerDay();

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
                Arrays.stream(labels)
                        .map(Etiqueta::new)
                        .map(fs -> new Etiqueta[]{ fs })
                        .toArray(Etiqueta[][]::new),
                values,
                tasks.stream()
                        .map(lista -> lista.toArray(TaskDTO[]::new))
                        .toArray(TaskDTO[][]::new),
                "Tareas Completadas",
                generateColorPalette(labels.length)
        );
        return d;
    }

    public Dashboard buildUncoveredNeedsChart() {
        List<List<NeedDTO>> needs = calculateNeedsPerType();

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
                ChartType.BAR,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );
        d.addData(
                labels,
                Arrays.stream(labels)
                        .map(Etiqueta::new)
                        .map(fs -> new Etiqueta[]{ fs })
                        .toArray(Etiqueta[][]::new),
                values,
                needs.stream()
                        .map(lista -> lista.toArray(NeedDTO[]::new))
                        .toArray(NeedDTO[][]::new),
                "Necesidades No Cubiertas",
                generateColorPalette(labels.length)
        );
        return d;
    }

    public Dashboard buildUncoveredNeedsPieChart() {
        List<List<NeedDTO>> needs = calculateNeedsPerType();

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
                Arrays.stream(labels)
                        .map(Etiqueta::new)
                        .map(fs -> new Etiqueta[]{ fs })
                        .toArray(Etiqueta[][]::new),
                values,
                needs.stream()
                        .map(lista -> lista.toArray(NeedDTO[]::new))
                        .toArray(NeedDTO[][]::new),
                "Necesidades No Cubiertas",
                generateColorPalette(labels.length)
        );
        return d;
    }

    public Dashboard buildUncoveredTaskTypeChart() {
        List<List<TaskDTO>> tasks = calculateCompletedTasksPerType();

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
                ChartType.BAR,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );
        d.addData(
                labels,
                Arrays.stream(labels)
                        .map(Etiqueta::new)
                        .map(fs -> new Etiqueta[]{ fs })
                        .toArray(Etiqueta[][]::new),
                values,
                tasks.stream()
                        .map(lista -> lista.toArray(TaskDTO[]::new))
                        .toArray(TaskDTO[][]::new),
                "Tareas No Terminadas",
                generateColorPalette(labels.length)
        );
        return d;
    }

    public Dashboard buildUncoveredTaskTypePieChart() {
        List<List<TaskDTO>> tasks = calculateCompletedTasksPerType();

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
                Arrays.stream(labels)
                        .map(Etiqueta::new)
                        .map(fs -> new Etiqueta[]{ fs })
                        .toArray(Etiqueta[][]::new),
                values,
                tasks.stream()
                        .map(lista -> lista.toArray(TaskDTO[]::new))
                        .toArray(TaskDTO[][]::new),
                "Tareas No Terminadas",
                generateColorPalette(labels.length)
        );
        return d;
    }

    public Dashboard buildVolunteersByTaskTypeChart() {
        List<List<VolunteerDTO>> volunteers = calculateVolunteersByTaskType();
        String[] allLabels = Arrays.stream(TaskType.values())
                .map(TaskType::name)
                .toArray(String[]::new);

        // Filtrar igual que en el Bar Chart (solo valores > 0)
        List<String> filteredLabels = new ArrayList<>();
        List<Integer> filteredValues = new ArrayList<>();
        List<Color> filteredColors = new ArrayList<>();

        for (int i = 0; i < allLabels.length; i++) {
            int value = volunteersCountByType[i] != null ? volunteersCountByType[i] : 0;
            if (value > 0) {
                filteredLabels.add(allLabels[i]);
                filteredValues.add(value);
                filteredColors.add(generateColorForType(allLabels[i]));
            }
        }

        Dashboard d = Dashboard.createSimpleDashboard(
                "Voluntarios por tipo de tarea",
                ChartType.BAR,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );

        d.addData(
                filteredLabels.toArray(new String[0]),
                filteredLabels.stream()
                        .map(Etiqueta::new)
                        .map(e -> new Etiqueta[]{ e })
                        .toArray(Etiqueta[][]::new),
                filteredValues.toArray(new Integer[0]),
                volunteers.stream()
                        .map(lista -> lista.toArray(VolunteerDTO[]::new))
                        .toArray(VolunteerDTO[][]::new),
                "Distribución de voluntarios",
                filteredColors.toArray(new Color[0])
        );
        return d;
    }

    public Dashboard buildVolunteersByTaskTypePieChart() {
        List<List<VolunteerDTO>> volunteers = calculateVolunteersByTaskType();
        String[] allLabels = Arrays.stream(TaskType.values())
                .map(TaskType::name)
                .toArray(String[]::new);

        // Filtrar igual que en el Bar Chart (solo valores > 0)
        List<String> filteredLabels = new ArrayList<>();
        List<Integer> filteredValues = new ArrayList<>();
        List<Color> filteredColors = new ArrayList<>();

        for (int i = 0; i < allLabels.length; i++) {
            int value = volunteersCountByType[i] != null ? volunteersCountByType[i] : 0;
            if (value > 0) {
                filteredLabels.add(allLabels[i]);
                filteredValues.add(value);
                filteredColors.add(generateColorForType(allLabels[i]));
            }
        }

        Dashboard d = Dashboard.createSimpleDashboard(
                "Voluntarios por tipo de tarea",
                ChartType.PIE,
                new RectangularCoordinate(
                        new XAxis(DataType.CATEGORY),
                        new YAxis(DataType.NUMBER)
                )
        );

        d.addData(
                filteredLabels.toArray(new String[0]),
                filteredLabels.stream()
                        .map(Etiqueta::new)
                        .map(e -> new Etiqueta[]{ e })
                        .toArray(Etiqueta[][]::new),
                filteredValues.toArray(new Integer[0]),
                volunteers.stream()
                        .map(lista -> lista.toArray(VolunteerDTO[]::new))
                        .toArray(VolunteerDTO[][]::new),
                "Distribución de voluntarios",
                filteredColors.toArray(new Color[0])
        );
        return d;
    }

    public Dashboard buildVolunteersVSAffectedChart() {

        List<VolunteerDTO> volunteers = this.volunteers;
        List<AffectedDTO> affected = this.affecteds;

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
        d.addData(
                labels,
                Arrays.stream(labels)
                        .map(Etiqueta::new)
                        .map(fs -> new Etiqueta[]{ fs })
                        .toArray(Etiqueta[][]::new),
                values,
                Arrays.stream(values)
                        .map(i -> new Integer[]{ i })
                        .toArray(Integer[][]::new),
                "Comparación",
                generateColorPalette(labels.length)
        );
        return d;
    }

    public Dashboard buildVolunteersVSAffectedPieChart() {
        List<VolunteerDTO> volunteers = this.volunteers;
        List<AffectedDTO> affected = this.affecteds;

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
                Arrays.stream(labels)
                        .map(Etiqueta::new)
                        .map(fs -> new Etiqueta[]{ fs })
                        .toArray(Etiqueta[][]::new),
                values,
                Arrays.stream(values)
                        .map(i -> new Integer[]{ i })
                        .toArray(Integer[][]::new),
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

    // Método auxiliar para generar colores consistentes
    private Color generateColorForType(String type) {
        // Implementación que devuelve el mismo color para el mismo tipo de tarea
        return generateColorPalette(1)[0];
    }

    public List<List<TaskDTO>> calculateCompletedTasksPerDay() {
        Arrays.fill(completedTasksPerDay, 0);

        List<List<TaskDTO>> finishedTasks = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            finishedTasks.add(new ArrayList<>());
        }

        for (TaskDTO task : this.tasks) {
            if ("FINISHED".equals(task.getStatus())
                    && task.getEstimatedEndTimeDate() != null) {

                int dowIndex = task.getEstimatedEndTimeDate()
                        .getDayOfWeek()
                        .getValue() - 1;

                completedTasksPerDay[dowIndex]++;
                finishedTasks.get(dowIndex).add(task);
            }
        }

        return finishedTasks;
    }

    public List<List<NeedDTO>> calculateNeedsPerType() {
        Arrays.fill(needsByTaskType, 0);

        int typesCount = TaskType.values().length;
        List<List<NeedDTO>> needsPerType = new ArrayList<>(typesCount);
        for (int i = 0; i < typesCount; i++) {
            needsPerType.add(new ArrayList<>());
        }

        for (NeedDTO need : this.needs) {
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

    public List<List<TaskDTO>> calculateCompletedTasksPerType() {
        Arrays.fill(completedTasks, 0);
        int typesCount = TaskType.values().length;
        List<List<TaskDTO>> tasksByType = new ArrayList<>(typesCount);
        for (int i = 0; i < typesCount; i++) {
            tasksByType.add(new ArrayList<>());
        }
        for (TaskDTO task : this.tasks) {
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

    public List<List<VolunteerDTO>> calculateVolunteersByTaskType() {
        Arrays.fill(volunteersCountByType, 0);
        int typesCount = TaskType.values().length;
        List<List<VolunteerDTO>> volunteersByType = new ArrayList<>(typesCount);
        for (int i = 0; i < typesCount; i++) {
            volunteersByType.add(new ArrayList<>());
        }
        for (VolunteerDTO v : this.volunteers) {
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
