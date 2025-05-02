package org.pinguweb.frontend.view;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import lombok.Getter;
import org.pingu.domain.DTO.*;
import org.pingu.domain.enums.EmergencyLevel;
import org.pinguweb.frontend.mapObjects.Need;
import org.pinguweb.frontend.mapObjects.RoutePoint;
import org.pinguweb.frontend.mapObjects.Zone;
import org.pinguweb.frontend.mapObjects.ZoneMarker;
import org.pinguweb.frontend.services.map.MapService;
import org.yaml.snakeyaml.util.Tuple;
import software.xdev.vaadin.maps.leaflet.MapContainer;
import software.xdev.vaadin.maps.leaflet.controls.LControlLayers;
import software.xdev.vaadin.maps.leaflet.controls.LControlLayersOptions;
import software.xdev.vaadin.maps.leaflet.controls.LControlScale;
import software.xdev.vaadin.maps.leaflet.controls.LControlScaleOptions;
import software.xdev.vaadin.maps.leaflet.layer.LLayer;
import software.xdev.vaadin.maps.leaflet.layer.LLayerGroup;
import software.xdev.vaadin.maps.leaflet.layer.raster.LTileLayer;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.map.LMapLocateOptions;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;
import software.xdev.vaadin.maps.leaflet.registry.LDefaultComponentManagementRegistry;

import java.awt.*;
import java.util.*;
import java.util.List;

@Route("map")
@PageTitle("Visor del mapa")
public class MapView extends HorizontalLayout {

    MapService controller;
    private final String ID = "mapa";
    private final LMap map;
    private LComponentManagementRegistry reg;

    private LLayer layer;
    @Getter
    private static LLayerGroup lLayerGroupZones;
    @Getter
    private static LLayerGroup lLayerGroupNeeds;

    private MapContainer mapContainer;
    private final Object lock = new Object();
    private final Object lock2 = new Object();
    private UI ui;

    private final Button necesidad = new Button("Necesidad");
    private final Button zona = new Button("Zona");
    private final Button ruta = new Button("Ruta Segura");
    private final Button editar = new Button("Editar");
    private final Button borrar = new Button("Borrar");

    private HashMap<Tuple<Double, Double>, ZoneMarker> zoneMarkers = new HashMap<>();
    private List<Tuple<Double, Double>> zoneMarkerPoints = new ArrayList<>();
    private Tuple<Double, Double> zoneMarkerStartingPoint;

    //private List<Tuple<Double, Double>> routePointPoints = new ArrayList<>();
    private Tuple<Double, Double> routePointStartingPoint;
    private List<RoutePoint> routePoints = new ArrayList<>();

    private String clickFuncReferenceCreateZone;
    private String clickFuncReferenceCreateNeed;
    private String clickFuncReferenceCreateRoute;

    private NeedDTO tempNeedDTO;
    private ZoneDTO tempZoneDTO;
    private RouteDTO tempRouteDTO;

