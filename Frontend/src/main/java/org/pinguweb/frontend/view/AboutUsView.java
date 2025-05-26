package org.pinguweb.frontend.view;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "about-us", layout = MainLayout.class)
public class AboutUsView extends VerticalLayout {

    public AboutUsView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        getStyle()
                .set("background-image", "url('../wallpapers/mainViewFondo.jpg')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("background-repeat", "no-repeat")
                .set("min-height", "100vh");

        H2 title = new H2("Integrantes del Proyecto Solidarity Hub");
        title.getStyle().set("margin-top", "30px");
        add(title);

        // Card de columna 1: Pingu Mobile (con enlace)
        Div mobileCard = new Div();
        mobileCard.getStyle()
                .set("background", "white")
                .set("box-shadow", "0 2px 12px rgba(0,0,0,0.09)")
                .set("border-radius", "20px")
                .set("padding", "28px 24px")
                .set("min-width", "230px")
                .set("max-width", "270px")
                .set("text-align", "center");

        Anchor mobileTitle = new Anchor("https://tu-url.com", "Pingu Mobile");
        mobileTitle.setTarget("_blank");
        mobileTitle.getStyle().set("font-weight", "bold").set("font-size", "1.1rem").set("color", "#2563eb");
        mobileCard.add(mobileTitle);
        mobileCard.add(new Paragraph("Javier Moreno Aguilar"));
        mobileCard.add(new Paragraph("Arnau Pelechano García"));
        mobileCard.add(new Paragraph("África Muñoz Fernández"));
        mobileCard.add(new Paragraph("Joan Pastor Ferrer"));

        // Card de columna 2: Pingu Web Tareas
        Div webTareasCard = new Div();
        webTareasCard.getStyle()
                .set("background", "white")
                .set("box-shadow", "0 2px 12px rgba(0,0,0,0.09)")
                .set("border-radius", "20px")
                .set("padding", "28px 24px")
                .set("min-width", "230px")
                .set("max-width", "270px")
                .set("text-align", "center");

        Anchor tareasTitle = new Anchor("https://github.com/Juguitoo/SolidarityHubTareas", "Pingu Web Tareas");
        tareasTitle.getStyle().set("font-size", "1.1rem");
        webTareasCard.add(tareasTitle);
        webTareasCard.add(new Paragraph("Hugo Juan Gómez"));
        webTareasCard.add(new Paragraph("Roberto Hortelano Toledo"));
        webTareasCard.add(new Paragraph("Javier García Fortis"));
        webTareasCard.add(new Paragraph("Adrián Labrador García"));

        // Card de columna 3: Pingu Web Visualización
        Div webVisualCard = new Div();
        webVisualCard.getStyle()
                .set("background", "white")
                .set("box-shadow", "0 2px 12px rgba(0,0,0,0.09)")
                .set("border-radius", "20px")
                .set("padding", "28px 24px")
                .set("min-width", "230px")
                .set("max-width", "270px")
                .set("text-align", "center");

        Anchor visualTitle = new Anchor("https://github.com/Luubstar/SolidarityHubMap", "Pingu Web Visualización");
        visualTitle.getStyle().set("font-size", "1.1rem");
        webVisualCard.add(visualTitle);
        webVisualCard.add(new Paragraph("Jesús Caravaca González"));
        webVisualCard.add(new Paragraph("Nicolás Barona Riera"));
        webVisualCard.add(new Paragraph("Saúl Alcázar López"));
        webVisualCard.add(new Paragraph("Francisco Blanco Miguel"));

        HorizontalLayout columns = new HorizontalLayout(mobileCard, webTareasCard, webVisualCard);
        columns.setWidthFull();
        columns.setJustifyContentMode(JustifyContentMode.CENTER);
        columns.setSpacing(true);

        add(columns);
    }
}
