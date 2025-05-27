package org.pinguweb.frontend.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.pingu.web.BackendObservableService.observableList.Observer;
import org.pingu.web.BackendObservableService.observableList.ObserverChange;
import org.pinguweb.frontend.interfaceBuilders.Directors.DashboardBuilderDirector;
import org.pinguweb.frontend.services.BackendDTOService;

@Slf4j
@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout implements Observer {

    DashboardBuilderDirector director = new DashboardBuilderDirector();
    UI ui;
    HorizontalLayout dashboard;

    public DashboardView(){
        BackendDTOService.GetInstancia().getNeedList().attach(this, ObserverChange.ADD_ALL);
        BackendDTOService.GetInstancia().getTaskList().attach(this, ObserverChange.ADD_ALL);
        BackendDTOService.GetInstancia().getVolunteerList().attach(this, ObserverChange.ADD_ALL);
        BackendDTOService.GetInstancia().getAffectedList().attach(this, ObserverChange.ADD_ALL);
        BackendDTOService.GetInstancia().getResourceList().attach(this, ObserverChange.ADD_ALL);
        //BackendDTOService.GetInstancia().getDonationList().attach(this, ObserverChange.ADD_ALL);
        director.buildComplete();
        dashboard = director.get().getInterface();
        this.add(dashboard);

        this.ui = UI.getCurrent();
        if (ui == null) {
            log.warn("UI is null, cannot update UI components.");
        }
    }

    @Override
    public void update(ObserverChange change) {
        log.info("Actualizando...");

        ui.access(() -> {
            this.remove(dashboard);
            director.buildComplete();
            dashboard = director.get().getInterface();
            this.add(dashboard);
        });

        log.info("Se ha actualizado la dashboard");
    }
}
