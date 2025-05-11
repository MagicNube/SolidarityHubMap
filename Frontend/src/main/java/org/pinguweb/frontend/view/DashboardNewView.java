package org.pinguweb.frontend.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.pinguweb.frontend.interfaceBuilders.Directors.DashboardBuilderDirector;
import org.pinguweb.frontend.singleton.BackendDTOObservableService;
import org.pinguweb.frontend.singleton.observableList.Observer;
import org.pinguweb.frontend.singleton.observableList.ObserverChange;

import java.util.Arrays;

@Slf4j
@Route("dashboard")
public class DashboardNewView extends VerticalLayout implements Observer {

    public DashboardNewView(){
        BackendDTOObservableService.GetInstancia().getNeedServerList().getValues().attach(this);
        DashboardBuilderDirector director = new DashboardBuilderDirector();
        director.buildComplete();
        this.add(director.get().getInterface());
    }

    @Override
    public void update(ObserverChange change) {
        if (change == ObserverChange.MANUAL || change == ObserverChange.ADD_ALL) {
            log.info("Datos actualizados! {}", change);
            log.info(Arrays.toString(BackendDTOObservableService.GetInstancia().getNeedServerList().getValues().toArray()));
        }
    }
}
