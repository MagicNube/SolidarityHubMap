package factory;

import factory.elements.ElementTypes;
import factory.elements.Refugio;
import factory.elements.ZonaAfectada;

public class MapElementFactory {
    public static MapElement crearElemento(ElementTypes type, double lat, double lon, String description) {
        switch (type) {
            case AFECTADA:
                return new ZonaAfectada(lat, lon, description);
            case REFUGIO:
                return new Refugio(lat, lon, description);
            default:
                throw new IllegalArgumentException("Tipo de elemento no reconocido");
        }
    }
}