    public MapView() {
        this.setId(ID);
        this.setSizeFull();
        this.add(NavigationBar.createNavBar());

        VerticalLayout MapVerticalLayout = new VerticalLayout();
        HorizontalLayout ButtonLayout = new HorizontalLayout();

        this.add(MapVerticalLayout);

        reg = new LDefaultComponentManagementRegistry(this);
        mapContainer = new MapContainer(reg);
        mapContainer.setSizeFull();
        this.map = mapContainer.getlMap();
        layer = LTileLayer.createDefaultForOpenStreetMapTileServer(reg);
        this.map.addLayer(layer);

        lLayerGroupZones = new LLayerGroup(reg);
        lLayerGroupNeeds = new LLayerGroup(reg);
        this.addControls(lLayerGroupZones, lLayerGroupNeeds);

        this.map.addLayer(lLayerGroupZones).addLayer(lLayerGroupNeeds);

        this.map.on("overlayadd", "e => document.getElementById('" + ID + "').$server.overlay('activo')");
        this.map.on("overlayremove", "e => document.getElementById('" + ID + "').$server.overlay('inactivo')");

        clickFuncReferenceCreateZone = map.clientComponentJsAccessor() + ".myClickFuncCreateZone";
        clickFuncReferenceCreateNeed = map.clientComponentJsAccessor() + ".myClickFuncCreateNeed";
        clickFuncReferenceCreateRoute = map.clientComponentJsAccessor() + ".myClickFuncCreateRoute";


        this.map.locate(new LMapLocateOptions().withSetView(true));

        MapVerticalLayout.add(mapContainer);
        MapVerticalLayout.add(ButtonLayout);
        necesidad.isDisableOnClick();
        ButtonLayout.add(necesidad, zona, ruta, editar, borrar);

        this.controller = new MapService();
        this.controller.setReg(reg);
        this.controller.setMap(map);
        this.controller.setID(ID);

        if (this.controller.getZones().isEmpty()) {
            this.necesidad.setEnabled(false);
        }

        this.controller.load();

        necesidad.addClickListener(e -> crearDialogoTarea());
        zona.addClickListener(e -> createDialogZona());
        ruta.addClickListener(e -> createDialogRuta());
        borrar.addClickListener(e -> clickBorrar());
        editar.addClickListener(e -> editar());

    }

