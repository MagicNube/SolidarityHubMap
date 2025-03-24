package org.pinguweb.frontend.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;
import org.pinguweb.frontend.objects.enums.EmergencyLevel;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class Catastrophe {

    private int iD;

    @Setter
    private String name;

    @Setter
    private String description;

    @Setter
    private GPSCoordinates location;

    @Setter
    private LocalDate startDate;

    @Setter
    private EmergencyLevel emergencyLevel;

    @Setter
    private List<Need> needs;

    @Setter
    private List<Zone> zones;

    public Catastrophe(String name, String description, GPSCoordinates location, LocalDate startDate , EmergencyLevel emergencyLevel){
        this.zones = new ArrayList<>();
        this.needs = new ArrayList<>();
        this.name = name;
        this.description = description;
        this.location = location;
        this.startDate = startDate;
        this.emergencyLevel = emergencyLevel;
    }
}
