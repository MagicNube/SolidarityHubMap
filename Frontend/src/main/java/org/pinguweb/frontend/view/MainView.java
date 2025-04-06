package org.pinguweb.frontend.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("Bienvenido")
public class MainView extends VerticalLayout {

    public MainView() {
        configureView();
}

    private void configureView() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        H1 title = new H1("Bienvenido al Proyecto Solidarity Hub");
        title.getStyle().set("color", "#333333").set("font-size", "2em");

        Paragraph welcomeMessage = new Paragraph("Por favor, inicia sesión para acceder a las funcionalidades.");

        Button loginButton = new Button("Iniciar Sesión", event -> {
            UI.getCurrent().navigate("login");
        });

        loginButton.getStyle()
                .set("background-color", "#007BFF")
                .set("color", "#FFFFFF")
                .set("padding", "10px 20px")
                .set("border-radius", "5px");

        add(title, welcomeMessage, loginButton);
    }
}

