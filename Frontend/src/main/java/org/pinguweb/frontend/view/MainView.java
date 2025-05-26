package org.pinguweb.frontend.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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

        // 1. Creamos el Dialog aquí
        Dialog aboutDialog = new Dialog();
        aboutDialog.setCloseOnEsc(true);
        aboutDialog.setCloseOnOutsideClick(true);

        H1 modalTitle = new H1("¿Qué es Solidarity Hub?");
        Paragraph modalText = new Paragraph("Aquí va tu texto explicando Solidarity Hub...");
        Button closeModal = new Button("Cerrar", e -> aboutDialog.close());

        VerticalLayout dialogLayout = new VerticalLayout(modalTitle, modalText, closeModal);
        dialogLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        aboutDialog.add(dialogLayout);

        add(createHeader(aboutDialog));

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

        Icon arrow = VaadinIcon.ARROW_RIGHT.create();
        Button login = new Button("Iniciar Sesión", arrow, e ->
                getUI().ifPresent(ui -> ui.navigate("login"))
        );
        login.setIconAfterText(true);
        login.addClassName("content-button");

        content.add(title, subtitle, login);

        addAndExpand(content);
    }

    // 2. Ahora el header recibe el Dialog para pasárselo al botón
    private HorizontalLayout createHeader(Dialog aboutDialog) {
        Image logo = new Image("icons/LogoBlancoSinFondo.png", "Logo");
        logo.setHeight("60px");

        H1 appName = new H1("Solidarity Hub");
        appName.getStyle()
                .set("color", "white")
                .set("font-size", "1.5rem")
                .set("margin", "0 0 0 0.5rem");

        HorizontalLayout left = new HorizontalLayout(logo, appName);
        left.setAlignItems(Alignment.CENTER);

        logo.getElement().setAttribute("title", "Logo de Pingu Solidarity Hub");

        HorizontalLayout right = getHorizontalLayout(aboutDialog);

        HorizontalLayout header = new HorizontalLayout(left, right);
        header.addClassName("app-header");
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);

        return header;
    }

    // 3. Ahora el layout de botones recibe el Dialog para abrirlo desde el botón
    private HorizontalLayout getHorizontalLayout(Dialog aboutDialog) {
        Button home = new Button("Home", e -> UI.getCurrent().navigate(""));
        Button whatIsSHUB = new Button("Qué es SHUB", e -> aboutDialog.open());
        home.addClassName("nav-button");
        whatIsSHUB.addClassName("nav-button");

        ComboBox<String> lang = new ComboBox<>();
        lang.setItems("Castellano", "English", "Valencià");
        lang.setValue("Castellano");
        lang.addClassName("nav-combo");

        HorizontalLayout right = new HorizontalLayout(home, whatIsSHUB, lang);
        right.addClassName("right");
        right.setSpacing(true);
        right.setAlignItems(Alignment.CENTER);
        return right;
    }
}
