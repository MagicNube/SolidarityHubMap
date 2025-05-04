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
import org.pingu.domain.enums.RouteType;
import org.pinguweb.frontend.mapObjects.Catastrophe;
import org.pinguweb.frontend.mapObjects.Route;
import org.pinguweb.frontend.mapObjects.Storage;
import org.pinguweb.frontend.mapObjects.Zone;
import org.pinguweb.frontend.view.MapView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MapDialogs {

    private final MapService service;
    private final MapBuild mapBuild;
    private final MapButtons mapButtons;

    public MapDialogs(MapService service, MapButtons mapButtons) {
        this.service = service;
        this.mapButtons = mapButtons;
        this.mapBuild = new MapBuild(service);
        MapView.setMapDialogs(this);
    }

    public void createDialogZona(MapState mapState) {
        if (mapState == MapState.CREATING_ZONE) {
            List<CatastropheDTO> catastropheDTOList = Catastrophe.getAllFromServer();
            List<StorageDTO> storageDTOList = Storage.getAllFromServer();
            final Icon icoClose = VaadinIcon.CLOSE.create();
            final Dialog dialog = new Dialog(icoClose);
            dialog.setCloseOnEsc(false);
            dialog.setCloseOnOutsideClick(false);
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
            String[] severityOptions = Arrays.stream(EmergencyLevel.values())
                    .map(EmergencyLevel::getName)
                    .toArray(String[]::new);
            severityComboBox.setItems(severityOptions);

            ComboBox<String> catastropheComboBox = new ComboBox<>("Catastrofe");
            catastropheComboBox.setItems(catastropheDTOList.stream().map(CatastropheDTO::getName).toList());

            MultiSelectListBox<String> storageComboBox = new MultiSelectListBox<>();
            storageComboBox.setItems(storageDTOList.stream().map(StorageDTO::getName).toList());

            TextArea descriptionTextArea = new TextArea();
            descriptionTextArea.setPlaceholder("Descripcion");
            descriptionTextArea.setWidthFull();
            descriptionTextArea.setHeight("50vh");

            Button cancelButton = new Button("Cancelar");
            cancelButton.addClickListener(event -> {
                mapButtons.cancelZoneCreation();
                dialog.close();
            });
            Button acceptButton = new Button("Aceptar", event -> {
                int catastropheID = catastropheComboBox.getValue() != null ? catastropheDTOList.stream()
                        .filter(catastropheDTO -> catastropheDTO.getName().equals(catastropheComboBox.getValue()))
                        .findFirst()
                        .map(CatastropheDTO::getID)
                        .orElse(0) : 0;


                Set<String> seleccionados = storageComboBox.getSelectedItems();
                List<Integer> selectedStorageIDs = new ArrayList<>();
                for (String seleccionado : seleccionados) {
                    for (StorageDTO storageDTO : storageDTOList) {
                        if (storageDTO.getName().equals(seleccionado)) {
                            selectedStorageIDs.add(storageDTO.getID());
                        }
                    }
                }

                ZoneDTO zoneDTO = new ZoneDTO();
                zoneDTO.setID(service.getTempIdZone());
                service.setTempIdZone(service.getTempIdZone() + 1);
                zoneDTO.setDescription(descriptionTextArea.getValue());
                zoneDTO.setName(nameTextArea.getValue());
                //TODO: Mirar como funcionan las catastrofes
                zoneDTO.setCatastrophe(catastropheID);
                zoneDTO.setEmergencyLevel(severityComboBox.getValue());
                zoneDTO.setLatitudes(new ArrayList<>());
                zoneDTO.setLongitudes(new ArrayList<>());
                //TODO: Mirar como asignar storages
                zoneDTO.setStorages(selectedStorageIDs);


                mapBuild.startZoneConstruction(zoneDTO);
                dialog.close();
            });
            acceptButton.setEnabled(false);

            nameTextArea.addValueChangeListener(event -> {
                acceptButton.setEnabled(!nameTextArea.getValue().isEmpty() && severityComboBox.getValue() != null && !descriptionTextArea.getValue().isEmpty() && !catastropheComboBox.getValue().isEmpty() && !storageComboBox.getValue().isEmpty());
            });
            descriptionTextArea.addValueChangeListener(event -> {
                acceptButton.setEnabled(!descriptionTextArea.getValue().isEmpty() && severityComboBox.getValue() != null && !nameTextArea.getValue().isEmpty() && !catastropheComboBox.getValue().isEmpty() && !storageComboBox.getValue().isEmpty());
            });
            severityComboBox.addValueChangeListener(event -> {
                acceptButton.setEnabled(!descriptionTextArea.getValue().isEmpty() && severityComboBox.getValue() != null && !nameTextArea.getValue().isEmpty() && !catastropheComboBox.getValue().isEmpty() && !storageComboBox.getValue().isEmpty());
            });
            catastropheComboBox.addValueChangeListener(event -> {
                acceptButton.setEnabled(!descriptionTextArea.getValue().isEmpty() && severityComboBox.getValue() != null && !nameTextArea.getValue().isEmpty() && !catastropheComboBox.getValue().isEmpty() && !storageComboBox.getValue().isEmpty());
            });
            storageComboBox.addValueChangeListener(event -> {
                acceptButton.setEnabled(!descriptionTextArea.getValue().isEmpty() && severityComboBox.getValue() != null && !nameTextArea.getValue().isEmpty() && !catastropheComboBox.getValue().isEmpty() && !storageComboBox.getValue().isEmpty());
            });

            HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, acceptButton);

            VerticalLayout dialogLayout = new VerticalLayout(title, nameTextArea, severityComboBox, catastropheComboBox, storageComboBox, descriptionTextArea, buttonLayout);
            dialog.add(dialogLayout);

            dialog.open();

            icoClose.addClickListener(iev -> {
                mapButtons.cancelZoneCreation();
                dialog.close();
            });

        } else {
            mapBuild.endZoneConstruction();
        }

    }

    public void createDialogRuta(MapState mapState) {
        if (mapState == MapState.CREATING_ROUTE) {
            final Icon icoClose = VaadinIcon.CLOSE.create();
            final Dialog dialog = new Dialog(icoClose);
            List<CatastropheDTO> catastropheDTOList = Catastrophe.getAllFromServer();
            dialog.setCloseOnEsc(false);
            dialog.setCloseOnOutsideClick(false);
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
            String[] routeTypeOptions = Arrays.stream(RouteType.values())
                    .map(RouteType::getName)
                    .toArray(String[]::new);
            routeTypeComboBox.setItems(routeTypeOptions);

            ComboBox<String> catastropheComboBox = new ComboBox<>("Catastrofe");
            catastropheComboBox.setItems(catastropheDTOList.stream().map(CatastropheDTO::getName).toList());


            Button cancelButton = new Button("Cancelar");
            cancelButton.addClickListener(event -> {
                mapButtons.cancelRouteCreation();
                dialog.close();
            });
            Button acceptButton = new Button("Aceptar", event -> {
                int catastropheID = catastropheComboBox.getValue() != null ? catastropheDTOList.stream()
                        .filter(catastropheDTO -> catastropheDTO.getName().equals(catastropheComboBox.getValue()))
                        .findFirst()
                        .map(CatastropheDTO::getID)
                        .orElse(0) : 0;

                RouteDTO routeDTO = new RouteDTO();
                routeDTO.setName(nameTextArea.getValue());
                routeDTO.setCatastrophe(catastropheID);
                routeDTO.setPoints(new ArrayList<>());
                routeDTO.setID(service.getTempIdRoute());
                service.setTempIdRoute(service.getTempIdRoute() + 1);
                routeDTO.setRouteType(routeTypeComboBox.getValue());


                mapBuild.startRouteConstruction(routeDTO);
                dialog.close();
            });

            acceptButton.setEnabled(false);
            nameTextArea.addValueChangeListener(event -> {
                acceptButton.setEnabled(!nameTextArea.getValue().isEmpty() && routeTypeComboBox.getValue() != null && !catastropheComboBox.getValue().isEmpty());
            });
            routeTypeComboBox.addValueChangeListener(event -> {
                acceptButton.setEnabled(!nameTextArea.getValue().isEmpty() && routeTypeComboBox.getValue() != null && !catastropheComboBox.getValue().isEmpty());
            });
            catastropheComboBox.addValueChangeListener(event -> {
                acceptButton.setEnabled(!nameTextArea.getValue().isEmpty() && routeTypeComboBox.getValue() != null && !catastropheComboBox.getValue().isEmpty());
            });
            HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, acceptButton);
            VerticalLayout dialogLayout = new VerticalLayout(title, nameTextArea, routeTypeComboBox, catastropheComboBox, buttonLayout);
            dialog.add(dialogLayout);
            dialog.open();
            icoClose.addClickListener(iev -> {
                mapButtons.cancelRouteCreation();
                dialog.close();
            });
        } else {
            mapBuild.endRouteConstruction();
        }
    }

    public void editDialogZone(String zoneID) {
        Zone zone = service.getZoneByID(zoneID);
        final Icon icoClose = VaadinIcon.CLOSE.create();
        final Dialog dialog = new Dialog(icoClose);
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setWidth("70vw");
        dialog.setHeight("70vh");

        H3 title = new H3("Editar zona");

        TextArea nameTextArea = new TextArea();
        nameTextArea.setPlaceholder("Nombre");
        nameTextArea.setWidth("50vw");
        nameTextArea.setHeight("5vh");
        nameTextArea.setValue(zone.getName());

        ComboBox<String> severityComboBox = new ComboBox<>("Gravedad");
        String[] severityOptions = Arrays.stream(EmergencyLevel.values())
                .map(EmergencyLevel::getName)
                .toArray(String[]::new);
        severityComboBox.setItems(severityOptions);
        severityComboBox.setValue(zone.getEmergencyLevel());

        ComboBox<String> catastropheComboBox = new ComboBox<>("Catastrofe");
        catastropheComboBox.setItems(Catastrophe.getAllFromServer().stream().map(CatastropheDTO::getName).toList());

        MultiSelectListBox<String> storageComboBox = new MultiSelectListBox<>();
        storageComboBox.setItems(Storage.getAllFromServer().stream().map(StorageDTO::getName).toList());

        Set<String> selectedStorageNames = zone.getStorages().stream()
                .map(storageID -> Storage.getAllFromServer().stream()
                        .filter(storageDTO -> storageDTO.getID() == storageID)
                        .findFirst()
                        .map(StorageDTO::getName)
                        .orElse(null))
                .filter(name -> name != null)
                .collect(Collectors.toSet());

        storageComboBox.setValue(selectedStorageNames);

        TextArea descriptionTextArea = new TextArea();
        descriptionTextArea.setPlaceholder("Descripcion");
        descriptionTextArea.setWidthFull();
        descriptionTextArea.setHeight("50vh");
        descriptionTextArea.setValue(zone.getDescription());

        Button cancelButton = new Button("Cancelar");
        cancelButton.addClickListener(event -> {
            dialog.close();
        });
        Button acceptButton = new Button("Aceptar", event -> {
            int catastropheID = catastropheComboBox.getValue() != null ? Catastrophe.getAllFromServer().stream()
                    .filter(catastropheDTO -> catastropheDTO.getName().equals(catastropheComboBox.getValue()))
                    .findFirst()
                    .map(CatastropheDTO::getID)
                    .orElse(0) : 0;

            Set<String> seleccionados = storageComboBox.getSelectedItems();
            List<Integer> selectedStorageIDs = new ArrayList<>();
            for (String seleccionado : seleccionados) {
                for (StorageDTO storageDTO : Storage.getAllFromServer()) {
                    if (storageDTO.getName().equals(seleccionado)) {
                        selectedStorageIDs.add(storageDTO.getID());
                    }
                }
            }
            zone.setDescription(descriptionTextArea.getValue());
            zone.setName(nameTextArea.getValue());
            zone.setCatastrophe(catastropheID);
            zone.setEmergencyLevel(severityComboBox.getValue());
            zone.setStorages(selectedStorageIDs);
            zone.setLatitudes(new ArrayList<>());
            zone.setLongitudes(new ArrayList<>());
            //TODO:PATCH
            dialog.close();
        });

        acceptButton.setEnabled(false);
        nameTextArea.addValueChangeListener(event -> {
            acceptButton.setEnabled(!nameTextArea.getValue().isEmpty() && severityComboBox.getValue() != null && !descriptionTextArea.getValue().isEmpty() && !catastropheComboBox.getValue().isEmpty() && !storageComboBox.getValue().isEmpty());
        });
        descriptionTextArea.addValueChangeListener(event -> {
            acceptButton.setEnabled(!descriptionTextArea.getValue().isEmpty() && severityComboBox.getValue() != null && !nameTextArea.getValue().isEmpty() && !catastropheComboBox.getValue().isEmpty() && !storageComboBox.getValue().isEmpty());
        });
        severityComboBox.addValueChangeListener(event -> {
            acceptButton.setEnabled(!descriptionTextArea.getValue().isEmpty() && severityComboBox.getValue() != null && !nameTextArea.getValue().isEmpty() && !catastropheComboBox.getValue().isEmpty() && !storageComboBox.getValue().isEmpty());
        });
        catastropheComboBox.addValueChangeListener(event -> {
            acceptButton.setEnabled(!descriptionTextArea.getValue().isEmpty() && severityComboBox.getValue() != null && !nameTextArea.getValue().isEmpty() && !catastropheComboBox.getValue().isEmpty() && !storageComboBox.getValue().isEmpty());
        });
        storageComboBox.addValueChangeListener(event -> {
            acceptButton.setEnabled(!descriptionTextArea.getValue().isEmpty() && severityComboBox.getValue() != null && !nameTextArea.getValue().isEmpty() && !catastropheComboBox.getValue().isEmpty() && !storageComboBox.getValue().isEmpty());
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, acceptButton);
        VerticalLayout dialogLayout = new VerticalLayout(title, nameTextArea, severityComboBox, catastropheComboBox, storageComboBox, descriptionTextArea, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
        icoClose.addClickListener(iev -> {
            dialog.close();
        });
    }

    public void editDialogRoute(String routeID) {
        Route route = service.getRouteByID(routeID);
        final Icon icoClose = VaadinIcon.CLOSE.create();
        final Dialog dialog = new Dialog(icoClose);
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setWidth("70vw");
        dialog.setHeight("40vh");
        H3 title = new H3("Editar ruta");
        TextArea nameTextArea = new TextArea();
        nameTextArea.setPlaceholder("nombre");
        nameTextArea.setWidth("50vw");
        nameTextArea.setHeight("5vh");
        nameTextArea.setValue(route.getName());

        ComboBox<String> routeTypeComboBox = new ComboBox<>("Tipo de ruta");
        String[] routeTypeOptions = Arrays.stream(RouteType.values())
                .map(RouteType::getName)
                .toArray(String[]::new);
        routeTypeComboBox.setItems(routeTypeOptions);
        routeTypeComboBox.setValue(route.getRouteType());

        Button cancelButton = new Button("Cancelar");
        cancelButton.addClickListener(event -> {
            dialog.close();
        });
        Button acceptButton = new Button("Aceptar", event -> {
            route.setName(nameTextArea.getValue());
            route.setRouteType(routeTypeComboBox.getValue());
            //TODO:PATCH
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
        icoClose.addClickListener(iev -> {
            dialog.close();
        });
    }
}
