package org.pinguweb.frontend.view;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("information")
@PageTitle("Información")
public class InformationView extends VerticalLayout {

    public InformationView() {
        setSizeFull();

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        mainLayout.setPadding(true);
        mainLayout.setSpacing(true);

        // Contenedor para el contenido textual
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setPadding(false);
        content.setSpacing(true);
        mainLayout.expand(content); // Para que el contenido ocupe el espacio restante

        add(mainLayout);

    }
}