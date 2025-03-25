package org.pinguweb.frontend.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Admin - Solidarity Hub")
public class LoginView extends VerticalLayout {

    public LoginView() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        getStyle().set("background", "#ffffff");

        H1 title = new H1("Solidarity Hub");
        title.getStyle().set("color", "#333333").set("font-size", "3em").set("margin-bottom", "1em");

        LoginForm loginForm = new LoginForm();
        loginForm.setAction("login");
        loginForm.setForgotPasswordButtonVisible(true);

        Button recoverPasswordButton = new Button("Recuperar Contraseña");
        recoverPasswordButton.addClickListener(e -> {
           //TODO Añadir lógica para recuperar contraseña
        });

        add(title, loginForm);
    }
}
