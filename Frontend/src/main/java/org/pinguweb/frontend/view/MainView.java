package org.pinguweb.frontend.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("")
@PageTitle("Bienvenido")
@PermitAll
@CssImport("./styles/mainView.css")
public class MainView extends VerticalLayout {

    public MainView() {
        addClassName("main");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        add(createHeader());

        VerticalLayout content = new VerticalLayout();
        content.addClassName("content-wrapper");
        content.setPadding(false);
        content.setSpacing(false);
        content.setWidthFull();
        content.setHeightFull();

        H1 title = new H1("Bienvenido al Proyecto Solidarity Hub");
        title.addClassName("content-title");

        Paragraph subtitle = new Paragraph(
                "Inicia sesión para acceder a las funcionalidades."
        );
        subtitle.addClassName("content-subtitle");

        Button login = new Button("Iniciar Sesión", e -> UI.getCurrent().navigate("login"));
        login.addClassName("content-button");

        content.add(title, subtitle, login);

        addAndExpand(content);
    }

    private HorizontalLayout createHeader() {
        Image logo = new Image("icons/PinguLogo.png", "Logo");
        logo.setHeight("60px");

        H1 appName = new H1("Solidarity Hub");
        appName.getStyle()
                .set("color", "white")
                .set("font-size", "1.5rem")
                .set("margin", "0 0 0 0.5rem");

        HorizontalLayout left = new HorizontalLayout(logo, appName);
        left.setAlignItems(Alignment.CENTER);

        logo.getElement().setAttribute("title", "Logo de Pingu Solidarity Hub");

        Button home = new Button("Home", e -> UI.getCurrent().navigate(""));
        Button about = new Button("About Us", e -> UI.getCurrent().navigate("about"));
        home.addClassName("nav-button");
        about.addClassName("nav-button");

        ComboBox<String> lang = new ComboBox<>();
        lang.setItems("Castellano", "English", "Valencià");
        lang.setValue("Castellano");
        lang.addClassName("nav-combo");

        HorizontalLayout right = new HorizontalLayout(home, about, lang);
        right.addClassName("right");
        right.setSpacing(true);
        right.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout header = new HorizontalLayout(left, right);
        header.addClassName("app-header");
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);

        return header;
    }
}
