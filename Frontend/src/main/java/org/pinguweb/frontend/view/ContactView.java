package org.pinguweb.frontend.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.Route;

@Route(value = "contact", layout = MainLayout.class)
public class ContactView extends VerticalLayout {

    public ContactView() {
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

        Image illus = new Image("icons/Support.png", "Soporte");
        illus.setWidth("110px");
        illus.setHeight("110px");
        illus.getStyle().set("margin-bottom", "15px").set("border-radius", "12px");

        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(FlexComponent.Alignment.CENTER);

        Image icon = new Image("https://cdn-icons-png.flaticon.com/512/561/561127.png", "Contacto");
        icon.setWidth("32px");
        icon.setHeight("32px");

        H2 title = new H2("Contacto");
        title.getStyle().set("margin", "0 0 0 10px").set("font-size", "1.5rem");
        header.add(icon, title);

        Text subtitle = new Text("¿Tienes dudas o necesitas ayuda?");
        Div subtitleDiv = new Div(subtitle);
        subtitleDiv.getStyle().set("margin-bottom", "8px");

        Div infoText = new Div(new Text("Responderemos lo antes posible."));
        infoText.getStyle().set("font-size", "0.98em").set("margin-bottom", "12px").set("color", "#555");

        Hr divider = new Hr();
        divider.getStyle().set("margin", "18px 0 12px 0");

        Anchor mail = new Anchor("mailto:nbarrie@upv.edu.es", "nbarrie@upv.edu.es");
        mail.getStyle().set("font-weight", "bold").set("color", "#2563eb").set("font-size", "1.07em");

        Button copyBtn = new Button("Copiar correo", e -> {
            getUI().ifPresent(ui ->
                    ui.getPage().executeJs("navigator.clipboard.writeText($0)", "nbarrie@upv.edu.es")
            );
            Notification notif = Notification.show("¡Correo copiado!", 2000, Notification.Position.BOTTOM_CENTER);
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
}
