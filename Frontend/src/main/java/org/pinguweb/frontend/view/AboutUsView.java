package org.pinguweb.frontend.view;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
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

        // Título en blanco y separado
        H2 title = new H2("Integrantes del Proyecto Solidarity Hub");
        title.getStyle()
                .set("margin-top", "50px")
                .set("color", "white")
                .set("margin-bottom", "50px")
                .set("font-size", "2.4rem")
                .set("text-shadow", "0 2px 8px rgba(0,0,0,0.2)");
        add(title);

        // Card columna 1: Pingu Mobile
        Div mobileCard = new Div();
        mobileCard.getStyle()
                .set("background", "white")
                .set("box-shadow", "0 2px 16px rgba(0,0,0,0.13)")
                .set("border-radius", "25px")
                .set("padding", "36px 36px 28px 36px")
                .set("min-width", "320px")
                .set("max-width", "360px")
                .set("text-align", "center")
                .set("font-size", "1.12rem");

        Anchor mobileTitle = new Anchor("https://youtu.be/HRTWYxbXDXU?feature=shared&t=7", "Pingu Mobile");
        mobileTitle.setTarget("_blank");
        mobileTitle.getStyle()
                .set("font-weight", "bold")
                .set("font-size", "1.16rem")
                .set("color", "#2563eb")
                .set("display", "block")
                .set("margin-bottom", "18px");
        mobileCard.add(mobileTitle);
        mobileCard.add(new Paragraph("Javier Moreno Aguilar"));
        mobileCard.add(new Paragraph("Arnau Pelechano García"));
        mobileCard.add(new Paragraph("África Muñoz Fernández"));
        mobileCard.add(new Paragraph("Joan Pastor Ferrer"));

        // Card columna 2: Pingu Web Tareas
        Div webTareasCard = new Div();
        webTareasCard.getStyle()
                .set("background", "white")
                .set("box-shadow", "0 2px 16px rgba(0,0,0,0.13)")
                .set("border-radius", "25px")
                .set("padding", "36px 36px 28px 36px")
                .set("min-width", "320px")
                .set("max-width", "360px")
                .set("text-align", "center")
                .set("font-size", "1.12rem");

        Anchor tareasTitle = new Anchor("https://github.com/Juguitoo/SolidarityHubTareas", "Pingu Web Tareas");
        tareasTitle.setTarget("_blank");
        tareasTitle.getStyle()
                .set("font-weight", "bold")
                .set("font-size", "1.16rem")
                .set("color", "#2563eb")
                .set("display", "block")
                .set("margin-bottom", "18px");
        webTareasCard.add(tareasTitle);
        webTareasCard.add(new Paragraph("Hugo Juan Gómez"));
        webTareasCard.add(new Paragraph("Roberto Hortelano Toledo"));
        webTareasCard.add(new Paragraph("Javier García Fortis"));
        webTareasCard.add(new Paragraph("Adrián Labrador García"));

        // Card columna 3: Pingu Web Visualización
        Div webVisualCard = new Div();
        webVisualCard.getStyle()
                .set("background", "white")
                .set("box-shadow", "0 2px 16px rgba(0,0,0,0.13)")
                .set("border-radius", "25px")
                .set("padding", "36px 36px 28px 36px")
                .set("min-width", "320px")
                .set("max-width", "360px")
                .set("text-align", "center")
                .set("font-size", "1.12rem");

        Anchor visualTitle = new Anchor("https://github.com/Luubstar/SolidarityHubMap", "Pingu Web Visualización");
        visualTitle.setTarget("_blank");
        visualTitle.getStyle()
                .set("font-weight", "bold")
                .set("font-size", "1.16rem")
                .set("color", "#2563eb")
                .set("display", "block")
                .set("margin-bottom", "18px");
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
