package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData;

import com.storedobject.chart.*;
import org.pingu.domain.DTO.*;
import org.pingu.domain.enums.ResourceType;
import org.pingu.domain.enums.TaskType;
import org.pingu.domain.enums.ResourceType;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.ChartType;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.Dashboard;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;
import org.pinguweb.frontend.services.BackendDTOService;

import java.time.LocalDate;
import java.util.*;

public class ChartGenerator {

    private final Integer[] completedTasksPerDay = new Integer[7];
    private final Integer[] needsByTaskType = new Integer[TaskType.values().length];
    private final Integer[] completedTasks = new Integer[TaskType.values().length];
    private final Integer[] volunteersCountByType = new Integer[TaskType.values().length];
    private final Integer[] tasksPerUrgency = new Integer[3];
    private final Integer[] resourcesByType = new Integer[ResourceType.values().length];

    private final List<TaskDTO> tasks;
    private final List<VolunteerDTO> volunteers;
    private final List<AffectedDTO> affecteds;
    private final List<NeedDTO> needs;
    private final List<ResourceDTO> resources;
    private final ChartDatasetGenerator dataGenerator;

    //TODO: Recuerda convertirlo en observador

    public ChartGenerator(){
        BackendDTOService service = BackendDTOService.GetInstancia();
        volunteers = service.getVolunteerList().getValues();
        affecteds = service.getAffectedList().getValues();
        needs = service.getNeedList().getValues();
        tasks = service.getTaskList().getValues();
        resources = service.getResourceList().getValues();
        dataGenerator = new ChartDatasetGenerator();
    }

