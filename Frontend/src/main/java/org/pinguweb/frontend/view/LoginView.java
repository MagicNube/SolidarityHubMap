package org.pinguweb.frontend.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.pinguweb.frontend.objects.LoginRequest;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Route("login")
@PageTitle("Admin · Solidarity Hub")
@CssImport("./styles/loginView.css")
public class LoginView extends VerticalLayout {

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        createLoginForm();
    }

    private void createLoginForm() {
        LoginForm loginForm = new LoginForm();
        loginForm.addClassName("login-form");

        LoginI18n i18n = LoginI18n.createDefault();

        i18n.getForm().setTitle("Iniciar sesión");
        i18n.getForm().setUsername("DNI");
        i18n.getForm().setPassword("Contraseña");
        i18n.getForm().setSubmit("Iniciar sesión");
        i18n.getForm().setForgotPassword("¿Olvidaste tu contraseña?");

        LoginI18n.ErrorMessage errorMessage = new LoginI18n.ErrorMessage();
        errorMessage.setTitle("Error de autenticación");
        errorMessage.setMessage("DNI o contraseña incorrectos.");

        i18n.setErrorMessage(errorMessage);
        loginForm.setI18n(i18n);

        loginForm.addLoginListener(e -> {
            if (authenticate(new LoginRequest(e.getUsername(), e.getPassword()))) {
                SecurityContextHolder.getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(e.getUsername(), null, null));
                getUI().ifPresent(ui -> ui.navigate("dashboard"));
            } else {
                loginForm.setError(true);
            }
        });

        loginForm.addForgotPasswordListener(e -> {
            Dialog dlg = new Dialog();
            VerticalLayout dl = new VerticalLayout();
            dl.setAlignItems(Alignment.CENTER);
            H2 h = new H2("Recuperar contraseña");
            Paragraph p = new Paragraph("Introduce tu DNI para recibir un enlace de recuperación.");
            TextField dni = new TextField("DNI");
            dni.setWidthFull();
            Button b = new Button("Enviar", ev -> {
                Notification.show("Enlace enviado a " + dni.getValue(), 3000, Notification.Position.TOP_CENTER);
                dlg.close();
            });
            dl.add(h, p, dni, b);
            dlg.add(dl);
            dlg.open();
        });

        Icon arrow = VaadinIcon.ARROW_LEFT.create();
        Button back = new Button("Volver", arrow);
        back.getStyle().set("gap", "0.5em");
        back.addClassName("back-button");
        back.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("")));

        add(loginForm, back);
    }

    private boolean authenticate(LoginRequest req) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> body = new HttpEntity<>(req, h);
        ResponseEntity<Void> resp = rt.postForEntity("http://localhost:8081/api/auth/login", body, Void.class);
        return resp.getStatusCode() == HttpStatus.OK;
    }
}
