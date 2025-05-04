package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.RouteDTO;
import org.pingu.domain.DTO.ZoneDTO;
import org.pinguweb.frontend.mapObjects.*;

import java.security.Provider;
import java.util.ArrayList;

@Slf4j
@Setter
@Getter
public class MapBuild {

    MapService service;

    private String clickFuncReferenceCreateZone;
    private String clickFuncReferenceCreateRoute;

    public MapBuild(MapService service) {
        this.service = service;
        clickFuncReferenceCreateZone = service.getMap().clientComponentJsAccessor() + ".myClickFuncCreateZone";
        clickFuncReferenceCreateRoute = service.getMap().clientComponentJsAccessor() + ".myClickFuncCreateRoute";
    }

    public void startZoneConstruction(ZoneDTO zoneDTO) {
        service.setTempZoneDTO(zoneDTO);
        log.debug("Registrando puntos para la zona");
        service.getReg().execJs(clickFuncReferenceCreateZone + "=e => document.getElementById('" + service.getID() + "').$server.mapZona(e.latlng)");
        service.getMap().on("click", clickFuncReferenceCreateZone);
    }

    public void endZoneConstruction() {
        service.getMap().off("click", clickFuncReferenceCreateZone);
        log.debug("Zona terminada");
        Zone zona = this.service.createZone(service.getTempZoneDTO());

        int newID = zona.pushToServer();
        service.getZones().stream().filter(z -> z.getID() == service.getTempZoneDTO().getID()).findFirst().ifPresent(z -> {
            z.setID(newID);
        });

        for (ZoneMarker zoneMarker : service.getZoneMarkers().values()) {
            zoneMarker.removeFromMap(service.getMap());
        }

        service.getZoneMarkers().clear();
        service.getZoneMarkerPoints().clear();
    }

    public void startRouteConstruction(RouteDTO routeDTO) {
        service.setTempRouteDTO(routeDTO);
        log.debug("Registrando puntos para la ruta");
        service.getReg().execJs(clickFuncReferenceCreateRoute + "=e => document.getElementById('" + service.getID() + "').$server.mapRoute(e.latlng)");
        service.getMap().on("click", clickFuncReferenceCreateRoute);
    }

    public void endRouteConstruction() {
        service.getMap().off("click", clickFuncReferenceCreateRoute);
        log.debug("Ruta terminada");
        Route ruta = this.service.createRoute(service.getTempRouteDTO(), service.getRoutePoint());
        ruta.pushToServer();

        for (int i = service.getRoutePoints().size() - 2; i > 0; i--) {
            RoutePoint routePoint = service.getRoutePoint().get(i);
            routePoint.removeFromMap(service.getMap());
            service.getRoutePoints().remove(routePoint.getID());
        }

        service.getRoutePoints().put(service.getTempRouteDTO().getID(), new ArrayList<>(service.getRoutePoint()));

        for (RoutePoint routePoint : service.getRoutePoint()) {
            routePoint.removeFromMap(service.getMap());
        }
    }

    public void startEdit() {
        for (Need need : this.service.getNeeds()) {
            String clickFuncReferenceEditMarker = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncEditMarker" + need.getID();
            this.service.getReg().execJs(clickFuncReferenceEditMarker + "=e => document.getElementById('" + this.service.getID() + "').$server.editMarker('" + need.getID() + "')");
            need.getMarkerObj().on("click", clickFuncReferenceEditMarker);
        }
        for (Zone zone : this.service.getZones()) {
            String clickFuncReferenceEditZone = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncEditZone" + zone.getID();
            this.service.getReg().execJs(clickFuncReferenceEditZone + "=e => document.getElementById('" + this.service.getID() + "').$server.editPolygon('" + zone.getID() + "') ");
            zone.getPolygon().on("click", clickFuncReferenceEditZone);
        }
        for (org.pinguweb.frontend.mapObjects.Route route : this.service.getRoutes()) {
            String clickFuncReferenceEditRoute = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncEditRoute" + route.getID();
            this.service.getReg().execJs(clickFuncReferenceEditRoute + "=e => document.getElementById('" + this.service.getID() + "').$server.editRoute('" + route.getID() + "') ");
            route.getPolygon().on("click", clickFuncReferenceEditRoute);
        }
    }

    public void endEdit() {
        for (Need need : this.service.getNeeds()) {
            String clickFuncReferenceEditMarker = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncEditMarker" + need.getID();
            need.getMarkerObj().off("click", clickFuncReferenceEditMarker);
        }
        for (Zone zone : this.service.getZones()) {
            String clickFuncReferenceEditZone = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncEditZone" + zone.getID();
            zone.getPolygon().off("click", clickFuncReferenceEditZone);
        }
        for (org.pinguweb.frontend.mapObjects.Route route : this.service.getRoutes()) {
            String clickFuncReferenceEditRoute = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncEditRoute" + route.getID();
            route.getPolygon().off("click", clickFuncReferenceEditRoute);
        }
    }

    public void startDelete() {
        for (Need need : this.service.getNeeds()) {
            String clickFuncReferenceDeleteMarker = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteMarker" + need.getID();
            this.service.getReg().execJs(clickFuncReferenceDeleteMarker + "=e => document.getElementById('" + this.service.getID() + "').$server.removeMarker('" + need.getID() + "')");
            need.getMarkerObj().on("click", clickFuncReferenceDeleteMarker);
        }
        for (Zone zone : this.service.getZones()) {
            String clickFuncReferenceDeleteZone = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteZone" + zone.getID();
            this.service.getReg().execJs(clickFuncReferenceDeleteZone + "=e => document.getElementById('" + this.service.getID() + "').$server.removePolygon('" + zone.getID() + "') ");
            zone.getPolygon().on("click", clickFuncReferenceDeleteZone);
        }
        for (org.pinguweb.frontend.mapObjects.Route route : this.service.getRoutes()) {
            String clickFuncReferenceDeleteRoute = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteRoute" + route.getID();
            this.service.getReg().execJs(clickFuncReferenceDeleteRoute + "=e => document.getElementById('" + this.service.getID() + "').$server.removeRoute('" + route.getID() + "') ");
            route.getPolygon().on("click", clickFuncReferenceDeleteRoute);
        }
    }

    public void endDelete() {
        for (Need need : this.service.getNeeds()) {
            String clickFuncReferenceDeleteMarker = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteMarker" + need.getID();
            need.getMarkerObj().off("click", clickFuncReferenceDeleteMarker);
        }
        for (Zone zone : this.service.getZones()) {
            String clickFuncReferenceDeleteZone = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteZone" + zone.getID();
            zone.getPolygon().off("click", clickFuncReferenceDeleteZone);
        }
        for (org.pinguweb.frontend.mapObjects.Route route : this.service.getRoutes()) {
            String clickFuncReferenceDeleteRoute = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteRoute" + route.getID();
            route.getPolygon().off("click", clickFuncReferenceDeleteRoute);
        }
    }
}
