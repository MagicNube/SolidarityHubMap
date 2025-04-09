package org.pinguweb.frontend.factory;

import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

public abstract class MapObjectFactory {
    abstract MapObject createMapObject(LComponentManagementRegistry reg, Double latitude, Double longitude);
}
