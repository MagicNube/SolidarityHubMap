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
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

@Route("")
@PageTitle("")
@PermitAll
@CssImport("./styles/mainView.css")
public class MainView extends VerticalLayout implements LocaleChangeObserver {

    private final I18NProvider i18n;

    private H1 modalTitle;
    private Paragraph modalText;
    private Button closeModal;
    private H1 title;
    private Paragraph subtitle;
    private Button login;
    private Button home;
    private Button whatIsSHUB;
    private ComboBox<String> lang;

    @Autowired
    public MainView(I18NProvider i18n) {
        this.i18n = i18n;
        addClassName("main");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // 1. Creamos el Dialog aquí
        Dialog aboutDialog = new Dialog();
        aboutDialog.setCloseOnEsc(true);
        aboutDialog.setCloseOnOutsideClick(true);

        modalTitle = new H1(getTranslation("about.title"));
        modalText = new Paragraph(getTranslation("about.text"));
        closeModal = new Button(getTranslation("about.close"), e -> aboutDialog.close());

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

        title = new H1(getTranslation("main.title"));
        title.addClassName("content-title");

        subtitle = new Paragraph(getTranslation("main.subtitle"));
        subtitle.addClassName("content-subtitle");

        Icon arrow = VaadinIcon.ARROW_RIGHT.create();
        login = new Button(getTranslation("main.login"), arrow, e ->
                getUI().ifPresent(ui -> ui.navigate("login"))
        );
        login.setIconAfterText(true);
        login.addClassName("content-button");

        content.add(title, subtitle, login);

        addAndExpand(content);
        UI.getCurrent().getPage().setTitle(getTranslation("app.title"));
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
        home = new Button(getTranslation("header.home"), e -> UI.getCurrent().navigate(""));
        whatIsSHUB = new Button(getTranslation("header.about"), e -> aboutDialog.open());
        home.addClassName("nav-button");
        whatIsSHUB.addClassName("nav-button");

        lang = new ComboBox<>();
        lang.setItems("Castellano", "English", "Valencià");
        lang.setValue(getCurrentLanguage());
        lang.addClassName("nav-combo");
        lang.addValueChangeListener(e -> changeLanguage(e.getValue()));

        HorizontalLayout right = new HorizontalLayout(home, whatIsSHUB, lang);
        right.addClassName("right");
        right.setSpacing(true);
        right.setAlignItems(Alignment.CENTER);
        return right;
    }

    private String getTranslation(String key) {
        return i18n.getTranslation(key, UI.getCurrent().getLocale());
    }

    private void changeLanguage(String value) {
        Locale locale = switch (value) {
            case "English" -> new Locale("en");
            case "Valencià" -> new Locale("va");
            default -> new Locale("es");
        };
        UI.getCurrent().setLocale(locale);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        UI.getCurrent().getPage().setTitle(getTranslation("app.title"));
        modalTitle.setText(getTranslation("about.title"));
        modalText.setText(getTranslation("about.text"));
        closeModal.setText(getTranslation("about.close"));
        home.setText(getTranslation("header.home"));
        whatIsSHUB.setText(getTranslation("header.about"));
        title.setText(getTranslation("main.title"));
        subtitle.setText(getTranslation("main.subtitle"));
        login.setText(getTranslation("main.login"));
        lang.setValue(getCurrentLanguage());
    }

    private String getCurrentLanguage() {
        Locale l = UI.getCurrent().getLocale();
        if (l.getLanguage().equals("en")) return "English";
        if (l.getLanguage().equals("va")) return "Valencià";
        return "Castellano";
    }
}
