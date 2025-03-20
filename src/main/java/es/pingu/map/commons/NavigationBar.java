package es.pingu.map.commons;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import es.pingu.map.views.ContactView;
import es.pingu.map.views.DashboardView;
import es.pingu.map.views.MainView;
import es.pingu.map.views.MapView;

public class NavigationBar {
    public static SideNav createNavBar() {
        SideNav nav = new SideNav();

        SideNavItem homeLink = new SideNavItem("Inicio",
                MainView.class, VaadinIcon.HOME.create());
        SideNavItem mapLink = new SideNavItem("Mapa",
                MapView.class, VaadinIcon.MAP_MARKER.create());
        SideNavItem taskLink = new SideNavItem("Tareas", "tareas",
                VaadinIcon.BELL.create());
        SideNavItem dashboardLink = new SideNavItem("Dashboard",
                DashboardView.class, VaadinIcon.DASHBOARD.create());
        SideNavItem resourcesLink = new SideNavItem("Recursos",
                "recursos", VaadinIcon.TOOLBOX.create());
        SideNavItem contactLink = new SideNavItem("Contacto",
                ContactView.class, VaadinIcon.PHONE.create());

        nav.addItem(homeLink, mapLink, taskLink, dashboardLink, resourcesLink, contactLink);
        return nav;
    }
}
