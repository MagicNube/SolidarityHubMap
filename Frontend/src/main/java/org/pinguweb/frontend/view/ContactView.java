package org.pinguweb.frontend.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;

@Route("contact")
public class ContactView extends VerticalLayout {

    public ContactView() {
        this.setSizeFull();
        this.add(NavigationBar.createNavBar());
        TextArea area = new TextArea();
        area.setLabel("Conctacta con nosotros usando el correo:" +
                "nbarrie@upv.edu.es");

        this.add(area);
    }
}