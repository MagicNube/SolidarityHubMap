package org.pinguweb.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.pinguweb.model.Catastrophe;
import org.pinguweb.model.Need;
import org.pinguweb.model.Zone;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class CatastropheDTO implements DTO{
    private int id;
    private String name;
    private String description;
    private int location;
    private LocalDate startDate;
    private String emergencyLevel;
    private List<Integer> needs;
    private List<Integer> zones;

    protected CatastropheDTO(Catastrophe catastrophe) {
        id = catastrophe.getID();
        name = catastrophe.getName();
        description = catastrophe.getDescription();
        startDate = catastrophe.getStartDate();
        emergencyLevel = catastrophe.getEmergencyLevel().name();
        location = catastrophe.getLocation().getId();

        if (catastrophe.getNeeds() != null) {
            needs = catastrophe.getNeeds().stream().map(Need::getId).collect(Collectors.toList());
        }
        if (catastrophe.getZones() != null) {
            zones = catastrophe.getZones().stream().map(Zone::getId).collect(Collectors.toList());
        }
    }
}
