package org.pinguweb.frontend.view;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import lombok.Getter;
import lombok.Setter;
import org.pinguweb.frontend.mapObjects.RoutePoint;
import org.pinguweb.frontend.mapObjects.ZoneMarker;
import org.pinguweb.frontend.services.map.MapButtons;
import org.pinguweb.frontend.services.map.MapService;
import org.yaml.snakeyaml.util.Tuple;
import software.xdev.vaadin.maps.leaflet.MapContainer;
import software.xdev.vaadin.maps.leaflet.layer.LLayer;
import software.xdev.vaadin.maps.leaflet.layer.raster.LTileLayer;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.map.LMapLocateOptions;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;
import software.xdev.vaadin.maps.leaflet.registry.LDefaultComponentManagementRegistry;

import java.util.Objects;

@Route("map")
@PageTitle("Visor del mapa")
@Setter
@Getter
public class MapView extends HorizontalLayout {

    private static final String ID = "map-view";

    private MapService controller;
    private MapButtons buttons;

    private LMap map;
    private LComponentManagementRegistry reg;
    private LLayer layer;
    private MapContainer mapContainer;

    VerticalLayout MapVerticalLayout = new VerticalLayout();
    HorizontalLayout ButtonLayout = new HorizontalLayout();

    public MapView() {
        this.setId(ID);
        this.setSizeFull();
        this.add(NavigationBar.createNavBar());

        this.add(MapVerticalLayout);

        reg = new LDefaultComponentManagementRegistry(this);
        mapContainer = new MapContainer(reg);
        mapContainer.setSizeFull();
        this.map = mapContainer.getlMap();
        layer = LTileLayer.createDefaultForOpenStreetMapTileServer(reg);
        this.map.addLayer(layer);

        this.map.locate(new LMapLocateOptions().withSetView(true).withMaxZoom(16));

        this.controller = new MapService();
        this.controller.setReg(reg);
        this.controller.setMap(map);
        this.controller.setID(ID);

        buttons = new MapButtons(ButtonLayout, this.controller);

        MapVerticalLayout.add(mapContainer);
        MapVerticalLayout.add(ButtonLayout);

        this.controller.load();
    }

    @ClientCallable
    public void mapZona(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        ZoneMarker zoneMarker = this.controller.createZoneMarker(obj.getNumber("lat"), obj.getNumber("lng"));
        controller.getTempZoneDTO().getLatitudes().add(obj.getNumber("lat"));
        controller.getTempZoneDTO().getLongitudes().add(obj.getNumber("lng"));

        controller.getZoneMarkerPoints().add(new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng")));
        controller.getZoneMarkers().put(new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng")), zoneMarker);

        /*if (zoneMarkerPoints.size() > 2) {
            zona.setEnabled(true);
        }*/
    }

    @ClientCallable
    public void mapRoute(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }
        //Cambiar para que se genere un ID
        RoutePoint routePoint = this.controller.createRoutePoint(obj.getNumber("lat"), obj.getNumber("lng"));
        routePoint.setID(controller.getTempIdRoutePoint());
        controller.setTempIdRoutePoint(controller.getTempIdRoutePoint() + 1);

        controller.getTempRouteDTO().getPoints().add(routePoint.getID());

        controller.getRoutePoint().add(routePoint);


        /*if (routePoints.size() > 1) {
            ruta.setEnabled(true);
        }*/


    }

    @ClientCallable
    public void zoneMarkerStart(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        controller.setZoneMarkerStartingPoint(new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng")));
    }

    @ClientCallable
    public void zoneMarkerEnd(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        Tuple<Double, Double> point = new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng"));

        int index = -1;
        for (int i = 0; i < controller.getZoneMarkerPoints().size(); i++) {
            Tuple<Double, Double> t = controller.getZoneMarkerPoints().get(i);

            if (t._1().equals(controller.getZoneMarkerStartingPoint()._1()) && t._2().equals(controller.getZoneMarkerStartingPoint()._2())) {
                index = i;
                break;
            }
        }

        System.out.println(controller.getZoneMarkerStartingPoint());
        controller.getZoneMarkerPoints().set(index, point);
    }

    /*@ClientCallable
    public void routePointStart(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        controller.setrothis.routePointStartingPoint = new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng"));
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
    }*/



}