    public void clickNeed(NeedDTO needDTO) {
        this.mapContainer.addClassName("map_action");
        this.tempNeedDTO = needDTO;
        controller.setCreatingNeed(true);
        reg.execJs(clickFuncReferenceCreateNeed + "=e => document.getElementById('" + ID + "').$server.mapNeed(e.latlng)");
        this.map.on("click", clickFuncReferenceCreateNeed);
        ui = UI.getCurrent();

        new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("Esperando clic en el mapa...");
                    lock.wait();
                } catch (InterruptedException e) {
                    System.out.println("Error esperando clic en el mapa" + e);
                    return;
                }
            }
            if (ui != null) {
                ui.access(() -> this.mapContainer.removeClassName("map_action"));
            }
        }).start();
    }

    public void startZoneConstruction(ZoneDTO zoneDTO) {
        this.mapContainer.addClassName("map_action");
        this.tempZoneDTO = zoneDTO;
        System.out.println("Registrando puntos para la zona");
        reg.execJs(clickFuncReferenceCreateZone + "=e => document.getElementById('" + ID + "').$server.mapZona(e.latlng)");
        map.on("click", clickFuncReferenceCreateZone);
        this.zona.setEnabled(false);
        this.zona.setText("Terminar zona");
        this.controller.setZoneBool(true);
    }

    public void endZoneConstruction() {
        this.map.off("click", clickFuncReferenceCreateZone);
        System.out.println("Zona terminada");
        this.zona.setText("Zona");
        this.zona.setEnabled(true);
        Zone zona = this.controller.createZone(this.tempZoneDTO);
        zona.pushToServer();

        this.controller.setZoneBool(false);
        this.mapContainer.removeClassName("map_action");

        for (ZoneMarker zoneMarker : zoneMarkers.values()) {
            zoneMarker.removeFromMap(this.map);
        }

        zoneMarkers.clear();
        zoneMarkerPoints.clear();
        necesidad.setEnabled(true);
    }

    public void startRouteConstruction(RouteDTO routeDTO) {
        this.mapContainer.addClassName("map_action");
        this.tempRouteDTO = routeDTO;
        System.out.println("Registrando puntos para la ruta");
        reg.execJs(clickFuncReferenceCreateRoute + "=e => document.getElementById('" + ID + "').$server.mapRoute(e.latlng)");
        map.on("click", clickFuncReferenceCreateRoute);
        this.ruta.setEnabled(false);
        this.ruta.setText("Terminar ruta");
        this.controller.setCreatingRoute(true);
    }

    public void endRouteConstruction() {
        this.map.off("click", clickFuncReferenceCreateRoute);
        System.out.println("Ruta terminada");
        this.ruta.setText("Ruta");
        this.ruta.setEnabled(true);
        this.controller.createRoute(this.tempRouteDTO, routePoints);
        this.controller.setCreatingRoute(false);
        this.mapContainer.removeClassName("map_action");

        for (int i = routePoints.size() - 2; i > 0; i--) {
            RoutePoint routePoint = routePoints.get(i);
            routePoint.removeFromMap(this.map);
            routePoints.remove(routePoint);
        }

        controller.getRoutePoints().put(tempRouteDTO.getID(), new ArrayList<>(routePoints));

        routePoints.clear();

    }

    public void clickBorrar() {
        if (!this.controller.isDeleteBool()) {
            this.controller.setDeleteBool(true);
            this.borrar.setText("Detener borrado");
            this.mapContainer.removeClassName("map_action");
            for (Need need : this.controller.getNeeds()) {
                System.out.println("Registrando marcadores para borrar");
                String clickFuncReferenceDeleteMarker = map.clientComponentJsAccessor() + ".myClickFuncDeleteMarker" + need.getID();
                reg.execJs(clickFuncReferenceDeleteMarker + "=e => document.getElementById('" + ID + "').$server.removeMarker('" + need.getID() + "')");
                need.getMarkerObj().on("click", clickFuncReferenceDeleteMarker);
            }
            for (Zone zone : this.controller.getZones()) {
                System.out.println("Registrando zonas para borrar");
                String clickFuncReferenceDeleteZone = map.clientComponentJsAccessor() + ".myClickFuncDeleteZone" + zone.getID();
                reg.execJs(clickFuncReferenceDeleteZone + "=e => document.getElementById('" + ID + "').$server.removePolygon('" + zone.getID() + "') ");
                zone.getPolygon().on("click", clickFuncReferenceDeleteZone);
            }
            for (org.pinguweb.frontend.mapObjects.Route route : this.controller.getRoutes()) {
                System.out.println("Registrando rutas para borrar");
                String clickFuncReferenceDeleteRoute = map.clientComponentJsAccessor() + ".myClickFuncDeleteRoute" + route.getID();
                reg.execJs(clickFuncReferenceDeleteRoute + "=e => document.getElementById('" + ID + "').$server.removeRoute('" + route.getID() + "') ");
                if (route.getPolygon() != null) {
                    route.getPolygon().addTo(map); // Asegurarse de que el polígono está en el mapa
                    route.getPolygon().on("click", clickFuncReferenceDeleteRoute);
                    System.out.println("Evento de clic registrado para la ruta con ID: " + route.getID());
                } else {
                    System.out.println("El polígono de la ruta con ID " + route.getID() + " no está definido.");
                }
            }
        } else {
            this.controller.setDeleteBool(false);
            this.borrar.setText("Borrar");
            for (Need need : this.controller.getNeeds()) {
                String clickFuncReferenceDeleteMarker = map.clientComponentJsAccessor() + ".myClickFuncDeleteMarker" + need.getID();
                need.getMarkerObj().off("click", clickFuncReferenceDeleteMarker);
            }
            for (Zone zone : this.controller.getZones()) {
                String clickFuncReferenceDeleteZone = map.clientComponentJsAccessor() + ".myClickFuncDeleteZone" + zone.getID();
                zone.getPolygon().off("click", clickFuncReferenceDeleteZone);
            }
            for (org.pinguweb.frontend.mapObjects.Route route : this.controller.getRoutes()) {
                String clickFuncReferenceDeleteRoute = map.clientComponentJsAccessor() + ".myClickFuncDeleteRoute" + route.getID();
                route.getPolygon().off("click", clickFuncReferenceDeleteRoute);
            }

            this.mapContainer.removeClassName("map_action");
        }
    }


    public void editar() {
        if (!controller.isEditing()) {
            this.controller.setEditing(true);
            this.editar.setText("Detener edicion");
        } else {
            this.controller.setEditing(false);
            this.editar.setText("Editar");
        }
    }


    private void addControls(
            final LLayerGroup lLayerGroupZones,
            final LLayerGroup lLayerGroupNeeds
    ) {
        // Use LinkedHashMap for order
        final LinkedHashMap<String, LLayer<?>> baseLayers = new LinkedHashMap<>();
        final LControlLayers lControlLayers = new LControlLayers(
                this.reg,
                baseLayers,
                new LControlLayersOptions().withCollapsed(false))
                .addOverlay(lLayerGroupNeeds, "Necesidades")
                .addOverlay(lLayerGroupZones, "Zonas")
                .addTo(this.map);

        this.map.on(
                "resize",
                lControlLayers.clientComponentJsAccessor() + "._expandIfNotCollapsed",
                lControlLayers.clientComponentJsAccessor());

        new LControlScale(this.reg, new LControlScaleOptions()).addTo(this.map);
    }

    @ClientCallable
    public void mapNeed(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        if (controller.getPointInZone()) {
            this.tempNeedDTO.setLatitude(obj.getNumber("lat"));
            this.tempNeedDTO.setLongitude(obj.getNumber("lng"));
            Need need = controller.createNeed(this.tempNeedDTO);
            need.pushToServer();
            synchronized (lock) {
                lock.notify();
            }
            this.map.off("click", clickFuncReferenceCreateNeed);
            ui.push();
            controller.setPointInZone(false);
            controller.setCreatingNeed(false);
        } else {
            Notification.show("Debes crear las necesidades dentro de una zona", 3000, Notification.Position.TOP_CENTER);
        }
    }

    @ClientCallable
    public void mapZona(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        ZoneMarker zoneMarker = this.controller.createZoneMarker(obj.getNumber("lat"), obj.getNumber("lng"));
        tempZoneDTO.getLatitudes().add(obj.getNumber("lat"));
        tempZoneDTO.getLongitudes().add(obj.getNumber("lng"));

        zoneMarkerPoints.add(new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng")));
        zoneMarkers.put(new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng")), zoneMarker);

        if (zoneMarkerPoints.size() > 2) {
            zona.setEnabled(true);
        }
    }


    //AQUI
    @ClientCallable
    public void mapRoute(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }
        //Cambiar para que se genere un ID
        RoutePoint routePoint = this.controller.createRoutePoint(obj.getNumber("lat"), obj.getNumber("lng"));
        routePoint.setID(controller.getTempIdRoutePoint());
        controller.setTempIdRoutePoint(controller.getTempIdRoutePoint() + 1);

        tempRouteDTO.getPoints().add(routePoint.getID());

        routePoints.add(routePoint);


        if (routePoints.size() > 1) {
            ruta.setEnabled(true);
        }


    }

    @ClientCallable
    public void zoneMarkerStart(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        this.zoneMarkerStartingPoint = new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng"));
    }

    @ClientCallable
    public void zoneMarkerEnd(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        Tuple<Double, Double> point = new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng"));

        int index = -1;
        for (int i = 0; i < zoneMarkerPoints.size(); i++) {
            Tuple<Double, Double> t = zoneMarkerPoints.get(i);

            if (t._1().equals(this.zoneMarkerStartingPoint._1()) && t._2().equals(this.zoneMarkerStartingPoint._2())) {
                index = i;
                break;
            }
        }

        System.out.println(this.zoneMarkerStartingPoint);
        zoneMarkerPoints.set(index, point);
    }

    @ClientCallable
    public void routePointStart(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        this.routePointStartingPoint = new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng"));
    }

    @ClientCallable
    public void routePointEnd(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        Tuple<Double, Double> point = new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng"));

        int index = -1;
        for (int i = 0; i < routePoints.size(); i++) {
            RoutePoint t = routePoints.get(i);

            if (Objects.equals(t.getLatitude(), this.routePointStartingPoint._1()) && Objects.equals(t.getLongitude(), this.routePointStartingPoint._2())) {
                index = i;
                break;
            }
        }

        System.out.println(this.routePointStartingPoint);
        RoutePoint t = routePoints.get(index);
        t.setLatitude(point._1());
        t.setLongitude(point._2());
        routePoints.set(index, t);
    }


    @ClientCallable
    public void clickOnZone(final JsonValue input, String ID) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }
        if (controller.isCreatingNeed()) {
            controller.setPointInZone(true);
        } else if (controller.isEditing()) {

        } else {
            System.out.println("No se esta ni creando ni editando la zona " + ID);
        }
    }

    @ClientCallable
    public void clickOnNeed(final JsonValue input, String ID) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }
        if (controller.isEditing()) {

        } else {
            System.out.println("No se esta editando la necesidad " + ID);
        }

    }


    @ClientCallable
    public void removeMarker(String ID) {
        System.out.println("removeMarker: " + ID);
        this.controller.deleteNeed(Integer.parseInt(ID));
    }

    @ClientCallable
    public void removePolygon(String ID) {
        System.out.println("removePolygon: " + ID);
        this.controller.deleteZone(Integer.parseInt(ID));
    }

    @ClientCallable
    public void removeRoute(String ID) {
        System.out.println("removeRoute: " + ID);
        this.controller.deleteRoute(Integer.parseInt(ID));
    }

    @ClientCallable
    public void overlay(String status) {
        System.out.println("overlay: " + status);
    }

    public void createDialogZona() {
        if (!this.controller.isZoneBool()) {
            List<CatastropheDTO> catastropheDTOList = this.controller.getCatastrophes();
            List<StorageDTO> storageDTOList = this.controller.getStorages();
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
                this.startZoneConstruction(zoneDTO);
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
            this.endZoneConstruction();
        }
    }

    public void crearDialogoTarea() {
        final Icon icoClose = VaadinIcon.CLOSE.create();
        final Dialog dialog = new Dialog(icoClose);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setWidth("70vw");
        dialog.setHeight("70vh");

        H3 title = new H3("Crear tarea");

        ComboBox<String> typesComboBox = new ComboBox<>("Tipo");
        typesComboBox.setItems("Mantenimiento", "Reparación", "Limpieza");

        ComboBox<String> severityComboBox = new ComboBox<>("Urgencia");
        severityComboBox.setItems("Baja", "Media", "Alta");

        TextArea descriptionTextArea = new TextArea();
        descriptionTextArea.setPlaceholder("descripcion");
        descriptionTextArea.setWidthFull();
        descriptionTextArea.setHeight("50vh");

        NeedDTO needDTO = new NeedDTO();
        //TODO: Mirar cambiar como se asigna el ID
        needDTO.setID(controller.getTempIdMarker());
        controller.setTempIdMarker(controller.getTempIdMarker() + 1);
        needDTO.setDescription(descriptionTextArea.getValue());
        needDTO.setNeedType(typesComboBox.getValue());
        needDTO.setAffected("zona");
        needDTO.setLatitude(0.0);
        needDTO.setLongitude(0.0);
        needDTO.setTask(0);
        needDTO.setCatastrophe(0);
        needDTO.setUrgency(severityComboBox.getValue());

        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        Button acceptButton = new Button("Aceptar", event -> {
            this.clickNeed(needDTO);
            dialog.close();
        });

        acceptButton.setEnabled(false);

        typesComboBox.addValueChangeListener(event -> {
            acceptButton.setEnabled(!descriptionTextArea.getValue().isEmpty() && severityComboBox.getValue() != null && typesComboBox.getValue() != null);
        });

        descriptionTextArea.addValueChangeListener(event -> {
            acceptButton.setEnabled(!descriptionTextArea.getValue().isEmpty() && severityComboBox.getValue() != null && typesComboBox.getValue() != null);
        });

        severityComboBox.addValueChangeListener(event -> {
            acceptButton.setEnabled(!descriptionTextArea.getValue().isEmpty() && severityComboBox.getValue() != null && typesComboBox.getValue() != null);
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, acceptButton);

        VerticalLayout dialogLayout = new VerticalLayout(title, typesComboBox, severityComboBox, descriptionTextArea, buttonLayout);
        dialog.add(dialogLayout);

        dialog.open();

        icoClose.addClickListener(iev -> dialog.close());
    }

    public void createDialogRuta() {
        if (!this.controller.isCreatingRoute()) {
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
                startRouteConstruction(routeDTO);
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
            endRouteConstruction();
        }
    }


}