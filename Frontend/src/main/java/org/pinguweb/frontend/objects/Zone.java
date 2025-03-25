package org.pinguweb.frontend.objects;


import lombok.*;
import org.pinguweb.frontend.objects.enums.EmergencyLevel;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class Zone {

    private int id;

    @Setter
    private String name;

    @Setter
    private String description;

    @Setter

    private EmergencyLevel emergencyLevel;

    @Setter
    private List<Catastrophe> catastrophes;

    @Setter
    private List<Storage> storages;

    @Setter
    @NonNull
    private List<GPSCoordinates> points;

    public Zone(String name, String description, EmergencyLevel emergencyLevel) {
        this.catastrophes = new ArrayList<>();
        this.storages = new ArrayList<>();
        this.name = name;
        this.description = description;
        this.emergencyLevel = emergencyLevel;
        points = new ArrayList<>();
    }

    public void addStorage(Storage storage) {
        this.storages.add(storage);
    }
    public void addCatastrophe(Catastrophe catastrophe) {
        this.catastrophes.add(catastrophe);
    }
}