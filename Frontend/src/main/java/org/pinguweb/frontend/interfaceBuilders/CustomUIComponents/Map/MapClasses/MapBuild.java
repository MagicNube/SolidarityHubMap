package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.RouteDTO;
import org.pingu.domain.DTO.ZoneDTO;
import org.pinguweb.frontend.mapObjects.RoutePoint;
import org.pinguweb.frontend.mapObjects.Zone;
import org.pinguweb.frontend.mapObjects.ZoneMarker;

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
        this.service.setZoneBool(true);
    }

    public void endZoneConstruction() {
        service.getMap().off("click", clickFuncReferenceCreateZone);
        log.debug("Zona terminada");
        Zone zona = this.service.createZone(service.getTempZoneDTO());
        zona.pushToServer();

        this.service.setZoneBool(false);

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
        this.service.setCreatingRoute(true);
    }

    public void endRouteConstruction() {
        service.getMap().off("click", clickFuncReferenceCreateRoute);
        log.debug("Ruta terminada");
        this.service.createRoute(service.getTempRouteDTO(), service.getRoutePoint());
        this.service.setCreatingRoute(false);

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
}
