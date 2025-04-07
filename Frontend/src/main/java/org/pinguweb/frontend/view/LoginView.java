package org.pinguweb.frontend.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.pinguweb.frontend.objects.LoginRequest;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

@Route("login")
@PageTitle("Admin - Solidarity Hub")
public class LoginView extends VerticalLayout {

    public LoginView() {
        configureView();
        createLoginForm();
    }

    private void configureView() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
        getStyle().set("background", "#ffffff");

        H1 title = new H1("Solidarity Hub MAPA");
        title.getStyle()
                .set("color", "#333333")
                .set("font-size", "3em")
                .set("margin-bottom", "1em");
    }

    private void createLoginForm() {
        LoginForm loginForm = new LoginForm();

        LoginI18n i18n = LoginI18n.createDefault();

        i18n.getForm().setTitle("Acceso administradores");
        i18n.getForm().setUsername("DNI");
        i18n.getForm().setPassword("Contraseña");
        i18n.getForm().setSubmit("Iniciar sesión");
        i18n.getForm().setForgotPassword("¿Olvidaste tu contraseña?");

        LoginI18n.ErrorMessage errorMessage = new LoginI18n.ErrorMessage();
        errorMessage.setTitle("Error de autenticación");
        errorMessage.setMessage("DNI o contraseña incorrectos. Por favor, inténtelo nuevamente.");
        i18n.setErrorMessage(errorMessage);

        loginForm.setI18n(i18n);

        loginForm.addLoginListener(event -> {
            String dni = event.getUsername();
            String password = event.getPassword();
            LoginRequest loginRequest = new LoginRequest(dni, password);

            System.out.println("Por enviar: " + dni + password);
            try {
                if (authenticate(loginRequest)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(dni, null, null);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    getUI().ifPresent(ui -> ui.navigate("dashboard"));
                } else {
                    loginForm.setError(true);
                }
            } catch (Exception e) {
                Notification.show("Error al conectar con el servidor",
                        3000,
                        Notification.Position.TOP_CENTER);
                loginForm.setError(true);
                throw e;
            }
        });

        loginForm.addForgotPasswordListener(event -> {
            Dialog dialog = new Dialog();
            dialog.setCloseOnEsc(true);
            dialog.setCloseOnOutsideClick(true);

            VerticalLayout dialogLayout = new VerticalLayout();
            dialogLayout.setAlignItems(Alignment.CENTER);

            H2 header = new H2("Recuperar contraseña");
            Paragraph instructions = new Paragraph("Introduce tu DNI para recibir un enlace de recuperación.");

            TextField dniField = new TextField("DNI");
            dniField.setWidth("100%");

            Button submitButton = new Button("Enviar", e -> {
                Notification.show("Solicitud enviada para DNI: " + dniField.getValue(), 3000, Notification.Position.TOP_CENTER);
                dialog.close();
            });

            dialogLayout.add(header, instructions, dniField, submitButton);
            dialog.add(dialogLayout);
            dialog.open();
        });

        add(loginForm);
    }

    private boolean authenticate(LoginRequest loginRequest) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/api/auth/login";
        System.out.println("Request con URL " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);
        System.out.println("Request: " + request);
        ResponseEntity<Void> response = restTemplate.postForEntity(
                url,
                request,
                Void.class
        );
        System.out.println("MENSAJE ENVIADO");
        return response.getStatusCode() == HttpStatus.OK;
    }
}

