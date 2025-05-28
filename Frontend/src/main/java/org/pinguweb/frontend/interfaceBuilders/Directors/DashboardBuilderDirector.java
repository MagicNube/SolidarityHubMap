package org.pinguweb.frontend.interfaceBuilders.Directors;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.pinguweb.frontend.interfaceBuilders.Builders.DashboardBuilder;
import org.pinguweb.frontend.interfaceBuilders.Builders.Interface;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.ChartType;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.Dashboard;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData.ChartGenerator;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData.Filters;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DashboardBuilderDirector {

    private final DashboardBuilder builder = new DashboardBuilder();
    @Getter
    private final List<Dashboard> dashboards = new ArrayList<>();

    public Interface get(){
        return builder.build();
    }

    public void buildComplete(){
        builder.reset();
        builder.setTile("Dashboards pingu web");
        builder.setSubtitle("Solidarity Hub");

        ChartGenerator generator = new ChartGenerator();


        List<InterfaceComponent> zero = generator.tasksToday(new ChartType[]{ChartType.BAR});
        List<InterfaceComponent> fisrtPair = generator.buildCompletedTasksChart(new ChartType[]{ChartType.LINECHART});
        List<InterfaceComponent> secondPair = generator.buildUncompletedTasksLineChart(new ChartType[]{ChartType.LINECHART});
        List<InterfaceComponent> thirdPair = generator.buildUncoveredNeedsChart(new ChartType[]{ChartType.PIE});
        List<InterfaceComponent> fourthPair = generator.buildVolunteersByTaskTypeChart(new ChartType[]{ChartType.PIE});
        List<InterfaceComponent> fifthPair = generator.buildVolunteersVSAffectedChart(new ChartType[]{ChartType.BAR, ChartType.PIE});
        List<InterfaceComponent> sixthPair = generator.buildResourcesByTypeChart(new ChartType[]{ChartType.BAR, ChartType.PIE});

        List<InterfaceComponent> completedAndUncompleted = new ArrayList<>();
        completedAndUncompleted.addAll(fisrtPair);
        completedAndUncompleted.addAll(secondPair);

        dashboards.clear();

        dashboards.add((Dashboard) zero.get(0));
        dashboards.add((Dashboard) fisrtPair.get(0));
        dashboards.add((Dashboard) secondPair.get(0));
        dashboards.add((Dashboard) thirdPair.get(0));
        dashboards.add((Dashboard) fourthPair.get(0));
        dashboards.add((Dashboard) fifthPair.get(0));
        dashboards.add((Dashboard) fifthPair.get(1));
        dashboards.add((Dashboard) sixthPair.get(0));

        // Filtros existentes para tareas completadas y no completadas
        Filters completedVsUncompletedFilters = Filters.builder().build();
        completedVsUncompletedFilters.addDashboard(completedAndUncompleted);

        builder.addBelow(completedVsUncompletedFilters);
        builder.addSide(completedAndUncompleted);

        // Nuevos filtros
        Filters urgencyLevelFilters = Filters.builder().build();
        urgencyLevelFilters.addDashboard(zero);

        Filters uncoveredNeedsFilters = Filters.builder().build();
        uncoveredNeedsFilters.addDashboard(thirdPair);

        Filters volunteersByTaskTypeFilters = Filters.builder().build();
        volunteersByTaskTypeFilters.addDashboard(fourthPair);

        Filters volunteersVsAffectedFilters = Filters.builder().build();
        volunteersVsAffectedFilters.addDashboard(fifthPair);

        Filters resourcesByTypeFilters = Filters.builder().build();
        resourcesByTypeFilters.addDashboard(sixthPair);

        // Agregar nuevos filtros y gráficas
        builder.addBelow(urgencyLevelFilters);
        builder.addSide(zero);

        builder.addBelow(uncoveredNeedsFilters);
        builder.addSide(thirdPair);

        builder.addBelow(volunteersByTaskTypeFilters);
        builder.addSide(fourthPair);

        builder.addBelow(volunteersVsAffectedFilters);
        builder.addSide(fifthPair);

        builder.addBelow(resourcesByTypeFilters);
        builder.addSide(sixthPair);

    }
}