package org.pinguweb.frontend.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class Storage {

    private int id;

    @Setter
    private String name;

    @Setter
    private GPSCoordinates gpsCoordinates;

    @Setter
    private boolean isFull;

    @Setter
    private List<Resource> resources;

    @Setter
    private Zone zone;

    public Storage(String name, GPSCoordinates gpsCoordinates, boolean isFull, Zone zone) {
        this.resources = new ArrayList<>();
        this.name = name;
        this.gpsCoordinates = gpsCoordinates;
        this.isFull = isFull;
        this.zone = zone;
    }

    public void addResource(Resource resource) {
        this.resources.add(resource);
    }
}
