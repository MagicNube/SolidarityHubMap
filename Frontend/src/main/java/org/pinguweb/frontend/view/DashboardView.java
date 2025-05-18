package org.pinguweb.frontend.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.pingu.web.BackendObservableService.observableList.Observer;
import org.pingu.web.BackendObservableService.observableList.ObserverChange;
import org.pinguweb.frontend.interfaceBuilders.Directors.DashboardBuilderDirector;
import org.pinguweb.frontend.services.BackendDTOService;

import java.util.Arrays;

@Slf4j
@Route("dashboard")
public class DashboardView extends VerticalLayout implements Observer {

    public DashboardView(){
        BackendDTOService.GetInstancia().getNeedList().attach(this, ObserverChange.ADD_ALL);
        DashboardBuilderDirector director = new DashboardBuilderDirector();
        director.buildComplete();
        this.add(director.get().getInterface());
    }

    @Override
    public void update(ObserverChange change) {
        log.info("Datos actualizados! {}", change);
        log.info(Arrays.toString(BackendDTOService.GetInstancia().getNeedList().getValues().toArray()));
    }
}
