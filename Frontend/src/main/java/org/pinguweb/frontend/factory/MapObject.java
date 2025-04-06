package org.pinguweb.frontend.factory;

import lombok.Data;
import org.pinguweb.DTO.DTO;

@Data
public abstract class MapObject {
    private Double latitude;
    private Double longitude;

    public abstract void pushToServer(String url, DTO dto);
    public abstract void deleteFromServer(String url, DTO dto);
}
