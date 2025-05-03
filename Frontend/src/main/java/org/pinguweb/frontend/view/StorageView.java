package org.pingu.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("new-storage")
@PermitAll
public class StorageView extends VerticalLayout {

    public StorageView() {
        TextField nameField = new TextField("Nombre del almacén");
        Checkbox isFullCheckbox = new Checkbox("¿Está lleno?");
        NumberField latitudeField = new NumberField("Latitud");
        NumberField longitudeField = new NumberField("Longitud");

        Button saveButton = new Button("Crear almacén");
        Button clearButton = new Button("Limpiar");

        saveButton.addClickListener(e -> {
            if (nameField.isEmpty() || latitudeField.isEmpty() || longitudeField.isEmpty()) {
                Notification.show("Por favor, rellena todos los campos obligatorios.");
                return;
            }

            Notification.show("Datos listos para enviar al backend");
        });

        clearButton.addClickListener(e -> {
            nameField.clear();
            isFullCheckbox.clear();
            latitudeField.clear();
            longitudeField.clear();
        });

        FormLayout formLayout = new FormLayout();
        formLayout.add(nameField, isFullCheckbox, latitudeField, longitudeField, saveButton, clearButton);
        add(formLayout);
    }
}
