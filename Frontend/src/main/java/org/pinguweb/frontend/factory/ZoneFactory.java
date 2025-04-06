package org.pinguweb.frontend.factory;

public class ZoneFactory extends MapObjectFactory {

    @Override
    public MapObject createMapObject(Double latitude, Double longitude) {
        return new Zone(latitude, longitude);
    }
}
