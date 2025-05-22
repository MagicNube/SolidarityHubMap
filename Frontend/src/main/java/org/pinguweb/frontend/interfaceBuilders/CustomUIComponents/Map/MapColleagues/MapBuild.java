package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.RouteDTO;
import org.pingu.domain.DTO.StorageDTO;
import org.pingu.domain.DTO.ZoneDTO;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.*;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Map;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.CreationEvent;
import org.pinguweb.frontend.mapObjects.*;
import org.pinguweb.frontend.utils.Mediador.Colleague;
import org.pinguweb.frontend.utils.Mediador.ComponentColleague;
import org.pinguweb.frontend.utils.Mediador.Event;
import org.pinguweb.frontend.utils.Mediador.EventType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Setter
@Getter
public class MapBuild extends ComponentColleague {

    //MapService service;

    private String clickFuncReferenceCreateZone;
    private String clickFuncReferenceCreateRoute;

    public MapBuild(Map map) {
        super(map);
        clickFuncReferenceCreateZone = service.getMap().clientComponentJsAccessor() + ".myClickFuncCreateZone";
        clickFuncReferenceCreateRoute = service.getMap().clientComponentJsAccessor() + ".myClickFuncCreateRoute";
    }

    @Override
    public void register() {
        mediator.subscribe(EventType.BUILD, (Colleague) this);
    }

    @Override
    public <T> void receive(Event<T> event) {
        if (event.getPayload() instanceof StorageDTO){
            createStorage((StorageDTO) event.getPayload(), (CreateStorageCommand) event.getCommand());
        }
        else if (event.getPayload() instanceof ZoneDTO){
            createZone((ZoneDTO) event.getPayload(), (CreateZoneCommand) event.getCommand());
        }
        else if (event.getPayload() instanceof RouteDTO){
            createRoute((RouteDTO) event.getPayload(), ((CreationEvent<T>) event).getExtraData(), (CreateRouteCommand) event.getCommand());
        }

    }

    public void createStorage(StorageDTO storageDTO, CreateStorageCommand c) {
        service.setClickFuncReferenceCreateStorage(service.getMap().clientComponentJsAccessor() + ".myClickFuncCreateNeed");
        service.getReg().execJs(service.getClickFuncReferenceCreateStorage() + "=e => document.getElementById('" + service.getID() + "').$server.mapStorage(e.latlng)");
        service.getMap().on("click", service.getClickFuncReferenceCreateStorage());

        UI ui = UI.getCurrent();
        if (ui != null) {
            service.setUi(ui);
        }

        new Thread(() -> {
            synchronized (service.getLock()) {
                try {
                    System.out.println("Esperando clic en el mapa...");
                    service.getLock().wait();

                    UI uiThread = service.getUi();
                    if (uiThread != null) {
                        uiThread.access(mapButtons::enableButtons);
                        mapButtons.getMap().setState(MapState.IDLE);
                    } else {
                        System.err.println("No se pudo acceder a la UI desde el hilo");
                    }
                } catch (InterruptedException e) {
                    System.out.println("Error esperando clic en el mapa" + e);
                }
            }
        }).start();
    }

    private Storage createStorage(StorageDTO storage) {
        double lat = storage.getLatitude();
        double lng = storage.getLongitude();
        Storage storageObj = (Storage) storageFactory.createMapObject(this.map.getReg(), lat, lng);
        storageObj.setID(storage.getID());
        storageObj.setName(storage.getName() != null ? storage.getName() : "Sin nombre");
        storageObj.setFull(storage.isFull());
        storageObj.setLatitude(storage.getLatitude());
        storageObj.setLongitude(storage.getLongitude());
        storageObj.setZoneID(storage.getZone() != null ? storage.getZone() : -1);
        storageObj.addToMap(this.map.getMap());
        storageObj.getMarkerObj().bindPopup("Almacen: " + storage.getName());

        storages.add(storageObj);
        lLayerGroupStorages.addLayer(storageObj.getMarkerObj());
        this.map.getMap().addLayer(lLayerGroupStorages);
        return storageObj;
    }

    public void endStorageConstruction(){
        Storage storage = service.createStorage(service.getTempStorageDTO());
        if (service.getTempStorageCommand() != null ) {service.getTempStorageCommand().setStorage(storage);}
        int tempId = storage.pushToServer();
        storage.setID(tempId);
        service.getStorages().stream().filter(s -> Objects.equals(s.getID(), storage.getID())).findFirst().ifPresent(s -> {
            service.getStorages().remove(s);
        });
        System.out.println(service.getStorages());
        synchronized (service.getLock()) {
            service.getLock().notify();
        }
        service.getMap().off("click", service.getClickFuncReferenceCreateStorage());

        Notification notification = new Notification("Almacén creado exitosamente", 3000);
        notification.open();
    }

    public void startZoneConstruction(ZoneDTO zoneDTO) {
        service.setTempZoneDTO(zoneDTO);
        log.debug("Registrando puntos para la zona");
        service.getReg().execJs(clickFuncReferenceCreateZone + "=e => document.getElementById('" + service.getID() + "').$server.mapZona(e.latlng)");
        service.getMap().on("click", clickFuncReferenceCreateZone);
    }

    public void createZone(ZoneDTO zone, CreateZoneCommand c) {
        service.getMap().off("click", clickFuncReferenceCreateZone);
        log.debug("Zona terminada");
        Zone zona = this.service.createZone(zone);

        int newID = zona.pushToServer();
        service.getZones().stream().filter(z -> z.getID() == service.getTempZoneDTO().getID()).findFirst().ifPresent(z -> {
            z.setID(newID);
        });

        for (ZoneMarker zoneMarker : service.getZoneMarkers().values()) {
            zoneMarker.removeFromMap(service.getMap());
        }

        service.getZoneMarkers().clear();
        service.getZoneMarkerPoints().clear();
        if (c != null) {c.setZone(zona);}
    }

