package org.pinguweb.DTO;

import lombok.Data;
import org.pinguweb.model.Catastrophe;
import org.pinguweb.model.Need;
import org.pinguweb.model.Zone;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CatastropheDTO {
    private int id;
    private String name;
    private String description;
    private int location;
    private LocalDate startDate;
    private String emergencyLevel;
    private List<Integer> needs;
    private List<Integer> zones;

    public CatastropheDTO(Catastrophe catastrophe) {
        id = catastrophe.getID();
        name = catastrophe.getName();
        description = catastrophe.getDescription();
        location = catastrophe.getLocation().getId();
        startDate = catastrophe.getStartDate();
        emergencyLevel = catastrophe.getEmergencyLevel().name();
        needs = catastrophe.getNeeds().stream().map(Need::getId).collect(Collectors.toList());
        zones = catastrophe.getZones().stream().map(Zone::getId).collect(Collectors.toList());
    }
}
