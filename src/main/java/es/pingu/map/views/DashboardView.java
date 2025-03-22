package es.pingu.map.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import es.pingu.map.commons.NavigationBar;
import es.pingu.map.controllers.DashboardController;

@Route("dashboard")
public class DashboardView extends VerticalLayout {
    DashboardController controller;

    public DashboardView() {
        this.setSizeFull();
        this.add(NavigationBar.createNavBar());

        VerticalLayout dashboardLayout = new VerticalLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout();

        this.add(dashboardLayout);
        dashboardLayout.setSizeFull();
        buttonLayout.setSizeFull();
        buttonLayout.add(NavigationBar.createNavBar(), dashboardLayout);
        buttonLayout.setFlexGrow(1, buttonLayout);
        this.add(buttonLayout);


        Button userButton = new Button("User");
        Button reportButton = new Button("Report");
        Button settingsButton = new Button("Settings");

        buttonLayout.add(userButton, reportButton, settingsButton);

        userButton.addClickListener(e -> controller.showUsers(this));
        reportButton.addClickListener(e-> controller.showReports(this));
        settingsButton.addClickListener(e-> controller.showSettings(this));



        
    }
}
