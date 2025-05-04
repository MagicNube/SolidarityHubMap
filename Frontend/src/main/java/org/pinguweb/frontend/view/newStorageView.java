package org.pinguweb.frontend.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.pingu.domain.DTO.StorageDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Route("new-storage")
@PageTitle("Crear Almacén • Solidarity Hub")
@PermitAll
public class newStorageView extends VerticalLayout {

    private TextField nameField;
    private NumberField latitudeField;
    private NumberField longitudeField;
    private Checkbox isFullCheckbox;

    private final RestTemplate rest = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8081/api/storage";

    public newStorageView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("storage-form-view");

        H1 title = new H1("Crear nuevo almacén");
        title.addClassName("storage-form-title");

        FormLayout form = buildForm();
        HorizontalLayout buttons = buildButtons();

        VerticalLayout card = new VerticalLayout(title, form, buttons);
        card.addClassName("storage-form-card");
        card.setWidth("400px");
        card.setPadding(true);
        card.setSpacing(true);

        add(card);
    }

    private FormLayout buildForm() {
        nameField = new TextField("Nombre");
        nameField.setRequiredIndicatorVisible(true);
        nameField.setWidthFull();

        latitudeField = new NumberField("Latitud");
        latitudeField.setRequiredIndicatorVisible(true);
        latitudeField.setStep(0.000001);
        latitudeField.setWidthFull();

        longitudeField = new NumberField("Longitud");
        longitudeField.setRequiredIndicatorVisible(true);
        longitudeField.setStep(0.000001);
        longitudeField.setWidthFull();

        isFullCheckbox = new Checkbox("¿Está lleno?");

        FormLayout layout = new FormLayout();
        layout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 1)
        );
        layout.add(nameField, latitudeField, longitudeField, isFullCheckbox);
        return layout;
    }

    private HorizontalLayout buildButtons() {
        Button cancel = new Button("Cancelar", e -> getUI().ifPresent(u -> u.navigate("storages")));
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button save = new Button("Guardar");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(e -> handleSave());

        HorizontalLayout hl = new HorizontalLayout(cancel, save);
        hl.setJustifyContentMode(JustifyContentMode.CENTER);
        hl.setWidthFull();
        return hl;
    }

    private void handleSave() {
        // Validar manualmente
        if (nameField.isEmpty() || latitudeField.isEmpty() || longitudeField.isEmpty()) {
            Notification.show("Por favor, completa todos los campos obligatorios.", 3000, Notification.Position.MIDDLE);
            return;
        }

        // Crear DTO con valores de los campos
        StorageDTO dto = new StorageDTO();
        dto.setName(nameField.getValue());
        dto.setLatitude(latitudeField.getValue());
        dto.setLongitude(longitudeField.getValue());
        dto.setFull(isFullCheckbox.getValue());

        // Enviar al backend
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<StorageDTO> request = new HttpEntity<>(dto, headers);

            ResponseEntity<StorageDTO> resp = rest.postForEntity(BASE_URL, request, StorageDTO.class);
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                Notification.show("Almacén creado (ID: " + resp.getBody().getID() + ")", 3000, Notification.Position.MIDDLE);
                getUI().ifPresent(u -> u.navigate("storages"));
            } else {
                Notification.show("Error al crear almacén: " + resp.getStatusCodeValue(), 3000, Notification.Position.MIDDLE);
            }
        } catch (Exception ex) {
            Notification.show("Fallo de conexión: " + ex.getMessage(), 4000, Notification.Position.MIDDLE);
        }
    }
}
