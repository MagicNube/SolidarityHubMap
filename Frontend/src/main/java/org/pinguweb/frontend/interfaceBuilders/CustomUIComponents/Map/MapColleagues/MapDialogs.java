package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.CatastropheDTO;
import org.pingu.domain.DTO.RouteDTO;
import org.pingu.domain.DTO.StorageDTO;
import org.pingu.domain.DTO.ZoneDTO;
import org.pingu.domain.enums.EmergencyLevel;
import org.pingu.domain.enums.RouteType;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.CreateRouteCommand;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.CreateStorageCommand;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.CreateZoneCommand;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.EditCommand;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Map;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.ClickedEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.enums.DialogsNames;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.CreationEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.GenericEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.ShowEvent;
import org.pinguweb.frontend.mapObjects.*;
import org.pinguweb.frontend.services.BackendDTOService;
import org.pinguweb.frontend.utils.Mediador.*;
import org.yaml.snakeyaml.util.Tuple;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class MapDialogs extends ComponentColleague {

    private final BackendDTOService backendService = BackendDTOService.GetInstancia();

    private final Map map;

    public MapDialogs(Map map) {
        super(map);
        this.map = map;
    }

    @Override
    public void register() {
        mediator.subscribe(EventType.SHOW_DIALOG, this);
        mediator.subscribe(EventType.SHOW_EDIT, this);
        mediator.subscribe(EventType.EXIT, this);
        mediator.subscribe(EventType.CHANGE_BANNER, this);
    }

    @Override
    public <T> void receive(Event<T> event) {
        switch (event.getType()) {
            case EXIT -> exit();
            case SHOW_DIALOG -> {
                ShowEvent<T> e = (ShowEvent<T>) event;
                if (e.getName() == DialogsNames.ZONE) {
                    createDialogZona((CreateZoneCommand) event.getCommand());
                } else if (e.getName() == DialogsNames.ROUTE) {
                    createDialogRuta((CreateRouteCommand) event.getCommand());
                } else if (e.getName() == DialogsNames.STORAGE) {
                    createDialogAlmacen((Tuple<Double, Double>) e.getPayload(), (CreateStorageCommand) event.getCommand());
                }
            }
            case SHOW_EDIT -> {
                ShowEvent<T> e = (ShowEvent<T>) event;
                if (e.getName() == DialogsNames.ZONE) {
                    editDialogZone((String) e.getPayload(), (EditCommand) event.getCommand());
                } else if (e.getName() == DialogsNames.ROUTE) {
                    editDialogRoute((String) e.getPayload(), (EditCommand) event.getCommand());
                } else if (e.getName() == DialogsNames.STORAGE) {
                    editDialogStorage((String) e.getPayload(), (EditCommand) event.getCommand());
                }
            }
            case CHANGE_BANNER -> {
                editStateOfMap((String) event.getPayload());
            }
        }
    }

    private void exit() {
        map.getMap().off("click");
        map.getMapContainer().getClassNames().clear();

        for (Tuple<Double, Double> p : map.getZoneMarkerPoints()) {
            map.getZoneMarkers().get(p).removeFromMap(map.getMap());
        }
        map.getZoneMarkers().clear();
        map.getZoneMarkerPoints().clear();

        for (RoutePoint p : map.getNewRoutePoints()) {
            p.removeFromMap(map.getMap());
        }

        map.getNewRoutePoints().clear();
        editStateOfMap("Navegación");
    }

    private void createDialogZona(CreateZoneCommand c) {
        map.getMap().off("click", map.getMap().clientComponentJsAccessor() + ".myClickFuncCreateZone");
        List<CatastropheDTO> catastropheDTOList = backendService.getCatastropheList().getValues();
        List<StorageDTO> storageDTOList = backendService.getStorageList().getValues();
        final Icon icoClose = VaadinIcon.CLOSE.create();
        final Dialog dialog = new Dialog(icoClose);
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setWidth("70vw");
        dialog.setHeight("70vh");

        H3 title = new H3("Crear zona");
//texto pequeño para indicar que los que contienen * son obligatorios
        Span requiredFieldsInfo = new Span("Los campos con · son obligatorios");
        requiredFieldsInfo.getStyle().set("color", "red");
        requiredFieldsInfo.getStyle().set("font-weight", "bold");
        requiredFieldsInfo.getStyle().set("font-size", "0.65em");

        // Campo obligatorio: Nombre
        TextArea nameTextArea = new TextArea("Nombre");
        nameTextArea.setPlaceholder("Introduce el nombre de la zona");
        nameTextArea.setRequiredIndicatorVisible(true);
        nameTextArea.setWidth("50vw");
        nameTextArea.setHeight("5vh");

        ComboBox<String> severityComboBox = new ComboBox<>("Gravedad");
        String[] severityOptions = Arrays.stream(EmergencyLevel.values())
                .map(EmergencyLevel::name)
                .toArray(String[]::new);
        severityComboBox.setItems(severityOptions);
        severityComboBox.setRequiredIndicatorVisible(true);


        ComboBox<String> catastropheComboBox = new ComboBox<>("Catastrofe");
        catastropheComboBox.setItems(catastropheDTOList.stream().map(CatastropheDTO::getName).toList());
        catastropheComboBox.setRequiredIndicatorVisible(true);

        Span storageLabel = new Span("Almacenes ");
        Span storageMark = new Span("·");
        storageMark.getStyle().set("bold", "true");
        storageLabel.add(storageMark);
        MultiSelectListBox<String> storageComboBox = new MultiSelectListBox<>();
        storageComboBox.setItems(storageDTOList.stream().map(StorageDTO::getName).toList());

        TextArea descriptionTextArea = new TextArea("Descripcion");
        descriptionTextArea.setPlaceholder("Añade una descripcion a la zona");
        descriptionTextArea.setRequiredIndicatorVisible(true);
        descriptionTextArea.setWidthFull();
        descriptionTextArea.setHeight("50vh");

        Button cancelButton = new Button("Cancelar");
        cancelButton.addClickListener(event -> {
            mediator.publish(new GenericEvent<>(EventType.EXIT, null, c));
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
            zoneDTO.setID(0);
            zoneDTO.setDescription(descriptionTextArea.getValue());
            zoneDTO.setName(nameTextArea.getValue());
            zoneDTO.setCatastrophe(catastropheID);
            zoneDTO.setEmergencyLevel(severityComboBox.getValue());

            ArrayList<Double> latitudes = new ArrayList<>();
            ArrayList<Double> longitudes = new ArrayList<>();

            log.info(" size {} {}", map.getZoneMarkers().keySet(), map.getZoneMarkerPoints().toArray());

            for (Tuple<Double, Double> point : map.getZoneMarkerPoints()) {
                latitudes.add(point._1());
                longitudes.add(point._2());
                map.getZoneMarkers().get(point).removeFromMap(map.getMap());
            }

            map.getZoneMarkerPoints().clear();
            map.getZoneMarkers().clear();

            zoneDTO.setLatitudes(latitudes);
            zoneDTO.setLongitudes(longitudes);
            zoneDTO.setStorages(selectedStorageIDs);

            mediator.publish(new CreationEvent<>(EventType.CREATE, zoneDTO, c, null));
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

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(
                title,
                requiredFieldsInfo,
                nameTextArea,
                severityComboBox,
                catastropheComboBox,
                storageLabel,
                storageComboBox,
                descriptionTextArea,
                buttonLayout
        );
        dialog.add(dialogLayout);
        dialog.open();

        icoClose.addClickListener(iev -> {
            mediator.publish(new GenericEvent<>(EventType.EXIT, null, c));
            dialog.close();
        });
    }

    private void createDialogRuta(CreateRouteCommand c) {
        map.getMap().off("click", map.getMap().clientComponentJsAccessor() + ".myClickFuncCreateRoute");
        final Icon icoClose = VaadinIcon.CLOSE.create();
        final Dialog dialog = new Dialog(icoClose);
        List<CatastropheDTO> catastropheDTOList = backendService.getCatastropheList().getValues();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setWidth("70vw");
        dialog.setHeight("40vh");
        H3 title = new H3("Crear ruta");
        TextArea nameTextArea = new TextArea("Nombre");
        nameTextArea.setPlaceholder("Introduce el nombre de la ruta");
        nameTextArea.setRequiredIndicatorVisible(true);
        nameTextArea.setWidth("50vw");
        nameTextArea.setHeight("7vh");

        //texto pequeño para indicar que los que contienen * son obligatorios
        Span requiredFieldsInfo = new Span("Los campos con · son obligatorios");
        requiredFieldsInfo.getStyle().set("color", "red");
        requiredFieldsInfo.getStyle().set("font-weight", "bold");
        requiredFieldsInfo.getStyle().set("font-size", "0.65em");

        ComboBox<String> routeTypeComboBox = new ComboBox<>("Tipo de ruta");
        routeTypeComboBox.setRequiredIndicatorVisible(true);
        String[] routeTypeOptions = Arrays.stream(RouteType.values())
                .map(RouteType::name)
                .toArray(String[]::new);
        routeTypeComboBox.setItems(routeTypeOptions);

        ComboBox<String> catastropheComboBox = new ComboBox<>("Catastrofe");
        catastropheComboBox.setRequiredIndicatorVisible(true);
        catastropheComboBox.setItems(catastropheDTOList.stream().map(CatastropheDTO::getName).toList());

        Button cancelButton = new Button("Cancelar");
        cancelButton.addClickListener(event -> {
            mediator.publish(new GenericEvent<>(EventType.EXIT, null, c));
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
            routeDTO.setID(0);
            routeDTO.setRouteType(routeTypeComboBox.getValue());

            ArrayList<RoutePoint> points = new ArrayList<>(map.getNewRoutePoints());

            routeDTO.setPoints(points.stream().map(MapObject::getID).toList());

            mediator.publish(new CreationEvent<>(EventType.CREATE, routeDTO, c, points));
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
        VerticalLayout dialogLayout = new VerticalLayout(title, requiredFieldsInfo, nameTextArea, routeTypeComboBox, catastropheComboBox, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
        icoClose.addClickListener(iev -> {
            mediator.publish(new GenericEvent<>(EventType.EXIT, null, c));
            dialog.close();
        });
    }

    private void createDialogAlmacen(Tuple<Double, Double> coords, CreateStorageCommand c) {
        final Icon icoClose = VaadinIcon.CLOSE.create();
        final Dialog dialog = new Dialog(icoClose);
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setWidth("70vw");
        dialog.setHeight("40vh");
        H3 title = new H3("Crear almacen");
        TextArea nameTextArea = new TextArea("Nombre");
        nameTextArea.setPlaceholder("Introduce el nombre del almacen");
        nameTextArea.setRequiredIndicatorVisible(true);
        nameTextArea.setWidth("50vw");
        nameTextArea.setHeight("5vh");

        //texto pequeño para indicar que los que contienen * son obligatorios
        Span requiredFieldsInfo = new Span("Los campos con · son obligatorios");
        requiredFieldsInfo.getStyle().set("color", "red");
        requiredFieldsInfo.getStyle().set("font-weight", "bold");
        requiredFieldsInfo.getStyle().set("font-size", "0.65em");

        ComboBox<String> zoneComboBox = new ComboBox<>("Zona");
        zoneComboBox.setRequiredIndicatorVisible(true);
        zoneComboBox.setItems(backendService.getZoneList().getValues().stream().map(ZoneDTO::getName).toList());

        ComboBox<String> llenoComboBox = new ComboBox<>("Estado");
        llenoComboBox.setRequiredIndicatorVisible(true);
        String[] llenoOptions = {"Lleno", "Vacio"};
        llenoComboBox.setItems(llenoOptions);
        llenoComboBox.setValue("Vacio");

        Button cancelButton = new Button("Cancelar");
        cancelButton.addClickListener(event -> {
            mediator.publish(new GenericEvent<>(EventType.EXIT, null, c));
            dialog.close();
        });
        Button acceptButton = new Button("Aceptar", event -> {
            int zoneID = zoneComboBox.getValue() != null ? backendService.getZoneList().getValues().stream()
                    .filter(zoneDTO -> zoneDTO.getName().equals(zoneComboBox.getValue()))
                    .findFirst()
                    .map(ZoneDTO::getID)
                    .orElse(0) : 0;
            StorageDTO storageDTO = new StorageDTO();
            storageDTO.setName(nameTextArea.getValue());
            storageDTO.setID(0);
            storageDTO.setZone(zoneID);
            storageDTO.setFull(llenoComboBox.getValue().equals("Lleno"));
            storageDTO.setLatitude(coords._1());
            storageDTO.setLongitude(coords._2());

            mediator.publish(new CreationEvent<>(EventType.CREATE, storageDTO, c, null));
            dialog.close();
        });

        acceptButton.setEnabled(false);
        nameTextArea.addValueChangeListener(event -> {
            acceptButton.setEnabled(!nameTextArea.getValue().isEmpty() && zoneComboBox.getValue() != null && llenoComboBox.getValue() != null);
        });
        zoneComboBox.addValueChangeListener(event -> {
            acceptButton.setEnabled(!nameTextArea.getValue().isEmpty() && zoneComboBox.getValue() != null && llenoComboBox.getValue() != null);
        });
        llenoComboBox.addValueChangeListener(event -> {
            acceptButton.setEnabled(!nameTextArea.getValue().isEmpty() && zoneComboBox.getValue() != null && llenoComboBox.getValue() != null);
        });


        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, acceptButton);
        VerticalLayout dialogLayout = new VerticalLayout(title, requiredFieldsInfo, nameTextArea, zoneComboBox, llenoComboBox, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
        icoClose.addClickListener(iev -> {
            mediator.publish(new GenericEvent<>(EventType.EXIT, null, c));
            dialog.close();
        });
    }

    private void editDialogZone(String zoneID, EditCommand c) {
        Zone zone = getZoneByID(zoneID);
        c.setOriginalObject(zone);
        final Icon icoClose = VaadinIcon.CLOSE.create();
        final Dialog dialog = new Dialog(icoClose);
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setWidth("70vw");
        dialog.setHeight("70vh");

        H3 title = new H3("Editar zona");

        //texto pequeño para indicar que los que contienen * son obligatorios
        Span requiredFieldsInfo = new Span("Los campos con · son obligatorios");
        requiredFieldsInfo.getStyle().set("color", "red");
        requiredFieldsInfo.getStyle().set("font-weight", "bold");
        requiredFieldsInfo.getStyle().set("font-size", "0.65em");

        TextArea nameTextArea = new TextArea("Nombre");
        nameTextArea.setPlaceholder("Introduce el nombre de la zona");
        nameTextArea.setRequiredIndicatorVisible(true);
        nameTextArea.setWidth("50vw");
        nameTextArea.setHeight("5vh");
        nameTextArea.setValue(zone.getName());

        ComboBox<String> severityComboBox = new ComboBox<>("Gravedad");
        severityComboBox.setRequiredIndicatorVisible(true);
        String[] severityOptions = Arrays.stream(EmergencyLevel.values())
                .map(EmergencyLevel::name)
                .toArray(String[]::new);
        severityComboBox.setItems(severityOptions);
        severityComboBox.setValue(zone.getEmergencyLevel());

        ComboBox<String> catastropheComboBox = new ComboBox<>("Catastrofe");
        catastropheComboBox.setRequiredIndicatorVisible(true);
        catastropheComboBox.setItems(backendService.getCatastropheList().getValues().stream().map(CatastropheDTO::getName).toList());
        catastropheComboBox.setValue(
                backendService.getCatastropheList().stream().filter(x -> x.getID() == zone.getCatastrophe()).findFirst().get().getName()
        );

        Span storageLabel = new Span("Almacenes ");
        Span storageMark = new Span("·");
        storageMark.getStyle().set("bold", "true");
        storageLabel.add(storageMark);
        MultiSelectListBox<String> storageComboBox = new MultiSelectListBox<>();
        storageComboBox.setItems(backendService.getStorageList().getValues().stream().map(StorageDTO::getName).toList());

        Set<String> selectedStorageNames = zone.getStorages().stream()
                .map(storageID -> backendService.getStorageList().getValues().stream()
                        .filter(storageDTO -> storageDTO.getID() == storageID)
                        .findFirst()
                        .map(StorageDTO::getName)
                        .orElse(null))
                .filter(name -> name != null)
                .collect(Collectors.toSet());

        storageComboBox.setValue(selectedStorageNames);

        TextArea descriptionTextArea = new TextArea("Descripcion");
        descriptionTextArea.setPlaceholder("Añade una descripcion a la zona");
        descriptionTextArea.setRequiredIndicatorVisible(true);
        descriptionTextArea.setWidthFull();
        descriptionTextArea.setHeight("50vh");
        descriptionTextArea.setValue(zone.getDescription());

        Button cancelButton = new Button("Cancelar");
        cancelButton.addClickListener(event -> {
            mediator.publish(new GenericEvent<>(EventType.EXIT, null, c));
            dialog.close();
        });
        Button acceptButton = new Button("Aceptar", event -> {
            int catastropheID = catastropheComboBox.getValue() != null ? backendService.getCatastropheList().getValues().stream()
                    .filter(catastropheDTO -> catastropheDTO.getName().equals(catastropheComboBox.getValue()))
                    .findFirst()
                    .map(CatastropheDTO::getID)
                    .orElse(0) : 0;

            Set<String> seleccionados = storageComboBox.getSelectedItems();
            List<Integer> selectedStorageIDs = new ArrayList<>();
            for (String seleccionado : seleccionados) {
                for (StorageDTO storageDTO : backendService.getStorageList().getValues()) {
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
            zone.setID(Integer.parseInt(zoneID));
            mediator.publish(new GenericEvent<>(EventType.EDIT, zone, null));
            c.setResultObject(zone);
            c.endExecution();
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
        VerticalLayout dialogLayout = new VerticalLayout(title, requiredFieldsInfo, nameTextArea, severityComboBox, catastropheComboBox, storageLabel, storageComboBox, descriptionTextArea, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
        icoClose.addClickListener(iev -> {
            mediator.publish(new GenericEvent<>(EventType.EXIT, null, c));
            dialog.close();
        });
    }

    private void editDialogRoute(String routeID, EditCommand c) {
        Route route = getRouteByID(routeID);
        c.setOriginalObject(route);
        final Icon icoClose = VaadinIcon.CLOSE.create();
        final Dialog dialog = new Dialog(icoClose);
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setWidth("70vw");
        dialog.setHeight("40vh");
        H3 title = new H3("Editar ruta");

        //texto pequeño para indicar que los que contienen * son obligatorios
        Span requiredFieldsInfo = new Span("Los campos con · son obligatorios");
        requiredFieldsInfo.getStyle().set("color", "red");
        requiredFieldsInfo.getStyle().set("font-weight", "bold");
        requiredFieldsInfo.getStyle().set("font-size", "0.65em");

        TextArea nameTextArea = new TextArea("Nombre");
        nameTextArea.setPlaceholder("Introduce el nombre de la ruta");
        nameTextArea.setRequiredIndicatorVisible(true);
        nameTextArea.setWidth("50vw");
        nameTextArea.setHeight("7vh");
        nameTextArea.setValue(route.getName());

        ComboBox<String> routeTypeComboBox = new ComboBox<>("Tipo de ruta");
        routeTypeComboBox.setRequiredIndicatorVisible(true);
        String[] routeTypeOptions = Arrays.stream(RouteType.values())
                .map(RouteType::name)
                .toArray(String[]::new);
        routeTypeComboBox.setItems(routeTypeOptions);
        routeTypeComboBox.setValue(route.getRouteType());

        ComboBox<String> catastropheComboBox = new ComboBox<>("Catastrofe");
        catastropheComboBox.setRequiredIndicatorVisible(true);
        catastropheComboBox.setItems(backendService.getCatastropheList().getValues().stream().map(CatastropheDTO::getName).toList());
        catastropheComboBox.setValue(
                backendService.getCatastropheList().stream().filter(x -> x.getID() == route.getCatastrophe()).findFirst().get().getName()
        );
        Button cancelButton = new Button("Cancelar");
        cancelButton.addClickListener(event -> {
            mediator.publish(new GenericEvent<>(EventType.EXIT, null, c));
            dialog.close();
        });
        Button acceptButton = new Button("Aceptar", event -> {
            int catastropheID = catastropheComboBox.getValue() != null ? backendService.getCatastropheList().getValues().stream()
                    .filter(catastropheDTO -> catastropheDTO.getName().equals(catastropheComboBox.getValue()))
                    .findFirst()
                    .map(CatastropheDTO::getID)
                    .orElse(0) : 0;

            route.setName(nameTextArea.getValue());
            route.setRouteType(routeTypeComboBox.getValue());
            route.setCatastrophe(catastropheID);
            route.setID(Integer.parseInt(routeID));
            mediator.publish(new GenericEvent<>(EventType.EDIT, route, null));
            c.setResultObject(route);
            c.endExecution();
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
        VerticalLayout dialogLayout = new VerticalLayout(title, requiredFieldsInfo, nameTextArea, routeTypeComboBox, catastropheComboBox, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
        icoClose.addClickListener(iev -> {
            mediator.publish(new GenericEvent<>(EventType.EXIT, null, c));
            dialog.close();
        });
    }

    private void editDialogStorage(String id, EditCommand c) {
        Storage storage = getStorageByID(id);
        c.setOriginalObject(storage);
        final Icon icoClose = VaadinIcon.CLOSE.create();
        final Dialog dialog = new Dialog(icoClose);
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setWidth("70vw");
        dialog.setHeight("40vh");
        H3 title = new H3("Editar almacen");

        //texto pequeño para indicar que los que contienen * son obligatorios
        Span requiredFieldsInfo = new Span("Los campos con · son obligatorios");
        requiredFieldsInfo.getStyle().set("color", "red");
        requiredFieldsInfo.getStyle().set("font-weight", "bold");
        requiredFieldsInfo.getStyle().set("font-size", "0.65em");

        TextArea nameTextArea = new TextArea("Nombre");
        nameTextArea.setPlaceholder("Introduce el nombre del almacen");
        nameTextArea.setRequiredIndicatorVisible(true);
        nameTextArea.setWidth("50vw");
        nameTextArea.setHeight("5vh");
        nameTextArea.setValue(storage.getName());

        ComboBox<String> zoneComboBox = new ComboBox<>("Zona");
        zoneComboBox.setRequiredIndicatorVisible(true);
        zoneComboBox.setItems(backendService.getZoneList().getValues().stream().map(ZoneDTO::getName).toList());
        zoneComboBox.setValue(
                backendService.getZoneList().stream().filter(x -> x.getID() == storage.getZoneID()).findFirst().get().getName()
        );

        ComboBox<String> llenoComboBox = new ComboBox<>("Estado");
        llenoComboBox.setRequiredIndicatorVisible(true);
        String[] llenoOptions = {"Lleno", "Vacio"};
        llenoComboBox.setItems(llenoOptions);
        llenoComboBox.setValue(storage.isFull() ? "Lleno" : "Vacio");

        Button cancelButton = new Button("Cancelar");
        cancelButton.addClickListener(event -> {
            mediator.publish(new GenericEvent<>(EventType.EXIT, null, c));
            dialog.close();
        });
        Button acceptButton = new Button("Aceptar", event -> {
            int zoneID = zoneComboBox.getValue() != null ? backendService.getZoneList().getValues().stream()
                    .filter(zoneDTO -> zoneDTO.getName().equals(zoneComboBox.getValue()))
                    .findFirst()
                    .map(ZoneDTO::getID)
                    .orElse(0) : 0;
            storage.setName(nameTextArea.getValue());
            storage.setZoneID(zoneID);
            storage.setFull(llenoComboBox.getValue().equals("Lleno"));
            storage.setID(Integer.parseInt(id));
            mediator.publish(new GenericEvent<>(EventType.EDIT, storage, null));
            c.setResultObject(storage);
            c.endExecution();
            dialog.close();
        });

        acceptButton.setEnabled(false);
        nameTextArea.addValueChangeListener(event -> {
            acceptButton.setEnabled(!nameTextArea.getValue().isEmpty() && zoneComboBox.getValue() != null && llenoComboBox.getValue() != null);
        });
        zoneComboBox.addValueChangeListener(event -> {
            acceptButton.setEnabled(!nameTextArea.getValue().isEmpty() && zoneComboBox.getValue() != null && llenoComboBox.getValue() != null);
        });
        llenoComboBox.addValueChangeListener(event -> {
            acceptButton.setEnabled(!nameTextArea.getValue().isEmpty() && zoneComboBox.getValue() != null && llenoComboBox.getValue() != null);
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, acceptButton);
        VerticalLayout dialogLayout = new VerticalLayout(title, requiredFieldsInfo, nameTextArea, zoneComboBox, llenoComboBox, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
        icoClose.addClickListener(iev -> {
            mediator.publish(new GenericEvent<>(EventType.EXIT, null, c));
            dialog.close();
        });
    }

    private Zone getZoneByID(String ID) {
        return map.getZones().stream()
                .filter(z -> z.getID() == Integer.parseInt(ID))
                .findFirst()
                .orElse(null);
    }

    private Route getRouteByID(String ID) {
        return map.getRoutes().stream()
                .filter(r -> r.getID() == Integer.parseInt(ID))
                .findFirst()
                .orElse(null);
    }

    private Storage getStorageByID(String id) {
        return map.getStorages().stream()
                .filter(s -> s.getID() == Integer.parseInt(id))
                .findFirst()
                .orElse(null);
    }

    public static void editStateOfMap(String newStatusText) {
        UI.getCurrent().access(() -> {
            Optional<Component> bannerComponent = VaadinUtils.findComponentById(UI.getCurrent(), "action-banner");

            if (bannerComponent.isPresent() && bannerComponent.get() instanceof Div) {
                Div foundBanner = (Div) bannerComponent.get();
                foundBanner.setText("Modo: " + newStatusText);
            } else {
                log.warn("Static editStateOfMap: Action banner with ID '{}' not found or not a Div.", "action-banner");
            }
        });
    }
}

