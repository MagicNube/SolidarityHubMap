package org.pinguweb.frontend.view;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

public class NavigationBar {
    public static SideNav createNavBar() {
        SideNav nav = new SideNav();

        SideNavItem homeLink = new SideNavItem("Catástrofes",
                "http://localhost:8083/catastrophe", VaadinIcon.CLOUD.create());
        SideNavItem mapLink = new SideNavItem("Mapa",
                MapView.class, VaadinIcon.MAP_MARKER.create());
        SideNavItem taskLink = new SideNavItem("Tareas", "http://localhost:8083/",
                VaadinIcon.TASKS.create());
        SideNavItem dashboardLink = new SideNavItem("Dashboard",
                DashboardView.class, VaadinIcon.DASHBOARD.create());
        SideNavItem resourcesLink = new SideNavItem("Recursos",
                "http://localhost:8083/recursos", VaadinIcon.TOOLBOX.create());
        SideNavItem contactLink = new SideNavItem("Contacto",
                ContactView.class, VaadinIcon.PHONE.create());

        nav.addItem(homeLink,taskLink,mapLink,resourcesLink,dashboardLink, contactLink);
        return nav;
    }
}