package org.pinguweb.frontend.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "contact", layout = MainLayout.class)
public class ContactView extends VerticalLayout implements LocaleChangeObserver {

    private final I18NProvider i18n;
    private Image illus;
    private Image icon;
    private H2 title;
    private Text subtitle;
    private Div subtitleDiv;
    private Div infoText;
    private Button copyBtn;

    @Autowired
    public ContactView(I18NProvider i18n) {
        this.i18n = i18n;
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        getStyle()
                .set("background-image", "url('../wallpapers/mainViewFondo.jpg')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("background-repeat", "no-repeat")
                .set("min-height", "100vh");

        Div card = new Div();
        card.getStyle()
                .set("box-shadow", "0 2px 12px rgba(0,0,0,0.09)")
                .set("border-radius", "18px")
                .set("padding", "40px 38px 38px 38px")
                .set("background", "white")
                .set("max-width", "400px")
                .set("width", "100%")
                .set("margin-top", "64px");

        illus = new Image("icons/Support.png", getTranslation("contact.title"));
        illus.setWidth("110px");
        illus.setHeight("110px");
        illus.getStyle().set("margin-bottom", "15px").set("border-radius", "12px");

        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(FlexComponent.Alignment.CENTER);

        icon = new Image("https://cdn-icons-png.flaticon.com/512/561/561127.png", getTranslation("contact.title"));
        icon.setWidth("32px");
        icon.setHeight("32px");

        title = new H2(getTranslation("contact.title"));
        title.getStyle().set("margin", "0 0 0 10px").set("font-size", "1.5rem");
        header.add(icon, title);

        subtitle = new Text(getTranslation("contact.subtitle"));
        subtitleDiv = new Div(subtitle);
        subtitleDiv.getStyle().set("margin-bottom", "8px");

        infoText = new Div(new Text(getTranslation("contact.info")));
        infoText.getStyle().set("font-size", "0.98em").set("margin-bottom", "12px").set("color", "#555");

        Hr divider = new Hr();
        divider.getStyle().set("margin", "18px 0 12px 0");

        Anchor mail = new Anchor("mailto:nbarrie@upv.edu.es", "nbarrie@upv.edu.es");
        mail.getStyle().set("font-weight", "bold").set("color", "#2563eb").set("font-size", "1.07em");

        copyBtn = new Button(getTranslation("contact.copy"), e -> {
            getUI().ifPresent(ui ->
                    ui.getPage().executeJs("navigator.clipboard.writeText($0)", "nbarrie@upv.edu.es")
            );
            Notification notif = Notification.show(getTranslation("contact.copied"), 2000, Notification.Position.BOTTOM_CENTER);
            notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

        copyBtn.getStyle().set("margin-top", "10px");

        VerticalLayout content = new VerticalLayout(
                illus, header, subtitleDiv, infoText, divider, mail, copyBtn
        );
        content.setSpacing(false);
        content.setPadding(false);
        content.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        card.add(content);
        add(card);
    }

    private String getTranslation(String key) {
        return i18n.getTranslation(key, getLocale());
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        illus.setAlt(getTranslation("contact.title"));
        icon.setAlt(getTranslation("contact.title"));
        title.setText(getTranslation("contact.title"));
        subtitle.setText(getTranslation("contact.subtitle"));
        infoText.setText(getTranslation("contact.info"));
        copyBtn.setText(getTranslation("contact.copy"));
    }
}
