package org.pinguweb.frontend.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.pinguweb.frontend.view.NavigationBar;

import org.pinguweb.frontend.services.DashboardService;

@Route("dashboard")
public class DashboardView extends VerticalLayout {
    DashboardService controller;

    public DashboardView() {
        this.setSizeFull();
        this.add(NavigationBar.createNavBar());

        VerticalLayout dashboardLayout = new VerticalLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout();

        this.add(dashboardLayout);
        dashboardLayout.setSizeFull();
        buttonLayout.setSizeFull();
        buttonLayout.add(dashboardLayout);
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
