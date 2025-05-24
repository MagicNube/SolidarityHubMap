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
        List<InterfaceComponent> fisrtPair = generator.buildCompletedTasksChart(new ChartType[]{ChartType.BAR, ChartType.PIE});
        List<InterfaceComponent> secondPair = generator.buildUncoveredNeedsChart(new ChartType[]{ChartType.BAR, ChartType.PIE});
        List<InterfaceComponent> thirdPair = generator.buildUncoveredTaskTypeChart(new ChartType[]{ChartType.BAR, ChartType.PIE});
        List<InterfaceComponent> fourthPair = generator.buildVolunteersByTaskTypeChart(new ChartType[]{ChartType.BAR, ChartType.PIE});
        List<InterfaceComponent> fifthPair = generator.buildVolunteersVSAffectedChart(new ChartType[]{ChartType.BAR, ChartType.PIE});

        dashboards.clear();

        dashboards.add((Dashboard) zero.get(0));
        dashboards.add((Dashboard) fisrtPair.get(0));
        dashboards.add((Dashboard) fisrtPair.get(1));
        dashboards.add((Dashboard) secondPair.get(0));
        dashboards.add((Dashboard) secondPair.get(1));
        dashboards.add((Dashboard) thirdPair.get(0));
        dashboards.add((Dashboard) thirdPair.get(1));
        dashboards.add((Dashboard) fourthPair.get(0));
        dashboards.add((Dashboard) fourthPair.get(1));
        dashboards.add((Dashboard) fifthPair.get(0));
        dashboards.add((Dashboard) fifthPair.get(1));

        Filters zeroFilters = Filters.builder().build();
        Filters firstPairFilters = Filters.builder().build();
        Filters secondPairFilters = Filters.builder().build();
        Filters thirdPairFilters = Filters.builder().build();
        Filters fourthPairFilters = Filters.builder().build();

        zeroFilters.addDashboard(zero);
        firstPairFilters.addDashboard(fisrtPair);
        secondPairFilters.addDashboard(secondPair);
        thirdPairFilters.addDashboard(thirdPair);
        fourthPairFilters.addDashboard(fourthPair);

        builder.addBelow(firstPairFilters);
        builder.addSide(fisrtPair);
        builder.addBelow(secondPairFilters);
        builder.addSide(secondPair);
        builder.addBelow(thirdPairFilters);;
        builder.addSide(thirdPair);
        builder.addBelow(fourthPairFilters);
        builder.addSide(fourthPair);
        builder.addSide(fifthPair);
    }
}