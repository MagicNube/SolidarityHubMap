package org.pinguweb.frontend.interfaceBuilders.Directors;

import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.AffectedDTO;
import org.pingu.domain.DTO.NeedDTO;
import org.pingu.domain.DTO.TaskDTO;
import org.pingu.domain.DTO.VolunteerDTO;
import org.pingu.domain.enums.TaskType;
import org.pinguweb.frontend.interfaceBuilders.Builders.DashboardBuilder;
import org.pinguweb.frontend.interfaceBuilders.Builders.Interface;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.ChartType;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData.ChartGenerator;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.DashboardData.Filters;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DashboardBuilderDirector {

    private final DashboardBuilder builder = new DashboardBuilder();

    public Interface get(){
        return builder.build();
    }

    public void buildComplete(){
        builder.reset();
        builder.setTile("Dashboards pingu web");
        builder.setSubtitle("Solidarity Hub");

        ChartGenerator generator = new ChartGenerator();

        List<InterfaceComponent> fisrtPair = generator.buildCompletedTasksChart(new ChartType[]{ChartType.BAR, ChartType.PIE});
        List<InterfaceComponent> secondPair = generator.buildUncoveredNeedsChart(new ChartType[]{ChartType.BAR, ChartType.PIE});
        List<InterfaceComponent> thirdPair = generator.buildUncoveredTaskTypeChart(new ChartType[]{ChartType.BAR, ChartType.PIE});
        List<InterfaceComponent> fourthPair = generator.buildVolunteersByTaskTypeChart(new ChartType[]{ChartType.BAR, ChartType.PIE});
        List<InterfaceComponent> fifthPair = generator.buildVolunteersVSAffectedChart(new ChartType[]{ChartType.BAR, ChartType.PIE});

        Filters firstPairFilters = Filters.builder().build();
        Filters secondPairFilters = Filters.builder().build();
        Filters thirdPairFilters = Filters.builder().build();
        Filters fourthPairFilters = Filters.builder().build();

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
