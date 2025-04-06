package org.pinguweb.frontend.view;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

public class NavigationBar {
    public static SideNav createNavBar() {
        SideNav nav = new SideNav();

        SideNavItem homeLink = new SideNavItem("Inicio",
                MainView.class, VaadinIcon.HOME.create());
        SideNavItem mapLink = new SideNavItem("Mapa",
                MapView.class, VaadinIcon.MAP_MARKER.create());
        SideNavItem taskLink = new SideNavItem("Tareas", "http://localhost:8082/tareas",
                VaadinIcon.BELL.create());
        SideNavItem dashboardLink = new SideNavItem("Dashboard",
                DashboardView.class, VaadinIcon.DASHBOARD.create());
        SideNavItem resourcesLink = new SideNavItem("Recursos",
                "http://localhost:8082/recursos", VaadinIcon.TOOLBOX.create());
        SideNavItem contactLink = new SideNavItem("Contacto",
                ContactView.class, VaadinIcon.PHONE.create());

        nav.addItem(homeLink, mapLink, taskLink, dashboardLink, resourcesLink, contactLink);
        return nav;
    }
}