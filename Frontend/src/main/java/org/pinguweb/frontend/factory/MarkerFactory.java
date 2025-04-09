package org.pinguweb.frontend.factory;

public class MarkerFactory extends MapObjectFactory {

    @Override
    public MapObject createMapObject(Double latitude, Double longitude) {
        return new Marker(latitude, longitude);
    }
}
