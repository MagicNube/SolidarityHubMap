package org.pinguweb.frontend.mapObjects.factories;

import org.pinguweb.frontend.mapObjects.MapObject;
import org.pinguweb.frontend.mapObjects.Storage;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

public class StorageFactory extends MapObjectFactory {

    @Override
    public MapObject createMapObject(LComponentManagementRegistry reg, Double latitude, Double longitude) {
        return new Storage(reg, latitude, longitude);
    }
}
