package org.pinguweb.frontend.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.pingu.domain.DTO.CatastropheDTO;

@Route("bartest")
@CssImport("./styles/LateralBar.css")
public class LateralBarTest extends AppLayout {

    private boolean minimized = false;
    private H1 viewTitle;
    private SideNav sideNav;
    private VerticalLayout drawerContent;
    private Span appName;
    private Button logoButton;
    private Div selectedCatastropheInfo;

    public LateralBarTest() {
        setPrimarySection(Section.DRAWER);
        getElement().setAttribute("class", "main-layout");
        addDrawerContent();

        // Configurar un listener para actualizar el indicador de catástrofe seleccionada
        // cuando cambia de ruta o se refresca la página
        UI.getCurrent().addAfterNavigationListener(event -> updateSelectedCatastropheInfo());
    }

    private void addDrawerContent() {
        drawerContent = new VerticalLayout();
        drawerContent.setPadding(false);
        drawerContent.setSpacing(false);
        drawerContent.setSizeFull();
        drawerContent.getElement().setAttribute("class", "drawer-content");

        Header header = new Header();
        header.getElement().setAttribute("class", "drawer-header");

        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.setSpacing(true);
        logoLayout.getElement().setAttribute("class", "logo-layout");

        Image logoImage = new Image("icons/LogoNegroSinFondo.png", "Logo");
        logoImage.getElement().setAttribute("class", "logo-principal");
        logoButton = new Button(logoImage);
        logoButton.getElement().setAttribute("class", "logo-button");
        logoButton.addClickListener(e -> toggleDrawerMinimized());

        appName = new Span("SolidarityHub");
        appName.getElement().setAttribute("class", "app-name");

        logoLayout.add(logoButton, appName);
        header.add(logoLayout);

        // Div para mostrar la catástrofe seleccionada
        selectedCatastropheInfo = new Div();
        selectedCatastropheInfo.addClassName("selected-catastrophe-info");
        updateSelectedCatastropheInfo(); // Inicializar con la información actual

        sideNav = createNavigation();
        Scroller scroller = new Scroller(sideNav);
        scroller.getElement().setAttribute("class", "nav-scroller");

        Footer footer = new Footer();
        footer.getElement().setAttribute("class", "drawer-footer");
        Span version = new Span("v1.0");
        version.getElement().setAttribute("class", "version-info");
        footer.add(version);

        drawerContent.add(header, selectedCatastropheInfo, scroller, footer);
        addToDrawer(drawerContent);
    }

    private void updateSelectedCatastropheInfo() {
        CatastropheDTO selectedCatastrophe =
                (CatastropheDTO) VaadinSession.getCurrent().getAttribute("selectedCatastrophe");

        selectedCatastropheInfo.removeAll();

        if (minimized) {
            selectedCatastropheInfo.setVisible(false);
            return;
        }

        if (selectedCatastrophe != null) {
            VerticalLayout infoLayout = new VerticalLayout();
            infoLayout.setSpacing(false);
            infoLayout.setPadding(false);

            // Título "Catástrofe seleccionada:"
            H4 title = new H4("Catástrofe seleccionada:");
            title.addClassName("selected-catastrophe-title");

            // Nombre de la catástrofe
            Paragraph catastropheName = new Paragraph(selectedCatastrophe.getName());
            catastropheName.addClassName("selected-catastrophe-name");

            // Botón para cambiar de catástrofe
            Button changeButton = new Button("Cambiar", VaadinIcon.EXCHANGE.create());
            changeButton.addClassName("change-catastrophe-button");
            changeButton.addClickListener(e -> UI.getCurrent().navigate("http://localhost:8083/catastrofes"));

            infoLayout.add(title, catastropheName, changeButton);
            selectedCatastropheInfo.add(infoLayout);
            selectedCatastropheInfo.setVisible(true);
        } else {
            // Si no hay catástrofe seleccionada, mostrar un mensaje o redirigir
            Button selectButton = new Button("Seleccionar catástrofe", VaadinIcon.PLUS.create());
            selectButton.addClassName("select-catastrophe-button");
            selectButton.addClickListener(e -> UI.getCurrent().navigate("http://localhost:8083/catastrofes"));

            selectedCatastropheInfo.add(new H4("No hay catástrofe seleccionada"), selectButton);
            selectedCatastropheInfo.setVisible(true);
        }
    }

