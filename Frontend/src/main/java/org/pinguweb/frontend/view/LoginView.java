package org.pinguweb.frontend.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.component.login.LoginI18n;
import jakarta.xml.bind.SchemaOutputResolver;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;

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

        LoginForm loginForm = getLoginForm();

        Button recoverPasswordButton = new Button("Recuperar Contraseña");
        recoverPasswordButton.addClickListener(e -> {
            // TODO Añadir lógica para recuperar contraseña
        });

        add(title, loginForm);
    }

    private LoginForm getLoginForm() {
        LoginForm loginForm = new LoginForm();
        loginForm.setForgotPasswordButtonVisible(true);

        loginForm.addLoginListener(event -> {
            String dni = event.getUsername();
            String password = event.getPassword();

            if (authenticate(dni, password)) {
                VaadinSession.getCurrent().setAttribute("admin", dni);
                getUI().ifPresent(ui -> ui.navigate("/dashboard"));
            } else {
                Notification.show("Credenciales incorrectas", 3000, Notification.Position.MIDDLE);
            }
        });
        return loginForm;
    }

    private boolean authenticate(String dni, String password) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/api/auth/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("dni", dni);
        requestBody.put("password", password);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, request, boolean.class);

        return Boolean.TRUE.equals(response.getBody());
    }
}
