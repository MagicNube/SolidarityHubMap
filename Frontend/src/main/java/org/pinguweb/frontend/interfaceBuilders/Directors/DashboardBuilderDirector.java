package org.pinguweb.frontend.interfaceBuilders.Directors;

import com.storedobject.chart.*;
import com.vaadin.flow.component.Component;
import org.pingu.domain.DTO.AffectedDTO;
import org.pingu.domain.DTO.NeedDTO;
import org.pingu.domain.DTO.TaskDTO;
import org.pingu.domain.DTO.VolunteerDTO;
import org.pingu.domain.enums.TaskType;
import org.pinguweb.frontend.interfaceBuilders.Builders.DashboardBuilder;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.Dashboard;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData.ChartData;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData.Filters;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData.TestString;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;
import org.pinguweb.frontend.mapObjects.Need;
import org.pinguweb.frontend.mapObjects.Task;
import org.pinguweb.frontend.mapObjects.Volunteer;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.ChartType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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

        Dashboard first = Dashboard.createSimpleDashboard("Test 1", ChartType.BAR, new RectangularCoordinate(
                new XAxis(DataType.CATEGORY),
                new YAxis(DataType.NUMBER))
        );

        first.addData(
                new Object[]{fs.getName(), sn.getName(), tr.getName(), fr.getName()},
                new TestString[]{fs, sn, tr, fr},
                new Object[]{1, 2, 3, 4},
                new Integer[]{1, 2, 3, 4},
                "Mis datos",
                new Color(0, 0, 255)
        );

        first.addData(
                new Object[]{fs.getName(), sn.getName(), tr.getName(), fr.getName()},
                new TestString[]{fs, sn, tr, fr},
                new Object[]{1, 2, 3, 4},
                new Integer[]{1, 2, 3, 4},
                "Mis datos 2",
                new Color(0, 255, 0)
        );

        firstFilter.addDashboard(first);

        builder.reset();

        return builder
                .setTile("Test")
                .setSubtitle("Doble test")
                .addBelow(firstFilter)
                .addBelow(first)
                .build();
    }

    //tareas completadas por dias
    public Component buildCompletedTasksChart() {
        String[] daysOfWeek = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        //Actualiza los datos con la base de datos
        calculatedDays();

        Color dayColors = new Color(0, 0, 0);
        // Configurar el Dashboard
//        Dashboard completedTasksChart = Dashboard.builder()
//                .name("Tareas Completadas por Día")
//                .colors(dayColors)
//                .data(
//                        new ChartData<>(
//                                daysOfWeek, // Etiquetas del eje X
//                                completedTasksPerDay, // Valores del eje Y
//                                daysOfWeek, // Objetos de etiquetas
//                                completedTasksPerDay // Objetos de valores
//                        )
//                )
//                .coordinateConfiguration(
//                        new RectangularCoordinate(
//                                new XAxis(DataType.CATEGORY), // Eje X categórico
//                                new YAxis(DataType.NUMBER) // Eje Y numérico
//                        )
//                )
//                .type(ChartType.BAR) // Tipo de gráfica: Barras
//                .width("100%")
//                .height("500px")
//                .build();

        builder.reset();
//        builder.addBelow(completedTasksChart);

        return builder.build();
    }

    // Gnecesidades no cubiertas por tasktype
    public Component buildUncoveredNeedsChart() {
        needsPerType();

        // Obtener los valores de TaskType
        TaskType[] taskTypes = TaskType.values();
        String[] taskTypeLabels = Arrays.stream(taskTypes).map(TaskType::name).toArray(String[]::new);

        Color typeColors = new Color(0, 0, 1); // Color por defectoº

        // Configurar el Dashboard
//        Dashboard uncoveredNeedsChart = Dashboard.builder()
//                .name("Necesidades No Cubiertas por Tipo de Tarea")
//                .colors(typeColors)
//                .data(
//                        new ChartData<>(
//                                taskTypeLabels, // Etiquetas del eje X
//                                needsByTaskType, // Valores del eje Y
//                                taskTypeLabels, // Objetos de etiquetas
//                                needsByTaskType // Objetos de valores
//                        )
//                )
//                .coordinateConfiguration(
//                        new RectangularCoordinate(
//                                new XAxis(DataType.CATEGORY),
//                                new YAxis(DataType.NUMBER)
//                        )
//                )
//                .type(ChartType.BAR)
//                .width("100%")
//                .height("500px")
//                .build();

        // Builder para construir el componente final
        builder.reset();
//        builder.addBelow(uncoveredNeedsChart);

        return builder.build();
    }
// Tareas no terminadas por tasktype
    public Component buildUncoveredTaskTypeChart() {
        TasksperType();

        // Obtener los valores de TaskType
        TaskType[] taskTypes = TaskType.values();
        String[] taskTypeLabels = Arrays.stream(taskTypes).map(TaskType::name).toArray(String[]::new);


        Color typeColors = new Color(0, 0, 1); // Color por defectoº

        // Configurar el Dashboard
//        Dashboard uncoveredNeedsChart = Dashboard.builder()
//                .name("Tareas no cubiertas por tipo de tarea")
//                .colors(typeColors)
//                .data(
//                        new ChartData<>(
//                                taskTypeLabels, // Etiquetas del eje X
//                                completedTasks, // Valores del eje Y
//                                taskTypeLabels, // Objetos de etiquetas
//                                completedTasks // Objetos de valores
//                        )
//                )
//                .coordinateConfiguration(
//                        new RectangularCoordinate(
//                                new XAxis(DataType.CATEGORY),
//                                new YAxis(DataType.NUMBER)
//                        )
//                )
//                .type(ChartType.BAR)
//                .width("100%")
//                .height("500px")
//                .build();

        // Builder para construir el componente final
        builder.reset();
//        builder.addBelow(uncoveredNeedsChart);

        return builder.build();
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
    public void VolunteerPerSkill(){
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


