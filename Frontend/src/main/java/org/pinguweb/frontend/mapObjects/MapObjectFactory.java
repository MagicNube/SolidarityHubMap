package org.pinguweb.frontend.mapObjects;

import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

public class MapObjectFactory {
    public Need createNeed(LComponentManagementRegistry reg, Double latitude, Double longitude) {
        return new Need(reg, latitude, longitude);
    }

    public Route createRoute() {
        return new Route();
    }

    public RoutePoint createRoutePoint(LComponentManagementRegistry reg, Double latitude, Double longitude) {
        return new RoutePoint(reg, latitude, longitude);
    }

    public Storage createStorage(LComponentManagementRegistry reg, Double latitude, Double longitude) {
        return new Storage(reg, latitude, longitude);
    }

    public Zone createZone() {
        return new Zone();
    }

    public ZoneMarker createZoneMarker(LComponentManagementRegistry reg, Double latitude, Double longitude) {
        return new ZoneMarker(reg, latitude, longitude);
    }
}
