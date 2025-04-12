package org.pinguweb.frontend.mapObjects.factories;

import org.pinguweb.frontend.mapObjects.MapObject;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

public abstract class MapObjectFactory {
    abstract MapObject createMapObject(LComponentManagementRegistry reg, Double latitude, Double longitude);
}
