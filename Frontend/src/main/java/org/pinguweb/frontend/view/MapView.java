package org.pinguweb.frontend.view;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import lombok.Getter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapDialogs;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapService;
import org.pinguweb.frontend.interfaceBuilders.Directors.MapBuilderDirector;
import org.pinguweb.frontend.mapObjects.RoutePoint;
import org.pinguweb.frontend.mapObjects.ZoneMarker;
import org.yaml.snakeyaml.util.Tuple;

import java.util.Objects;

@Route("map")
@PageTitle("Visor del mapa")
public class MapView extends HorizontalLayout {

    @Getter
    private static String mapId = "MapView";
    private static MapService controller;
    private static MapDialogs mapDialogs;

    public MapView() {
        this.setSizeFull();
        this.setId(mapId);
        MapBuilderDirector director = new MapBuilderDirector();
        this.add(NavigationBar.createNavBar());
        this.add(director.createFullMap());
    }

    public static void setMapService(MapService controller) {
        MapView.controller = controller;
    }

    public static void setMapDialogs(MapDialogs mapDialogs) {
        MapView.mapDialogs = mapDialogs;
    }

    @ClientCallable
    public void mapZona(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        ZoneMarker zoneMarker = controller.createZoneMarker(obj.getNumber("lat"), obj.getNumber("lng"));
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
        RoutePoint routePoint = controller.createRoutePoint(obj.getNumber("lat"), obj.getNumber("lng"));
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

        controller.getZoneMarkerPoints().set(index, point);
    }

    @ClientCallable
    public void routePointStart(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        controller.setRoutePointStartingPoint(new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng")));
    }

    @ClientCallable
    public void routePointEnd(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        Tuple<Double, Double> point = new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng"));

        int index = -1;
        for (int i = 0; i < controller.getRoutePoints().size(); i++) {
            RoutePoint t = controller.getRoutePoints().get(0).get(i);

            if (Objects.equals(t.getLatitude(), controller.getRoutePointStartingPoint()._1()) && Objects.equals(t.getLongitude(), controller.getRoutePointStartingPoint()._2())) {
                index = i;
                break;
            }
        }

        RoutePoint t = controller.getRoutePoint().get(index);
        t.setLatitude(point._1());
        t.setLongitude(point._2());
        controller.getRoutePoint().set(index, t);
    }

    @ClientCallable
    public void removeMarker(String ID) {
        System.out.println("removeMarker: " + ID);
        controller.deleteNeed(Integer.parseInt(ID));
    }

    @ClientCallable
    public void removePolygon(String ID) {
        System.out.println("removePolygon: " + ID);
        controller.deleteZone(Integer.parseInt(ID));
    }

    @ClientCallable
    public void removeRoute(String ID) {
        System.out.println("removeRoute: " + ID);
        controller.deleteRoute(Integer.parseInt(ID));
    }

    /*@ClientCallable
    public void editMarker(Need need) {
        System.out.println("editMarker: " + need.getID());
        controller.editNeed(need);
    }*/

    @ClientCallable
    public void editPolygon(String ID) {
        System.out.println("editPolygon: " + ID);
        mapDialogs.editDialogZone(ID);
    }

    @ClientCallable
    public void editRoute(String ID) {
        System.out.println("editRoute: " + ID);
        mapDialogs.editDialogRoute(ID);
    }


}