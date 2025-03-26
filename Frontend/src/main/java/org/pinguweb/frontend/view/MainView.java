package org.pinguweb.frontend.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {
    public MainView() {
        this.setSizeFull();
        this.add(NavigationBar.createNavBar());
    }
}