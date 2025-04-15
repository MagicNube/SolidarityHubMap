package org.pinguweb.frontend.mapObjects.factories;

import org.pinguweb.frontend.mapObjects.MapObject;
import org.pinguweb.frontend.mapObjects.RoutePoint;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

public class RoutePointFactory extends MapObjectFactory {

    @Override
    public MapObject createMapObject(LComponentManagementRegistry reg, Double latitude, Double longitude) {
        return new RoutePoint(reg, latitude, longitude);
    }
}
