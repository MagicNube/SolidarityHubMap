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
public class MapBuilder {

    MapService controller;

    private String clickFuncReferenceCreateZone;
    private String clickFuncReferenceCreateRoute;

    public MapBuilder(MapService controller) {
        this.controller = controller;
        clickFuncReferenceCreateZone = controller.getMap().clientComponentJsAccessor() + ".myClickFuncCreateZone";
        clickFuncReferenceCreateRoute = controller.getMap().clientComponentJsAccessor() + ".myClickFuncCreateRoute";
    }

    public void startZoneConstruction(ZoneDTO zoneDTO) {
        controller.setTempZoneDTO(zoneDTO);
        log.debug("Registrando puntos para la zona");
        controller.getReg().execJs(clickFuncReferenceCreateZone + "=e => document.getElementById('" + controller.getID() + "').$server.mapZona(e.latlng)");
        controller.getMap().on("click", clickFuncReferenceCreateZone);
        this.controller.setZoneBool(true);
    }

    public void endZoneConstruction() {
        controller.getMap().off("click", clickFuncReferenceCreateZone);
        log.debug("Zona terminada");
        Zone zona = this.controller.createZone(controller.getTempZoneDTO());
        zona.pushToServer();

        this.controller.setZoneBool(false);

        for (ZoneMarker zoneMarker : controller.getZoneMarkers().values()) {
            zoneMarker.removeFromMap(controller.getMap());
        }

        controller.getZoneMarkers().clear();
        controller.getZoneMarkerPoints().clear();
    }

    public void startRouteConstruction(RouteDTO routeDTO) {
        controller.setTempRouteDTO(routeDTO);
        log.debug("Registrando puntos para la ruta");
        controller.getReg().execJs(clickFuncReferenceCreateRoute + "=e => document.getElementById('" + controller.getID() + "').$server.mapRoute(e.latlng)");
        controller.getMap().on("click", clickFuncReferenceCreateRoute);
        this.controller.setCreatingRoute(true);
    }

    public void endRouteConstruction() {
        controller.getMap().off("click", clickFuncReferenceCreateRoute);
        log.debug("Ruta terminada");
        this.controller.createRoute(controller.getTempRouteDTO(), controller.getRoutePoint());
        this.controller.setCreatingRoute(false);

        for (int i = controller.getRoutePoints().size() - 2; i > 0; i--) {
            RoutePoint routePoint = controller.getRoutePoint().get(i);
            routePoint.removeFromMap(controller.getMap());
            controller.getRoutePoints().remove(routePoint);
        }

        controller.getRoutePoints().put(controller.getTempRouteDTO().getID(), new ArrayList<>(controller.getRoutePoint()));

        for (RoutePoint routePoint : controller.getRoutePoint()) {
            routePoint.removeFromMap(controller.getMap());
        }
    }
}
