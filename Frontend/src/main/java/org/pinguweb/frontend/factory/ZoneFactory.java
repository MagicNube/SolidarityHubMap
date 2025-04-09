package org.pinguweb.frontend.factory;

import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

public class ZoneFactory extends MapObjectFactory {

    @Override
    public MapObject createMapObject(LComponentManagementRegistry reg, Double latitude, Double longitude) {
        return new Zone(latitude, longitude);
    }
}
