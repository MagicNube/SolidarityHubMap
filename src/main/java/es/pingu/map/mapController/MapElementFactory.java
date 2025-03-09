package es.pingu.map.mapController;

import es.pingu.map.mapController.elements.ElementTypes;
import es.pingu.map.mapController.elements.Refugio;
import es.pingu.map.mapController.elements.ZonaAfectada;

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
