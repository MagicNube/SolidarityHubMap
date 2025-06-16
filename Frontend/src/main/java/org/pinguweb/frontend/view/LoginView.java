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
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LoginView extends VerticalLayout implements LocaleChangeObserver {

    private final I18NProvider i18n;
    private LoginForm loginForm;
    private Button back;

    @Autowired
    public LoginView(I18NProvider i18n) {
        this.i18n = i18n;
        addClassName("login-view");
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        createLoginForm();
    }

    private void createLoginForm() {
        loginForm = new LoginForm();
        loginForm.addClassName("login-form");

        LoginI18n loginI18n = createI18n();

        loginI18n.getForm().setTitle(getTranslation("login.title"));
        loginI18n.getForm().setUsername(getTranslation("login.username"));
        loginI18n.getForm().setPassword(getTranslation("login.password"));
        loginI18n.getForm().setSubmit(getTranslation("login.submit"));
        loginI18n.getForm().setForgotPassword(getTranslation("login.forgot"));

        loginForm.setI18n(loginI18n);

        loginForm.addLoginListener(e -> {
            if (authenticate(new LoginRequest(e.getUsername(), e.getPassword()))) {
                SecurityContextHolder.getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(e.getUsername(), null, null));
                getUI().ifPresent(ui -> ui.navigate("localhost:8083/home"));
            } else {
                loginForm.setError(true);
            }
        });

        loginForm.addForgotPasswordListener(e -> {
            Dialog dlg = new Dialog();
            VerticalLayout dl = new VerticalLayout();
            dl.setAlignItems(Alignment.CENTER);
            H2 h = new H2(getTranslation("login.recover.title"));
            Paragraph p = new Paragraph(getTranslation("login.recover.message"));
            TextField dni = new TextField(getTranslation("login.username"));
            dni.setWidthFull();
            Button b = new Button(getTranslation("login.recover.send"), ev -> {
                Notification.show(getTranslation("login.recover.send") + " " + dni.getValue(), 3000, Notification.Position.TOP_CENTER);
                dlg.close();
            });
            dl.add(h, p, dni, b);
            dlg.add(dl);
            dlg.open();
        });

        Icon arrow = VaadinIcon.ARROW_LEFT.create();
        back = new Button(getTranslation("login.back"), arrow);
        back.getStyle().set("gap", "0.5em");
        back.addClassName("back-button");
        back.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("")));

        add(loginForm, back);
    }

    private LoginI18n createI18n() {
        LoginI18n loginI18n = LoginI18n.createDefault();

        loginI18n.getForm().setTitle(getTranslation("login.title"));
        loginI18n.getForm().setUsername(getTranslation("login.username"));
        loginI18n.getForm().setPassword(getTranslation("login.password"));
        loginI18n.getForm().setSubmit(getTranslation("login.submit"));
        loginI18n.getForm().setForgotPassword(getTranslation("login.forgot"));

        LoginI18n.ErrorMessage errorMessage = new LoginI18n.ErrorMessage();
        errorMessage.setTitle(getTranslation("login.error.title"));
        errorMessage.setMessage(getTranslation("login.error.message"));

        loginI18n.setErrorMessage(errorMessage);
        return loginI18n;
    }

    private boolean authenticate(LoginRequest req) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> body = new HttpEntity<>(req, h);
        ResponseEntity<Void> resp = rt.postForEntity("http://localhost:8081/api/auth/login", body, Void.class);
        return resp.getStatusCode() == HttpStatus.OK;
    }

    private String getTranslation(String key) {
        return i18n.getTranslation(key, getLocale());
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        loginForm.setI18n(createI18n());
        back.setText(getTranslation("login.back"));
    }
}
