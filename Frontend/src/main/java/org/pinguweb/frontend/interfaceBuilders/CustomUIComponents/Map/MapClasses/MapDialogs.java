package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.pingu.domain.DTO.CatastropheDTO;
import org.pingu.domain.DTO.RouteDTO;
import org.pingu.domain.DTO.StorageDTO;
import org.pingu.domain.DTO.ZoneDTO;
import org.pingu.domain.enums.EmergencyLevel;
import org.pinguweb.frontend.mapObjects.Catastrophe;
import org.pinguweb.frontend.mapObjects.Storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MapDialogs {

    private final MapService controller;
    private MapBuild mapBuild;

    public MapDialogs(MapService controller) {
        this.controller = controller;
        this.mapBuild = new MapBuild(controller);
    }

    public void createDialogZona(MapState mapState) {
        if (mapState == MapState.CREATING_ZONE) {
            List<CatastropheDTO> catastropheDTOList = Catastrophe.getAllFromServer();
            List<StorageDTO> storageDTOList = Storage.getAllFromServer();
            final Icon icoClose = VaadinIcon.CLOSE.create();
            final Dialog dialog = new Dialog(icoClose);
            dialog.setDraggable(true);
            dialog.setResizable(true);
            dialog.setWidth("70vw");
            dialog.setHeight("70vh");

            H3 title = new H3("Crear zona");

            TextArea nameTextArea = new TextArea();
            nameTextArea.setPlaceholder("Nombre");
            nameTextArea.setWidth("50vw");
            nameTextArea.setHeight("5vh");

            ComboBox<String> severityComboBox = new ComboBox<>("Gravedad");
            severityComboBox.setItems(Arrays.toString(EmergencyLevel.values()));

            ComboBox<String> catastropheComboBox = new ComboBox<>("Catastrofe");
            catastropheComboBox.setItems(catastropheDTOList.stream().map(CatastropheDTO::getName).toList());

            int catastropheID = catastropheComboBox.getValue() != null ? catastropheDTOList.stream()
                    .filter(catastropheDTO -> catastropheDTO.getName().equals(catastropheComboBox.getValue()))
                    .findFirst()
                    .map(CatastropheDTO::getID)
                    .orElse(0) : 0;

            MultiSelectListBox<String> storageComboBox = new MultiSelectListBox<>();
            storageComboBox.setItems(storageDTOList.stream().map(StorageDTO::getName).toList());

            Set<String> seleccionados = storageComboBox.getSelectedItems();
            List<Integer> selectedStorageIDs = new ArrayList<>();
            for (String seleccionado : seleccionados) {
                for (StorageDTO storageDTO : storageDTOList) {
                    if (storageDTO.getName().equals(seleccionado)) {
                        selectedStorageIDs.add(storageDTO.getID());
                    }
                }
            }

            TextArea descriptionTextArea = new TextArea();
            descriptionTextArea.setPlaceholder("Descripcion");
            descriptionTextArea.setWidthFull();
            descriptionTextArea.setHeight("50vh");

            ZoneDTO zoneDTO = new ZoneDTO();
            //TODO: Mirar cambiar como se asigna el ID
            zoneDTO.setID(controller.getTempIdZone());
            controller.setTempIdZone(controller.getTempIdZone() + 1);
            zoneDTO.setDescription(descriptionTextArea.getValue());
            zoneDTO.setName(nameTextArea.getValue());
            //TODO: Mirar como funcionan las catastrofes
            zoneDTO.setCatastrophe(catastropheID);
            zoneDTO.setEmergencyLevel(severityComboBox.getValue());
            zoneDTO.setLatitudes(new ArrayList<>());
            zoneDTO.setLongitudes(new ArrayList<>());
            //TODO: Mirar como asignar storages
            zoneDTO.setStorages(selectedStorageIDs);

            Button cancelButton = new Button("Cancelar", event -> dialog.close());
            Button acceptButton = new Button("Aceptar", event -> {
                mapBuild.startZoneConstruction(zoneDTO);
                dialog.close();
            });
            acceptButton.setEnabled(false);

            nameTextArea.addValueChangeListener(event -> {
                acceptButton.setEnabled(!nameTextArea.getValue().isEmpty() && severityComboBox.getValue() != null && !descriptionTextArea.getValue().isEmpty());
            });

            descriptionTextArea.addValueChangeListener(event -> {
                acceptButton.setEnabled(!descriptionTextArea.getValue().isEmpty() && severityComboBox.getValue() != null && !nameTextArea.getValue().isEmpty());
            });

            severityComboBox.addValueChangeListener(event -> {
                acceptButton.setEnabled(!descriptionTextArea.getValue().isEmpty() && severityComboBox.getValue() != null && !nameTextArea.getValue().isEmpty());
            });

            HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, acceptButton);

            VerticalLayout dialogLayout = new VerticalLayout(title, nameTextArea, severityComboBox, catastropheComboBox, storageComboBox, descriptionTextArea, buttonLayout);
            dialog.add(dialogLayout);

            dialog.open();

            icoClose.addClickListener(iev -> dialog.close());
        } else {
            mapBuild.endZoneConstruction();
        }

    }

    public void createDialogRuta(MapState mapState) {
        if (mapState == MapState.CREATING_ROUTE) {
            final Icon icoClose = VaadinIcon.CLOSE.create();
            final Dialog dialog = new Dialog(icoClose);
            dialog.setDraggable(true);
            dialog.setResizable(true);
            dialog.setWidth("70vw");
            dialog.setHeight("40vh");
            H3 title = new H3("Crear ruta");
            TextArea nameTextArea = new TextArea();
            nameTextArea.setPlaceholder("nombre");
            nameTextArea.setWidth("50vw");
            nameTextArea.setHeight("5vh");
            ComboBox<String> routeTypeComboBox = new ComboBox<>("Tipo de ruta");
            routeTypeComboBox.setItems("Ruta Segura", "Ruta de Evacuación", "Ruta de Emergencia");

            RouteDTO routeDTO = new RouteDTO();
            routeDTO.setName(nameTextArea.getValue());
            routeDTO.setCatastrophe(0);
            routeDTO.setPoints(new ArrayList<>());
            routeDTO.setID(controller.getTempIdRoute());
            controller.setTempIdRoute(controller.getTempIdRoute() + 1);
            routeDTO.setRouteType(routeTypeComboBox.getValue());

            Button cancelButton = new Button("Cancelar", event -> dialog.close());
            Button acceptButton = new Button("Aceptar", event -> {
                mapBuild.startRouteConstruction(routeDTO);
                dialog.close();
            });

            acceptButton.setEnabled(false);
            nameTextArea.addValueChangeListener(event -> {
                acceptButton.setEnabled(!nameTextArea.getValue().isEmpty() && routeTypeComboBox.getValue() != null);
            });

            routeTypeComboBox.addValueChangeListener(event -> {
                acceptButton.setEnabled(!nameTextArea.getValue().isEmpty() && routeTypeComboBox.getValue() != null);
            });

            HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, acceptButton);
            VerticalLayout dialogLayout = new VerticalLayout(title, nameTextArea, routeTypeComboBox, buttonLayout);
            dialog.add(dialogLayout);
            dialog.open();
            icoClose.addClickListener(iev -> dialog.close());
        } else {
            mapBuild.endRouteConstruction();
        }
    }
}
