package es.pingu.map.mapController;

public abstract class MapElement {
    protected double lat;
    protected double lon;
    protected String description;

    public MapElement(double lat, double lon, String description) {
        this.lat = lat;
        this.lon = lon;
        this.description = description;
    }
}
