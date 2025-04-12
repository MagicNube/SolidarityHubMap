package org.pinguweb.frontend.mapObjects.factories;

import org.pinguweb.frontend.mapObjects.MapObject;
import org.pinguweb.frontend.mapObjects.Need;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

public class NeedFactory extends MapObjectFactory {

    @Override
    public MapObject createMapObject(LComponentManagementRegistry reg, Double latitude, Double longitude) {
        return new Need(reg, latitude, longitude);
    }
}
