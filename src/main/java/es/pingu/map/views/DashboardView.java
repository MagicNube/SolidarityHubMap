package es.pingu.map.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import es.pingu.map.commons.NavigationBar;

@Route("dashboard")
public class DashboardView extends VerticalLayout {

    public DashboardView() {
        this.setSizeFull();
        this.add(NavigationBar.createNavBar());
    }
}
