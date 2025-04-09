package org.pinguweb.frontend.factory;

import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

public class MarkerFactory extends MapObjectFactory {

    @Override
    public MapObject createMapObject(LComponentManagementRegistry reg, Double latitude, Double longitude) {
        return new Marker(reg, latitude, longitude);
    }
}
