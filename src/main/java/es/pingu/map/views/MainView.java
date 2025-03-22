package es.pingu.map.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import es.pingu.map.commons.NavigationBar;

@Route("")
public class MainView extends VerticalLayout {
    public MainView() {
        this.setSizeFull();
        this.add(NavigationBar.createNavBar());
    }
}
