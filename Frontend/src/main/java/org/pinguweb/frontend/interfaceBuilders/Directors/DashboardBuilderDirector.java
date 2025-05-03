package org.pinguweb.frontend.interfaceBuilders.Directors;

import com.storedobject.chart.*;
import com.vaadin.flow.component.Component;
import org.pingu.domain.DTO.AffectedDTO;
import org.pingu.domain.DTO.NeedDTO;
import org.pingu.domain.DTO.TaskDTO;
import org.pingu.domain.DTO.VolunteerDTO;
import org.pingu.domain.enums.TaskType;
import org.pinguweb.frontend.interfaceBuilders.Builders.DashboardBuilder;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.ChartType;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.DashboardData.ChartData;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.DashboardData.TestString;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Filters;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;
import org.pinguweb.frontend.mapObjects.Need;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DashboardBuilderDirector {
    DashboardBuilder builder = new DashboardBuilder();
    int[] completedTasksPerDay = new int[7];
    int[] needsByTaskType = new int[TaskType.values().length];
    Integer[] completedTasks = new Integer[TaskType.values().length];

    //Sacar las necesidades y tareas de la BD
    BackendObject<List<NeedDTO>> needs = BackendService.getListFromBackend(BackendService.BACKEND + "/api/need/(ID)",
            new ParameterizedTypeReference<List<NeedDTO>>() {
            });
    BackendObject<List<TaskDTO>> task = BackendService.getListFromBackend(BackendService.BACKEND + "/api/task",
            new ParameterizedTypeReference<List<TaskDTO>>() {
            });


    public Component buildTest() {
        TestString fs = new TestString("TasksCr", LocalDate.now(), LocalDateTime.now(), 1, false);
        TestString sn = new TestString("TasksCo", LocalDate.now().minusDays(1), LocalDateTime.now().minusDays(1), 2, false);
        TestString tr = new TestString("NeedsCr", LocalDate.now().minusDays(2), LocalDateTime.now().minusDays(2), 3, true);
        TestString fr = new TestString("NeedsCo", LocalDate.now().minusDays(3), LocalDateTime.now().minusDays(3), 4, true);

        Filters firstFilter = Filters.builder().build();
        Filters secondFilter = Filters.builder().build();

        Dashboard first = Dashboard.builder()
                .name("test 1")
                .colors(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)))
                .data(
                        new ChartData<>(
                                new Object[]{fs.getName(), sn.getName(), tr.getName(), fr.getName()},
                                new Object[]{1, 2, 3, 4},
                                new TestString[]{fs, sn, tr, fr},
                                new Integer[]{1, 2, 3, 4}
                        )
                )
                .coordinateConfiguration(
                        new RectangularCoordinate(
                                new XAxis(DataType.CATEGORY),
                                new YAxis(DataType.NUMBER)
                        )
                )
                .type(ChartType.BAR)
                .width("100%")
                .height("700px")
                .build();

        Dashboard second = Dashboard.builder()
                .name("test 2")
                .colors(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)))
                .data(
                        new ChartData<>(
                                new Object[]{fs.getName(), sn.getName(), tr.getName(), fr.getName()},
                                new Object[]{1, 2, 3, 4},
                                new TestString[]{fs, sn, tr, fr},
                                new Integer[]{1, 2, 3, 4}
                        )
                )
                .coordinateConfiguration(
                        new RectangularCoordinate(
                                new XAxis(DataType.CATEGORY),
                                new YAxis(DataType.NUMBER)
                        )
                )
                .type(ChartType.BAR)
                .width("100%")
                .height("500px")
                .build();

        Dashboard third = Dashboard.builder()
                .name("test 3")
                .colors(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)))
                .data(
                        new ChartData<>(
                                new Object[]{fs.getName(), sn.getName(), tr.getName(), fr.getName()},
                                new Object[]{1, 2, 3, 4},
                                new TestString[]{fs, sn, tr, fr},
                                new Integer[]{1, 2, 3, 4}
                        )
                )
                .coordinateConfiguration(
                        new RectangularCoordinate(
                                new XAxis(DataType.CATEGORY),
                                new YAxis(DataType.NUMBER)
                        )
                )
                .type(ChartType.BAR)
                .width("100%")
                .height("500px")
                .build();

        Dashboard forth = Dashboard.builder()
                .name("test 4")
                .colors(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)))
                .data(
                        new ChartData<>(
                                new Object[]{fs.getName(), sn.getName(), tr.getName(), fr.getName()},
                                new Object[]{1, 2, 3, 4},
                                new TestString[]{fs, sn, tr, fr},
                                new Integer[]{1, 2, 3, 4}
                        )
                )
                .coordinateConfiguration(
                        new RectangularCoordinate(
                                new XAxis(DataType.CATEGORY),
                                new YAxis(DataType.NUMBER)
                        )
                )
                .type(ChartType.BAR)
                .width("100%")
                .height("500px")
                .build();

        firstFilter.addDashboard(first);

        secondFilter.addDashboard(second);
        secondFilter.addDashboard(third);
        secondFilter.addDashboard(forth);

        builder.reset();

        third.addBelowComponent(forth);

        List<InterfaceComponent> sides = new ArrayList<>();
        sides.add(second);
        sides.add(third);

        return builder
                .setTile("Test")
                .setSubtitle("Doble test")
                .addBelow(firstFilter)
                .addBelow(first)
                .addBelow(secondFilter)
                .addSide(sides)
                .build();
    }

    // GRAFICA DE PRUEBA CON BUILDER DE TAREAS
    public Component buildCompletedTasksChart() {
        String[] daysOfWeek = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

        //Actualiza los datos con la base de datos
        calculatedDays();

        // CompletedTasksPerDay a Integer[]
        Integer[] completedTasksPerDayObjects = new Integer[completedTasksPerDay.length];
        for (int i = 0; i < completedTasksPerDay.length; i++) {
            completedTasksPerDayObjects[i] = completedTasksPerDay[i];
        }

        Color dayColors = new Color(0, 0, 0);
        // Configurar el Dashboard
        Dashboard completedTasksChart = Dashboard.builder()
                .name("Tareas Completadas por Día")
                .colors(dayColors) // Asignar colores específicos
                .data(
                        new ChartData<>(
                                daysOfWeek, // Etiquetas del eje X
                                completedTasksPerDayObjects, // Valores del eje Y
                                daysOfWeek, // Objetos de etiquetas
                                completedTasksPerDayObjects // Objetos de valores
                        )
                )
                .coordinateConfiguration(
                        new RectangularCoordinate(
                                new XAxis(DataType.CATEGORY), // Eje X categórico
                                new YAxis(DataType.NUMBER) // Eje Y numérico
                        )
                )
                .type(ChartType.BAR) // Tipo de gráfica: Barras
                .width("100%")
                .height("500px")
                .build();

        // Builder para construir el componente final
        builder.reset();
        builder.addBelow(completedTasksChart);

        return builder.build();
    }

    // GRAFICA DE PRUEBA CON BUILDER DE NECESIDADES
    public Component buildUncoveredNeedsChart() {
        //necesidadesNoCubiertas();

        // Obtener los valores de TaskType
        TaskType[] taskTypes = TaskType.values();
        String[] taskTypeLabels = Arrays.stream(taskTypes).map(TaskType::name).toArray(String[]::new);

        // datos de needsByTaskType -> Integer[]
        Integer[] needsByTaskTypeObjects = Arrays.stream(needsByTaskType).boxed().toArray(Integer[]::new);

        Color typeColors = new Color(0, 0, 1); // Color por defectoº

        // Configurar el Dashboard
        Dashboard uncoveredNeedsChart = Dashboard.builder()
                .name("Necesidades No Cubiertas por Tipo de Tarea")
                .colors(typeColors)
                .data(
                        new ChartData<>(
                                taskTypeLabels, // Etiquetas del eje X
                                needsByTaskTypeObjects, // Valores del eje Y
                                taskTypeLabels, // Objetos de etiquetas
                                needsByTaskTypeObjects // Objetos de valores
                        )
                )
                .coordinateConfiguration(
                        new RectangularCoordinate(
                                new XAxis(DataType.CATEGORY),
                                new YAxis(DataType.NUMBER)
                        )
                )
                .type(ChartType.BAR)
                .width("100%")
                .height("500px")
                .build();

        // Builder para construir el componente final
        builder.reset();
        builder.addBelow(uncoveredNeedsChart);

        return builder.build();
    }

    public Component buildUncoveredTaskTypeChart() {
        TasksInitialized();

        // Obtener los valores de TaskType
        TaskType[] taskTypes = TaskType.values();
        String[] taskTypeLabels = Arrays.stream(taskTypes).map(TaskType::name).toArray(String[]::new);


        Color typeColors = new Color(0, 0, 1); // Color por defectoº

        // Configurar el Dashboard
        Dashboard uncoveredNeedsChart = Dashboard.builder()
                .name("Tareas no cubiertas por tipo de tarea")
                .colors(typeColors)
                .data(
                        new ChartData<>(
                                taskTypeLabels, // Etiquetas del eje X
                                completedTasks, // Valores del eje Y
                                taskTypeLabels, // Objetos de etiquetas
                                completedTasks // Objetos de valores
                        )
                )
                .coordinateConfiguration(
                        new RectangularCoordinate(
                                new XAxis(DataType.CATEGORY),
                                new YAxis(DataType.NUMBER)
                        )
                )
                .type(ChartType.BAR)
                .width("100%")
                .height("500px")
                .build();

        // Builder para construir el componente final
        builder.reset();
        builder.addBelow(uncoveredNeedsChart);
        System.out.println(completedTasks[0]);

        return builder.build();
    }

    //PRUEBAS PARA GRAFICAS CON BUILDER
    public void calculatedDays() {
        // Inicializar el conteo de tareas completadas por día (0 = Lunes, 6 = Domingo)

        Arrays.fill(this.completedTasksPerDay, 0);
        if (task.getStatusCode() == HttpStatus.OK) {

            for (TaskDTO task : task.getData()) {
                if (task.getStatus().equals("FINISHED") && task.getEstimatedEndTimeDate() != null) {
                    // Obtener el día de la semana (1 = Lunes, 7 = Domingo)
                    int dayOfWeek = task.getEstimatedEndTimeDate().getDayOfWeek().getValue();
                    // Convertir a índice del array (0 = Lunes, 6 = Domingo)
                    int index = (dayOfWeek - 1);
                    completedTasksPerDay[index]++;
                }
            }
        }
    }

    //PRUEBAS PARA GRAFICAS CON BUILDER
    public void necesidadesNoCubiertas() {

        TaskType[] tasks = TaskType.values();


        for (NeedDTO n : needs.getData()) {
            if (!n.getStatus().equals("FINISHED")) {
                for (int i = 0; i < tasks.length; i++) {
                    if (n.getNeedType().equals(tasks[i].name())) {
                        needsByTaskType[i]++;
                        break;
                    }
                }
            }
        }
    }

    public void TasksInitialized() {
        TaskType[] tasks = TaskType.values();

        Arrays.fill(this.completedTasks, 0);
        if (task.getStatusCode() == HttpStatus.OK) {
            for (TaskDTO taskk : task.getData()) {
                //TODO: SI RENTA PODEMOS RELLENAR UN ARRAY CON LAS TAREAS Y NEEDS QUE YA ACABARON AUNQUE PODRÍA GENERAR PORBLEMAS SI HAY MUCHAS QUE FINALIZARON
                if (!taskk.getStatus().equals("FINISHED")) {
                    for (int i = 0; i < tasks.length; i++) {
                        if (taskk.getType().equals(tasks[i].name())) {
                            this.completedTasks[i]++;
                            break;
                        }
                    }
                }
            }
        }
    }
}


