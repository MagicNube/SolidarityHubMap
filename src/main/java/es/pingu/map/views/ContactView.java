package es.pingu.map.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import es.pingu.map.commons.NavigationBar;

@Route("contact")
public class ContactView extends VerticalLayout {

    public ContactView() {
        this.setSizeFull();
        this.add(NavigationBar.createNavBar());
    }
}
