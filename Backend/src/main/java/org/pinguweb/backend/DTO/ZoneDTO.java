package org.pinguweb.backend.DTO;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import org.pinguweb.backend.model.*;
import org.pinguweb.backend.model.enums.EmergencyLevel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ZoneDTO {
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
