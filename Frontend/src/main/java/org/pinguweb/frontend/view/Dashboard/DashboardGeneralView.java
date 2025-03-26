package org.pinguweb.frontend.view.Dashboard;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.pinguweb.frontend.view.DashboardView;

@Route("dashboard/view/:type")
public class DashboardGeneralView extends VerticalLayout implements BeforeEnterObserver {

    public DashboardGeneralView() {}

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getRouteParameters().get("type").isEmpty()){
            UI.getCurrent().navigate(DashboardView.class);
        }

        String type = event.getRouteParameters().get("type").get();
        System.out.println(type);
    }
}
