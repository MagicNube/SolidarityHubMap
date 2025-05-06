package org.pinguweb.frontend.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.pinguweb.frontend.interfaceBuilders.Directors.DashboardBuilderDirector;

@Route("dashboard")
public class DashboardNewView extends VerticalLayout {

    public DashboardNewView(){
        DashboardBuilderDirector director = new DashboardBuilderDirector();
        director.buildComplete();
        this.add(director.get().getInterface());
    }

}
