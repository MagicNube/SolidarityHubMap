package org.pinguweb.frontend.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayout.Section;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.pingu.domain.DTO.CatastropheDTO;

@Route("bartest")
@CssImport("./styles/LateralBar.css")
public class LateralBarTest extends AppLayout {

    private boolean minimized = false;
    private SideNav sideNav;
    private VerticalLayout drawerContent;
    private Span appName;
    private Button logoButton;
    private Button minimizeButton;
    private Div selectedCatastropheInfo;
    private Select<String> languageSelect;
    private HorizontalLayout footerLayout;

    public LateralBarTest() {
        setPrimarySection(Section.DRAWER);
        getElement().setAttribute("class", "main-layout");
        addDrawerContent();
        UI.getCurrent().addAfterNavigationListener(e -> updateSelectedCatastropheInfo());
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

        selectedCatastropheInfo = new Div();
        selectedCatastropheInfo.addClassName("selected-catastrophe-info");
        updateSelectedCatastropheInfo();

        sideNav = createNavigation();
        Scroller scroller = new Scroller(sideNav);
        scroller.getElement().setAttribute("class", "nav-scroller");

        languageSelect = new Select<>();
        languageSelect.setItems("Castellano", "Valencià", "English");
        languageSelect.setValue("Castellano");
        languageSelect.getElement().setAttribute("class", "language-select");
        languageSelect.addValueChangeListener(e -> {
            VaadinSession.getCurrent().setAttribute("locale", e.getValue());
            UI.getCurrent().getPage().reload();
        });

        minimizeButton = new Button(VaadinIcon.ANGLE_DOUBLE_LEFT.create());
        minimizeButton.getElement().setAttribute("class", "minimize-button");
        minimizeButton.addClickListener(e -> toggleDrawerMinimized());

        Footer footer = new Footer();
        footer.setWidthFull();
        footer.getElement().setAttribute("class", "drawer-footer");

        footerLayout = new HorizontalLayout(languageSelect, minimizeButton);
        footerLayout.setSpacing(true);
        footerLayout.setWidthFull();
        footerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        footerLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        footer.add(footerLayout);

        drawerContent.add(header, selectedCatastropheInfo, scroller, footer);
        addToDrawer(drawerContent);
    }

    private void updateSelectedCatastropheInfo() {
        CatastropheDTO selectedCatastrophe = (CatastropheDTO) VaadinSession.getCurrent().getAttribute("selectedCatastrophe");
        selectedCatastropheInfo.removeAll();
        if (minimized) {
            selectedCatastropheInfo.setVisible(false);
            return;
        }
        if (selectedCatastrophe != null) {
            VerticalLayout infoLayout = new VerticalLayout();
            infoLayout.setSpacing(false);
            infoLayout.setPadding(false);
            H4 title = new H4("Catástrofe seleccionada:");
            title.addClassName("selected-catastrophe-title");
            Paragraph catastropheName = new Paragraph(selectedCatastrophe.getName());
            catastropheName.addClassName("selected-catastrophe-name");
            Button changeButton = new Button("Cambiar", VaadinIcon.EXCHANGE.create());
            changeButton.addClassName("change-catastrophe-button");
            changeButton.addClickListener(e -> UI.getCurrent().navigate("/catastrofes"));
            infoLayout.add(title, catastropheName, changeButton);
            selectedCatastropheInfo.add(infoLayout);
            selectedCatastropheInfo.setVisible(true);
        } else {
            Button selectButton = new Button("Seleccionar catástrofe", VaadinIcon.PLUS.create());
            selectButton.addClassName("select-catástrofe-button");
            selectButton.addClickListener(e -> UI.getCurrent().navigate("/catastrofes"));
            selectedCatastropheInfo.add(new H4("No hay catástrofe seleccionada"), selectButton);
            selectedCatastropheInfo.setVisible(true);
        }
    }

    private void toggleDrawerMinimized() {
        minimized = !minimized;
        if (minimized) {
            sideNav.getItems().forEach(item -> item.getElement().setAttribute("title", item.getLabel()));
            drawerContent.getElement().setAttribute("class", "drawer-content drawer-minimized");
            getElement().executeJs(
                    "document.documentElement.style.setProperty('--drawer-width', '55px');" +
                            "document.querySelector('vaadin-app-layout::part(content)').style.marginLeft = '55px';"
            );
            appName.setVisible(false);
            selectedCatastropheInfo.setVisible(false);
            languageSelect.setVisible(false);
            UI.getCurrent().access(() -> sideNav.getItems().forEach(item -> item.setLabel("")));
            minimizeButton.setIcon(VaadinIcon.ANGLE_DOUBLE_RIGHT.create());
            footerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        } else {
            drawerContent.getElement().setAttribute("class", "drawer-content");
            getElement().executeJs(
                    "document.documentElement.style.setProperty('--drawer-width', '260px');" +
                            "document.querySelector('vaadin-app-layout::part(content)').style.marginLeft = '260px';"
            );
            appName.setVisible(true);
            updateSelectedCatastropheInfo();
            selectedCatastropheInfo.setVisible(true);
            languageSelect.setVisible(true);
            updateNavigationTexts();
            minimizeButton.setIcon(VaadinIcon.ANGLE_DOUBLE_LEFT.create());
            minimizeButton.getElement().getStyle().set("margin-right", "20px");
            footerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        }
        UI.getCurrent().getPage().executeJs(
                "setTimeout(function() { window.dispatchEvent(new Event('resize')); }, 100);"
        );
    }

    private void updateNavigationTexts() {
        String[] labels = {"Inicio", "Tareas", "Mapa", "Dashboard", "Recursos",
                "Crear recurso", "Crear donación", "Crear almacén",
                "Contacto", "Sobre nosotros", "Log Out"
        };
        int i = 0;
        for (SideNavItem item : sideNav.getItems()) {
            item.setLabel(labels[i++]);
        }
    }

    public SideNav createNavigation() {
        SideNav nav = new SideNav();
        nav.getElement().setAttribute("class", "side-nav");
        nav.addItem(
                createNavItem("Inicio", VaadinIcon.HOME, "/inicio"),
                createNavItem("Tareas", VaadinIcon.TASKS, "/tasks"),
                createNavItem("Mapa", VaadinIcon.MAP_MARKER, "/map"),
                createNavItem("Dashboard", VaadinIcon.DASHBOARD, "/dashboard"),
                createNavItem("Recursos", VaadinIcon.TOOLBOX, "/resources"),
                createNavItem("Crear recurso", VaadinIcon.PLUS_CIRCLE, "/"),
                createNavItem("Crear donación", VaadinIcon.MONEY, "/"),
                createNavItem("Crear almacén", VaadinIcon.DATABASE, "/new-storage"),
                createNavItem("Contacto", VaadinIcon.PHONE, "/contact"),
                createNavItem("Sobre nosotros", VaadinIcon.INFO_CIRCLE, "/about-us"),
                createNavItem("Log Out", VaadinIcon.SIGN_OUT, "/logout")
        );
        return nav;
    }

    private SideNavItem createNavItem(String label, VaadinIcon icon, String url) {
        SideNavItem item = new SideNavItem(label, url);
        item.setPrefixComponent(icon.create());
        item.getElement().setAttribute("class", "nav-item");
        item.getElement().setAttribute("title", label);
        item.setLabel(label);
        return item;
    }
}