    public void editZone(Zone zone) {
        log.debug("Zona editada");
        zone.updateToServer();
        service.getZones().stream().filter(z -> Objects.equals(z.getID(), zone.getID())).findFirst().ifPresent(z -> {
            service.getZones().remove(z);
            service.getZones().add(zone);
        });
        service.updateZone(zone);
    }

    public void startRouteConstruction(RouteDTO routeDTO) {
        service.setTempRouteDTO(routeDTO);
        log.debug("Registrando puntos para la ruta");
        service.getReg().execJs(clickFuncReferenceCreateRoute + "=e => document.getElementById('" + service.getID() + "').$server.mapRoute(e.latlng)");
        service.getMap().on("click", clickFuncReferenceCreateRoute);
    }

    public void createRoute(RouteDTO route, List<RoutePoint> points, CreateRouteCommand c) {
        service.getMap().off("click", clickFuncReferenceCreateRoute);
        log.debug("Ruta terminada");
        ArrayList<RoutePoint> routePoints = points;
        ArrayList<Integer> pointsID = new ArrayList<>();
        for (RoutePoint routePoint : routePoints) {
            pointsID.add(routePoint.pushToServer());
        }

        if (c != null) {c.setPoints(routePoints);}

        Route ruta = this.service.createRoute(service.getTempRouteDTO(), service.getRoutePoint());
        ruta.setPointsID(pointsID);
        int tempID = ruta.pushToServer();

        if (c != null) {c.setRoute(ruta);}

        service.getRoutes().stream().filter(r -> r.getID() == service.getTempRouteDTO().getID()).findFirst().ifPresent(r -> {
            service.getRoutes().remove(r);
            service.getRoutes().add(ruta);
        });
        service.getRoutePoints().put(tempID, new ArrayList<>(service.getRoutePoint()));

        for (int i = service.getRoutePoint().size() - 2; i > 0; i--) {
            RoutePoint routePoint = service.getRoutePoint().get(i);
            routePoint.removeFromMap(service.getMap());
            service.getRoutePoint().remove(routePoint);
        }

        service.getRoutePoint().clear();
    }


    public void editRoute(org.pinguweb.frontend.mapObjects.Route route) {
        log.debug("Ruta editada");
        route.updateToServer();
        service.getRoutes().stream().filter(r -> Objects.equals(r.getID(), route.getID())).findFirst().ifPresent(r -> {
            service.getRoutes().remove(r);
            service.getRoutes().add(route);
        });
        service.updateRoute(route);
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
        for (Storage storage : this.service.getStorages()) {
            String clickFuncReferenceEditStorage = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncEditStorage" + storage.getID();
            this.service.getReg().execJs(clickFuncReferenceEditStorage + "=e => document.getElementById('" + this.service.getID() + "').$server.editStorage('" + storage.getID() + "') ");
            storage.getMarkerObj().on("click", clickFuncReferenceEditStorage);
        }
    }

    public void endEdit(EditCommand c) {
        this.service.setTempEditCommand(c);

        for (Zone zone : this.service.getZones()) {
            String clickFuncReferenceEditZone = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncEditZone" + zone.getID();
            zone.getPolygon().off("click", clickFuncReferenceEditZone);
        }
        for (org.pinguweb.frontend.mapObjects.Route route : this.service.getRoutes()) {
            String clickFuncReferenceEditRoute = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncEditRoute" + route.getID();
            route.getPolygon().off("click", clickFuncReferenceEditRoute);
        }
        for (Storage storage : this.service.getStorages()) {
            String clickFuncReferenceEditStorage = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncEditStorage" + storage.getID();
            storage.getMarkerObj().off("click", clickFuncReferenceEditStorage);
        }

        Notification notification = new Notification("Edición realizada exitosamente", 3000);
        notification.open();
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
        for (Storage storage : this.service.getStorages()) {
            String clickFuncReferenceDeleteStorage = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteStorage" + storage.getID();
            this.service.getReg().execJs(clickFuncReferenceDeleteStorage + "=e => document.getElementById('" + this.service.getID() + "').$server.removeStorage('" + storage.getID() + "') ");
            storage.getMarkerObj().on("click", clickFuncReferenceDeleteStorage);
        }
    }

    public void endDelete(DeleteCommand c) {
        this.service.setTempDeleteCommand(c);
        for (Zone zone : this.service.getZones()) {
            String clickFuncReferenceDeleteZone = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteZone" + zone.getID();
            zone.getPolygon().off("click", clickFuncReferenceDeleteZone);
        }
        for (org.pinguweb.frontend.mapObjects.Route route : this.service.getRoutes()) {
            String clickFuncReferenceDeleteRoute = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteRoute" + route.getID();
            route.getPolygon().off("click", clickFuncReferenceDeleteRoute);
        }
        for (Storage storage : this.service.getStorages()) {
            String clickFuncReferenceDeleteStorage = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteStorage" + storage.getID();
            storage.getMarkerObj().off("click", clickFuncReferenceDeleteStorage);
        }
    }

    public void editStorage(Storage storage) {
        log.debug("Almacen editado");
        storage.updateToServer();
        service.getStorages().stream().filter(s -> Objects.equals(s.getID(), storage.getID())).findFirst().ifPresent(s -> {
            service.getStorages().remove(s);
            service.getStorages().add(storage);
        });
        service.updateStorage(storage);
    }

}
