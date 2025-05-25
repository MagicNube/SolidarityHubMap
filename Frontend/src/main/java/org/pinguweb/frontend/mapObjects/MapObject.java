package org.pinguweb.frontend.mapObjects;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import software.xdev.vaadin.maps.leaflet.map.LMap;

@Data
public abstract class MapObject {
    private Double latitude;
    private Double longitude;

    @Getter
    @Setter
    private Integer ID;

    public abstract int pushToServer();
    public abstract int deleteFromServer();
    public abstract int updateToServer();

    public abstract void addToMap(LMap map);
    public abstract void removeFromMap(LMap map);
}
