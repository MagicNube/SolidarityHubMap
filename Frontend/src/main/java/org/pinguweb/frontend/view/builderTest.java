package org.pinguweb.frontend.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.pinguweb.frontend.interfaceBuilders.Directors.DashboardBuilderDirector;

@Route("test")
public class builderTest extends VerticalLayout {

    public builderTest(){
        DashboardBuilderDirector director = new DashboardBuilderDirector();
        this.add(director.buildTest());
        this.add(director.buildUncoveredNeedsChart());
        this.add(director.buildCompletedTasksChart());
        this.add(director.buildUncoveredTaskTypeChart());
    }

}
