package org.pinguweb.frontend.interfaceBuilders.Directors;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.pinguweb.frontend.interfaceBuilders.Builders.DashboardBuilder;
import org.pinguweb.frontend.interfaceBuilders.Builders.Interface;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.ChartType;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.Dashboard;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData.ChartGenerator;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData.Filters;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.GenericComponent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;
import org.pinguweb.frontend.view.InfoPopup;

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
        List<InterfaceComponent> sixthPair = generator.buildResourcesByTypePieChart(new ChartType[]{ChartType.BAR, ChartType.PIE});

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

        Filters zeroFilters = Filters.builder().build();
        //Filters firstPairFilters = Filters.builder().build();
        //Filters secondPairFilters = Filters.builder().build();
        Filters thirdPairFilters = Filters.builder().build();
        Filters fourthPairFilters = Filters.builder().build();
        Filters fifthPairFilters = Filters.builder().build();
        Filters sixthPairFilters = Filters.builder().build();

        Filters completedVsUncompletedFilters = Filters.builder().build();
        completedVsUncompletedFilters.addDashboard(completedAndUncompleted);


        builder.addBelow(completedVsUncompletedFilters);
        builder.addSide(completedAndUncompleted);

        zeroFilters.addDashboard(zero);
        //firstPairFilters.addDashboard(fisrtPair);
        //secondPairFilters.addDashboard(secondPair);
        thirdPairFilters.addDashboard(thirdPair);
        fourthPairFilters.addDashboard(fourthPair);
        fifthPairFilters.addDashboard(fifthPair);
        sixthPairFilters.addDashboard(sixthPair);

        builder.addBelow(zeroFilters);
        builder.addSide(zero);
        builder.addSide(thirdPair);
        builder.addBelow(thirdPairFilters);;
        builder.addBelow(fourthPairFilters);
        builder.addSide(fourthPair);
        builder.addSide(fifthPair);
        builder.addSide(sixthPair);
        builder.addBelow(sixthPairFilters);
        builder.addBelow(GenericComponent.builder().component(InfoPopup.DashboardPopup()).build());

    }
}