package org.pinguweb.frontend.factory;

import lombok.Data;
import org.pinguweb.DTO.DTO;

@Data
public abstract class MapObject {
    private Double latitude;
    private Double longitude;

    public abstract void pushToServer();
    public abstract void deleteFromServer();
}
