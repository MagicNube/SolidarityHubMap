package org.pinguweb.frontend.mapObjects.factories;

import org.pinguweb.frontend.mapObjects.MapObject;
import org.pinguweb.frontend.mapObjects.ZoneMarker;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

public class ZoneMarkerFactory extends MapObjectFactory {

    @Override
    public MapObject createMapObject(LComponentManagementRegistry reg, Double latitude, Double longitude) {
        return new ZoneMarker(reg, latitude, longitude);
    }
}
