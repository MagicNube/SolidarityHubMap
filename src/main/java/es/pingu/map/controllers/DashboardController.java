package es.pingu.map.controllers;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DashboardController {

    public void showUsers(VerticalLayout layout){
        layout.add(new Span("Usuarios"));
        // TODO: Como hago para mostrar los usuarios?
    }

    public void showReports(VerticalLayout layout){
        layout.add(new Span("Reportes"));
        // TODO: Como hago para mostrar los reportes?
    }

    public void showSettings(VerticalLayout layout){
        layout.add(new Span("Configuración"));
        // TODO: Como hago para mostrar la configuración?
    }
}
