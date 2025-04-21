package org.pinguweb.frontend.view;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import lombok.Getter;
import org.pingu.domain.DTO.NeedDTO;
import org.pingu.domain.DTO.ZoneDTO;
import org.pinguweb.frontend.mapObjects.Need;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    private final Button borrar = new Button("Borrar");
    private final Button editar = new Button("Editar");

    private HashMap<Tuple<Double, Double>, ZoneMarker> zoneMarkers = new HashMap<>();
    private List<Tuple<Double, Double>> zoneMarkerPoints = new ArrayList<>();
    private Tuple<Double, Double> zoneMarkerStartingPoint;
    private String clickFuncReferenceCreateZone;

    private NeedDTO tempNeedDTO;
    private ZoneDTO tempZoneDTO;

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

        this.map.locate(new LMapLocateOptions().withSetView(true));

        MapVerticalLayout.add(mapContainer);
        MapVerticalLayout.add(ButtonLayout);
        necesidad.isDisableOnClick();
        ButtonLayout.add(necesidad, zona, borrar, editar);

        this.controller = new MapService();
        this.controller.setReg(reg);
        this.controller.setMap(map);
        this.controller.setID(ID);

        this.controller.load();

        necesidad.addClickListener(e -> crearDialogoTarea());
        zona.addClickListener(e -> createDialogZona());
        borrar.addClickListener(e -> clickBorrar());
        editar.addClickListener(e -> editar());

    }

    public void clickNeed(NeedDTO needDTO) {
        this.mapContainer.addClassName("map_action");
        this.tempNeedDTO = needDTO;
        controller.setCreatingNeed(true);
        this.map.once("click", "e => document.getElementById('" + ID + "').$server.mapNeed(e.latlng)");
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

    public void endZoneConstruction(){
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
                reg.execJs(clickFuncReferenceDeleteZone + "=e => document.getElementById('" + ID + "').$server.removePolygon('" + zone.getID()+ "') ");
                zone.getPolygon().on("click", clickFuncReferenceDeleteZone);
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

            this.mapContainer.removeClassName("map_action");
        }
    }


    public void editar() {
        if (!controller.isEditing()){
            this.controller.setEditing(true);
            this.editar.setText("Detener edicion");
        }else {
            this.controller.setEditing(false);
            this.editar.setText("Editar");
        }
    }














    private void addControls(
            final LLayerGroup lLayerGroupZones,
            final LLayerGroup lLayerGroupNeeds
    )
    {
        // Use LinkedHashMap for order
        final LinkedHashMap<String, LLayer<?>> baseLayers = new LinkedHashMap<>();
        final LControlLayers lControlLayers = new LControlLayers(
                this.reg,
                baseLayers,
                new LControlLayersOptions().withCollapsed(false))
                .addOverlay(lLayerGroupNeeds , "Necesidades")
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
            ui.push();
            controller.setPointInZone(false);
            controller.setCreatingNeed(false);
        } else {
            System.out.println("El clic no está dentro de ninguna zona.");
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
    public void clickOnZone(final JsonValue input, String ID) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }
        if (controller.isCreatingNeed()) {
            controller.setPointInZone(true);
        } else if (controller.isEditing()) {

        }else {
            System.out.println("No se esta ni creando ni editando la zona "+ ID);
        }
    }

    @ClientCallable
    public void clickOnNeed(final JsonValue input, String ID) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }
        if (controller.isEditing()){

        } else {
            System.out.println("No se esta editando la necesidad "+ID);
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
    public void overlay(String status) {
        System.out.println("overlay: " + status);
    }


















    public void createDialogZona() {
        if (!this.controller.isZoneBool()) {
            final Icon icoClose = VaadinIcon.CLOSE.create();
            final Dialog dialog = new Dialog(icoClose);
            dialog.setDraggable(true);
            dialog.setResizable(true);
            dialog.setWidth("70vw");
            dialog.setHeight("70vh");

            H3 title = new H3("Crear zona");

            TextArea nameTextArea = new TextArea();
            nameTextArea.setPlaceholder("nombre");
            nameTextArea.setWidth("50vw");
            nameTextArea.setHeight("3vh");

            ComboBox<String> severityComboBox = new ComboBox<>("Gravedad");
            severityComboBox.setItems("Baja", "Media", "Alta");

            TextArea descriptionTextArea = new TextArea();
            descriptionTextArea.setPlaceholder("descripcion");
            descriptionTextArea.setWidthFull();
            descriptionTextArea.setHeight("50vh");

            ZoneDTO zoneDTO = new ZoneDTO();
            //TODO: Mirar cambiar como se asigna el ID
            zoneDTO.setID(controller.getTempIdZone());
            controller.setTempIdZone(controller.getTempIdZone() + 1);
            zoneDTO.setDescription(descriptionTextArea.getValue());
            zoneDTO.setName(nameTextArea.getValue());
            //TODO: Mirar como funcionan las catastrofes
            zoneDTO.setCatastrophe(null);
            zoneDTO.setEmergencyLevel(severityComboBox.getValue());
            zoneDTO.setLatitudes(new ArrayList<>());
            zoneDTO.setLongitudes(new ArrayList<>());
            //TODO: Mirar como asignar storages
            zoneDTO.setStorages(new ArrayList<>());

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

            VerticalLayout dialogLayout = new VerticalLayout(title, nameTextArea, severityComboBox, descriptionTextArea, buttonLayout);
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
        Button acceptButton = new Button("Aceptar", event -> {this.clickNeed(needDTO);
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



}