package org.pinguweb.frontend.factory;

import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

public class MarkerFactory extends MapObjectFactory {

    @Override
    public MapObject createMapObject(LComponentManagementRegistry reg, Double latitude, Double longitude) {
        return new Marker(reg, latitude, longitude);
    }

    public MapObject createZoneMapObject(LComponentManagementRegistry reg, Double latitude, Double longitude) {
        return ((Marker) createMapObject(reg, latitude, longitude)).convertToZoneMarker(reg);
    }
}