    private void toggleDrawerMinimized() {
        minimized = !minimized;

        if (minimized) {
            // Primero guardar los tooltips
            sideNav.getItems().forEach(item -> {
                item.getElement().setAttribute("title", item.getLabel());
            });

            // Luego colapsar el drawer
            drawerContent.getElement().setAttribute("class", "drawer-content drawer-minimized");
            getElement().executeJs(
                    "document.documentElement.style.setProperty('--drawer-width', '55px');" +
                            "document.querySelector('vaadin-app-layout::part(content)').style.marginLeft = '55px';"
            );

            // Ocultar nombre de la app
            appName.setVisible(false);

            // Ocultar info de catástrofe seleccionada
            selectedCatastropheInfo.setVisible(false);

            // Vaciar las etiquetas para evitar que ocupen espacio
            UI.getCurrent().access(() -> {
                sideNav.getItems().forEach(item -> item.setLabel(""));
            });

        } else {
            // Expandir el drawer
            drawerContent.getElement().setAttribute("class", "drawer-content");
            getElement().executeJs(
                    "document.documentElement.style.setProperty('--drawer-width', '260px');" +
                            "document.querySelector('vaadin-app-layout::part(content)').style.marginLeft = '260px';"
            );

            // Mostrar nombre de la app
            appName.setVisible(true);
            updateSelectedCatastropheInfo();
            // Mostrar info de catástrofe seleccionada
            selectedCatastropheInfo.setVisible(true);

            // Restaurar las etiquetas
            updateNavigationTexts();
        }

        // Notificar al navegador para recalcular tamaños
        UI.getCurrent().getPage().executeJs(
                "setTimeout(function() { window.dispatchEvent(new Event('resize')); }, 100);"
        );
    }

    //TODO Acabar
    private void updateNavigationTexts() {
        String[] labels = {"Tareas", "Mapa", "Dashboard", "Recursos", "Crear recurso", "Crear donación", "Crear almacén", "Contacto", "Sobre nosotros", "Cambiar idioma", "Log Out"};
        int index = 0;
        for (SideNavItem item : sideNav.getItems()) {
            item.setLabel(labels[index++]);
        }
    }
    public SideNav createNavigation() {
        SideNav nav = new SideNav();
        nav.getElement().setAttribute("class", "side-nav");

        nav.addItem(
                createNavItem("Tareas",         VaadinIcon.TASKS,        "http://localhost:8083/tasks"),
                createNavItem("Mapa",           VaadinIcon.MAP_MARKER,    "http://localhost:8080/map"),
                createNavItem("Dashboard",      VaadinIcon.DASHBOARD,     "http://localhost:8080/dashboard"),
                createNavItem("Recursos",       VaadinIcon.TOOLBOX,       "http://localhost:8083/resources"),

                createNavItem("Crear recurso",  VaadinIcon.PLUS_CIRCLE,   "http://localhost:8083/"),
                createNavItem("Crear donación", VaadinIcon.MONEY,         "http://localhost:8083/"),
                createNavItem("Crear almacén",  VaadinIcon.DATABASE,      "http://localhost:8080/new-storage"),

                createNavItem("Contacto",       VaadinIcon.PHONE,         "http://localhost:8080/contact"),
                createNavItem("Sobre nosotros", VaadinIcon.INFO_CIRCLE,   "http://localhost:8080/about-us"),
                createNavItem("Cambiar idioma", VaadinIcon.GLOBE,         "http://localhost:8080/lang"),
                createNavItem("Log Out",        VaadinIcon.SIGN_OUT,      "http://localhost:8080/logout")
        );


        return nav;
    }

    private SideNavItem createNavItem(String label, VaadinIcon icon, Class<? extends Component> view) {
        SideNavItem item = new SideNavItem(label, view);
        item.setPrefixComponent(new Icon(icon));
        item.getElement().setAttribute("class", "nav-item");

        // Añadir tooltip usando el atributo title
        item.getElement().setAttribute("title", label);

        // Configurar el texto del elemento
        item.setLabel(label);
        return item;
    }

    private SideNavItem createNavItem(String label, VaadinIcon icon, String url) {
        SideNavItem item = new SideNavItem(label, url);
        item.setPrefixComponent(new Icon(icon));
        item.getElement().setAttribute("class", "nav-item");
        // Añadir tooltip usando el atributo title
        item.getElement().setAttribute("title", label);

        // Configurar el texto del elemento
        item.setLabel(label);
        return item;}
}

