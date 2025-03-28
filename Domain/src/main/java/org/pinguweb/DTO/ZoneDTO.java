package org.pinguweb.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.pinguweb.model.Catastrophe;
import org.pinguweb.model.GPSCoordinates;
import org.pinguweb.model.Storage;
import org.pinguweb.model.Zone;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class ZoneDTO implements DTO{
    private int id;
    private String name;
    private String description;
    private String emergencyLevel;
    private List<Integer> catastrophes;
    private List<Integer> storages;
    private List<Integer> points;

    public ZoneDTO(Zone zone) {
        this.id = zone.getId();
        this.name = zone.getName();
        this.description = zone.getDescription();
        this.emergencyLevel = zone.getEmergencyLevel().name();
        this.catastrophes = zone.getCatastrophes().stream().map(Catastrophe::getID).collect(Collectors.toList());
        storages = zone.getStorages().stream().map(Storage::getId).collect(Collectors.toList());
        points = zone.getPoints().stream().map(GPSCoordinates::getId).collect(Collectors.toList());
    }
}
