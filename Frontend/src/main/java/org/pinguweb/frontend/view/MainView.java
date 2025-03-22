package org.pinguweb.frontend.view;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.pinguweb.frontend.GreetService;

@Route
public class MainView extends VerticalLayout {
    public MainView() {
        this.setSizeFull();
        this.add(NavigationBar.createNavBar());
    }
}