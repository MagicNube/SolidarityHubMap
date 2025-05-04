package org.pinguweb.frontend.view;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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
    public class MainView extends VerticalLayout {

        public MainView() {
            configureView();
        }

        private void configureView() {
            setSizeFull();
            setSpacing(false);
            setPadding(false);

            // Aquí se añade la imagen de fondo
            getStyle().set("background-image", "url('wallpapers/LoginFondo.jpg')")
                    .set("background-size", "cover") // Asegura que la imagen cubra todo el fondo
                    .set("background-position", "center") // Centra la imagen
                    .set("background-attachment", "fixed"); // Hace que la imagen no se mueva al hacer scroll

            HorizontalLayout header = createHeader();

            VerticalLayout content = new VerticalLayout();
            content.setAlignItems(Alignment.CENTER);
            content.setJustifyContentMode(JustifyContentMode.CENTER);
            content.setSizeFull();
            content.setSpacing(true);

            H1 title = new H1("Bienvenido al Proyecto Solidarity Hub");
            title.getStyle().set("color", "#1f2937").set("font-size", "3em"); // Aumenta el tamaño de la fuente

            Paragraph welcomeMessage = new Paragraph("Por favor, inicia sesión para acceder a las funcionalidades.");

            Button loginButton = new Button("Iniciar Sesión", event -> {
                UI.getCurrent().navigate("map"); // O cambia a la vista de login real
            });
            loginButton.getStyle()
                    .set("background-color", "#2563eb")
                    .set("color", "white")
                    .set("padding", "12px 24px")
                    .set("border-radius", "8px")
                    .set("font-weight", "bold");

            content.add(title, welcomeMessage, loginButton);

            add(header, content);
        }

        private HorizontalLayout createHeader() {
            Image logo = new Image("icons/Pingu.png", "Solidarity Hub Logo");
            logo.setHeight("80px");

            Button homeButton = new Button("Home", e -> UI.getCurrent().navigate(""));
            Button aboutButton = new Button("About Us", e -> UI.getCurrent().navigate("about"));

            homeButton.getStyle().set("background", "none").set("color", "#1f2937").set("font-weight", "bold");
            aboutButton.getStyle().set("background", "none").set("color", "#1f2937").set("font-weight", "bold");

            ComboBox<String> languageSelector = new ComboBox<>();
            languageSelector.setItems("Español", "English");
            languageSelector.setValue("Español");
            languageSelector.addValueChangeListener(event -> {
                // TODO: implementar cambio de idioma real
            });

            HorizontalLayout rightSide = new HorizontalLayout(homeButton, aboutButton, languageSelector);
            rightSide.setSpacing(true);
            rightSide.setAlignItems(Alignment.CENTER);

            HorizontalLayout header = new HorizontalLayout(logo, rightSide);
            header.setWidthFull();
            header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
            header.setAlignItems(Alignment.CENTER);
            header.setPadding(true);
            header.getStyle().set("background-color", "#e5e7eb"); // Gris claro

            return header;
        }
    }
