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
import org.pinguweb.frontend.services.map.MapService;
import org.pinguweb.frontend.services.map.MapTypes;
import org.yaml.snakeyaml.util.Tuple;
import software.xdev.vaadin.maps.leaflet.MapContainer;
import software.xdev.vaadin.maps.leaflet.layer.raster.LTileLayer;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
import software.xdev.vaadin.maps.leaflet.controls.LControlLayers;
import software.xdev.vaadin.maps.leaflet.controls.LControlLayersOptions;
import software.xdev.vaadin.maps.leaflet.controls.LControlScale;
import software.xdev.vaadin.maps.leaflet.controls.LControlScaleOptions;
import software.xdev.vaadin.maps.leaflet.layer.LLayer;
import software.xdev.vaadin.maps.leaflet.layer.LLayerGroup;
import software.xdev.vaadin.maps.leaflet.layer.raster.LTileLayer;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
import software.xdev.vaadin.maps.leaflet.layer.vector.LPolygon;
import software.xdev.vaadin.maps.leaflet.map.LMapLocateOptions;
import software.xdev.vaadin.maps.leaflet.MapContainer;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.map.LMapLocateOptions;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;
import software.xdev.vaadin.maps.leaflet.registry.LDefaultComponentManagementRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static org.pinguweb.frontend.services.map.MapTypes.TAREA;
import static org.pinguweb.frontend.services.map.MapTypes.ZONA;

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
    private UI ui;

    private final Button necesidad = new Button("Necesidad");
    private final Button zona = new Button("Zona");
    private final Button borrar = new Button("Borrar");

    private HashMap<Tuple<Double, Double>, LMarker> zoneMarkers = new HashMap<>();
    private List<Tuple<Double, Double>> zoneMarkerPoints = new ArrayList<>();
    private Tuple<Double, Double> zoneMarkerStartingPoint;
    private String clickFuncReferenceCreateZone;
    private String clickFuncReferenceDeleteZone;
    private String clickFuncReferenceDeleteMarker;

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


        clickFuncReferenceCreateZone = map.clientComponentJsAccessor() + ".myCoolClickFunc";
        clickFuncReferenceDeleteZone = map.clientComponentJsAccessor() + ".myCoolClickFuncDeleteZone";
        reg.execJs(clickFuncReferenceDeleteZone + "=e => document.getElementById('" + ID + "').$server.removePolygon(e.target._leaflet_id) && e.target.remove()");
        clickFuncReferenceDeleteMarker = map.clientComponentJsAccessor() + ".myCoolClickFuncDeleteMarker";
        reg.execJs(clickFuncReferenceDeleteMarker + "=e => document.getElementById('" + ID + "').$server.removeMarker(e.target._leaflet_id) && e.target.remove()");

        this.map.locate(new LMapLocateOptions().withSetView(true));

        MapVerticalLayout.add(mapContainer);
        MapVerticalLayout.add(ButtonLayout);
        necesidad.isDisableOnClick();
        ButtonLayout.add(necesidad, zona, borrar);

        this.controller = new MapService();
        this.controller.setReg(reg);
        this.controller.setMap(map);
        this.controller.setID(ID);

        necesidad.addClickListener(e -> crearDialogoTarea(controller));
        zona.addClickListener(e -> createDialogZona(controller));
        borrar.addClickListener(e -> clickBorrar(borrar));

    }



    public void click(MapTypes Action, Button button) {
        switch (Action) {
            case TAREA:
                this.mapContainer.addClassName("map_action");
                this.map.once("click", "e => document.getElementById('" + ID + "').$server.mapTarea(e.latlng)");

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

                break;
            case ZONA:
                this.mapContainer.addClassName("map_action");
                if (!this.controller.isZoneBool()) {
                    System.out.println("Registrando puntos para la zona");
                    reg.execJs(clickFuncReferenceCreateZone + "=e => document.getElementById('" + ID + "').$server.mapZona(e.latlng)");
                    map.on("click", clickFuncReferenceCreateZone);
                    button.setEnabled(false);
                    button.setText("Terminar zona");
                    this.controller.setZoneBool(true);
                } else {
                    this.map.off("click", clickFuncReferenceCreateZone);
                    System.out.println("Zona terminada");
                    button.setText("Zona");
                    button.setEnabled(true);
                    this.controller.createZone(this.zoneMarkerPoints);
                    this.controller.setZoneBool(false);
                    this.mapContainer.removeClassName("map_action");

                    for (LMarker marker : zoneMarkers.values()) {
                        marker.removeFrom(this.map);
                    }

                    zoneMarkers.clear();
                    zoneMarkerPoints.clear();
                }
                break;
        }
    }

    public void clickBorrar(Button button) {
        if (!this.controller.isDeleteBool()) {
            this.controller.setDeleteBool(true);
            button.setText("Detener borrado");
            this.mapContainer.removeClassName("map_action");
            for (LMarker marker : this.controller.getMarkers()) {
                System.out.println("Registrando marcadores para borrar");
                marker.on("click", clickFuncReferenceDeleteMarker);
            }
            for (LPolygon polygon : this.controller.getPolygons()) {
                System.out.println("Registrando zonas para borrar");
                polygon.on("click", clickFuncReferenceDeleteZone);
            }
        } else {
            this.controller.setDeleteBool(false);
            button.setText("Borrar");
            for (LMarker marker : this.controller.getMarkers()) {
                marker.off("click", clickFuncReferenceDeleteMarker);
            }
            for (LPolygon polygon : this.controller.getPolygons()) {
                polygon.off("click", clickFuncReferenceDeleteZone);
            }

            this.mapContainer.removeClassName("map_action");
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
    public void mapTarea(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        controller.createNeed(obj.getNumber("lat"), obj.getNumber("lng"));

        synchronized (lock) {
            lock.notify();
        }
        ui.push();
    }

    @ClientCallable
    public void mapZona(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        LMarker marker = this.controller.createZoneMarker(obj.getNumber("lat"), obj.getNumber("lng"));

        zoneMarkerPoints.add(new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng")));
        zoneMarkers.put(new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng")), marker);

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
    public void clickOnZone(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }
        System.out.println("clickOnZone: " + obj.getNumber("lat") + ", " + obj.getNumber("lng"));
    }

    @ClientCallable
    public void removeMarker(String id) {
        System.out.println("removeMarker: " + id);
    }

    @ClientCallable
    public void removePolygon(String id) {
        System.out.println("removePolygon: " + id);
    }

    @ClientCallable
    public void overlay(String status) {
        System.out.println("overlay: " + status);
    }

    public void createDialogZona(MapService controller) {
        final Icon icoClose = VaadinIcon.CLOSE.create();
        final Dialog dialog = new Dialog(icoClose);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setWidth("70vw");
        dialog.setHeight("70vh");

        H3 title = new H3("Crear zona");

        ComboBox<String> severityComboBox = new ComboBox<>("Gravedad");
        severityComboBox.setItems("Baja", "Media", "Alta");

        TextArea descriptionTextArea = new TextArea();
        descriptionTextArea.setPlaceholder("descripcion");
        descriptionTextArea.setWidthFull();
        descriptionTextArea.setHeight("50vh");

        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        Button acceptButton = new Button("Aceptar", event -> {controller.setZone(descriptionTextArea.getValue(), "zona", severityComboBox.getValue());
            dialog.close();});

        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, acceptButton);

        VerticalLayout dialogLayout = new VerticalLayout(title, severityComboBox, descriptionTextArea, buttonLayout);
        dialog.add(dialogLayout);

        dialog.open();

        icoClose.addClickListener(iev -> dialog.close());
    }

    public void crearDialogoTarea(MapService controller) {
        final Icon icoClose = VaadinIcon.CLOSE.create();
        final Dialog dialog = new Dialog(icoClose);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setWidth("70vw");
        dialog.setHeight("70vh");

        H3 title = new H3("Crear tarea");

        ComboBox<String> typesComboBox = new ComboBox<>("Tipo");
        typesComboBox.setItems("Mantenimiento", "Reparación", "Limpieza");

        ComboBox<String> severityComboBox = new ComboBox<>("Tipo");
        severityComboBox.setItems("Mantenimiento", "Reparación", "Limpieza");

        TextArea descriptionTextArea = new TextArea();
        descriptionTextArea.setPlaceholder("descripcion");
        descriptionTextArea.setWidthFull();
        descriptionTextArea.setHeight("50vh");

        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        Button acceptButton = new Button("Aceptar", event -> {
            controller.setNeed(descriptionTextArea.getValue(), typesComboBox.getValue(), "zona", severityComboBox.getValue(), 0, 0);
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, acceptButton);

        VerticalLayout dialogLayout = new VerticalLayout(title, severityComboBox, descriptionTextArea, buttonLayout);
        dialog.add(dialogLayout);

        dialog.open();

        icoClose.addClickListener(iev -> dialog.close());
    }



}