    public List<InterfaceComponent> buildCompletedTasksChart(ChartType[] types) {
        List<List<TaskDTO>> completedTasksByType = dataGenerator.calculateCompletedTasksPerType(completedTasks, this.tasks);

        String[] allLabels = Arrays.stream(TaskType.values())
                .map(TaskType::name)
                .toArray(String[]::new);

        List<String> lbl = new ArrayList<>();
        List<Integer> val = new ArrayList<>();
        for (int i = 0; i < allLabels.length; i++) {
            int v = completedTasks[i] != null ? completedTasks[i] : 0;
            if (v > 0) {
                lbl.add(allLabels[i]);
                val.add(v);
            }
        }
        String[] labels = lbl.toArray(new String[0]);
        Integer[] values = val.toArray(new Integer[0]);

        // Generar paleta de colores
        Color[] palette = dataGenerator.generateColorPalette(labels.length);

        // Crear dashboards
        List<InterfaceComponent> dashboards = new ArrayList<>();
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
                    completedTasksByType.stream()
                            .map(lista -> lista.toArray(TaskDTO[]::new))
                            .toArray(TaskDTO[][]::new),
                    "Tareas Completadas",
                    palette
            );
            dashboards.add(d);
        }
        return dashboards;
    }

    public List<InterfaceComponent> tasksToday(ChartType[] types) {
        List<List<TaskDTO>> tasksByUrgency = dataGenerator.calculateTaskPerUrgency(tasksPerUrgency, this.tasks);

        System.out.println("Tareas no completadas: " + Arrays.toString(tasksPerUrgency));

        String[] labels = {"URGENT", "MODERATE", "LOW"};
        Integer[] values = tasksPerUrgency;

        // Generar la paleta de colores
        Color[] palette = dataGenerator.generateColorPalette(labels.length);

        // Crear dashboards
        List<InterfaceComponent> dashboards = new ArrayList<>();
        for (ChartType type : types) {
            Dashboard d = Dashboard.createSimpleDashboard(
                    "Nivel de urgencia de tareas",
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
                    tasksByUrgency.stream()
                            .map(lista -> lista.toArray(TaskDTO[]::new))
                            .toArray(TaskDTO[][]::new),
                    "Tareas de Hoy",
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

    public List<InterfaceComponent> buildUncompletedTasksLineChart(ChartType[] types) {
        Integer[] uncompletedTasks = new Integer[TaskType.values().length];
        Arrays.fill(uncompletedTasks, 0);

        List<List<TaskDTO>> uncompletedTasksByType = dataGenerator.calculateUnfinishedTasksByType(uncompletedTasks, this.tasks);


        String[] allLabels = Arrays.stream(TaskType.values())
                .map(TaskType::name)
                .toArray(String[]::new);

        List<String> filteredLabels = new ArrayList<>();
        List<Integer> filteredValues = new ArrayList<>();
        for (int i = 0; i < allLabels.length; i++) {
            int value = uncompletedTasks[i] != null ? uncompletedTasks[i] : 0;
            if (value > 0) {
                filteredLabels.add(allLabels[i]);
                filteredValues.add(value);
            }
        }

        Color[] palette = dataGenerator.generateColorPalette(filteredLabels.size());
        List<InterfaceComponent> dashboards = new ArrayList<>();
        for (ChartType type : types) {
            Dashboard d = Dashboard.createSimpleDashboard(
                    "Tareas No Completadas (LineChart)",
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
                    uncompletedTasksByType.stream()
                            .map(lista -> lista.toArray(TaskDTO[]::new))
                            .toArray(TaskDTO[][]::new),
                    "Tareas No Completadas",
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



    public List<InterfaceComponent> buildResourcesByTypeChart(ChartType[] types) {
        List<List<ResourceDTO>> resources= dataGenerator.calculateResourcesByType(resourcesByType, this.resources);

        String[] allLabels = Arrays.stream(ResourceType.values())
                .map(ResourceType::name)
                .toArray(String[]::new);

        List<String> filteredLabels = new ArrayList<>();
        List<Integer> filteredValues = new ArrayList<>();
        for (int i = 0; i < allLabels.length; i++) {
            int value = resourcesByType[i] != null ? resourcesByType[i] : 0;
            if (value > 0) {
                filteredLabels.add(allLabels[i]);
                filteredValues.add(value);
            }
        }

        String[] labels = filteredLabels.toArray(new String[0]);
        Integer[] values = filteredValues.toArray(new Integer[0]);

        Color[] palette = dataGenerator.generateColorPalette(labels.length);

        List<InterfaceComponent> dashboards = new ArrayList<>();
        for (ChartType type : types) {
            Dashboard d = Dashboard.createSimpleDashboard(
                    "Recursos por Tipo",
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
                            .map(e -> new Etiqueta[]{e})
                            .toArray(Etiqueta[][]::new),
                    values,
                    Arrays.stream(values)
                            .map(v -> new Integer[]{v})
                            .toArray(Integer[][]::new),
                    "Recursos por Tipo",
                    palette
            );
            dashboards.add(d);
        }
        return dashboards;
    }

    public List<InterfaceComponent> buildResourcesByTypePieChart(ChartType[] types) {
        List<List<ResourceDTO>> resources= dataGenerator.calculateResourcesByType(resourcesByType, this.resources);

        String[] allLabels = Arrays.stream(ResourceType.values())
                .map(ResourceType::name)
                .toArray(String[]::new);

        List<String> filteredLabels = new ArrayList<>();
        List<Integer> filteredValues = new ArrayList<>();
        for (int i = 0; i < allLabels.length; i++) {
            int value = resourcesByType[i] != null ? resourcesByType[i] : 0;
            if (value > 0) {
                filteredLabels.add(allLabels[i]);
                filteredValues.add(value);
            }
        }

        String[] labels = filteredLabels.toArray(new String[0]);
        Integer[] values = filteredValues.toArray(new Integer[0]);

        Color[] palette = dataGenerator.generateColorPalette(labels.length);

        List<InterfaceComponent> dashboards = new ArrayList<>();
        for (ChartType type : types) {
            Dashboard d = Dashboard.createSimpleDashboard(
                    "Recursos por Tipo",
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
                            .map(e -> new Etiqueta[]{e})
                            .toArray(Etiqueta[][]::new),
                    values,
                    Arrays.stream(values)
                            .map(v -> new Integer[]{v})
                            .toArray(Integer[][]::new),
                    "Recursos por Tipo",
                    palette
            );
            dashboards.add(d);
        }
        return dashboards;
    }




}