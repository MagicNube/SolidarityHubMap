package org.pinguweb.backend.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;
import org.pinguweb.backend.model.*;
import org.pinguweb.backend.model.enums.NeedType;
import org.pinguweb.backend.model.enums.UrgencyLevel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class NeedDTO {
    private int id;
    private String affected;
    private String description;
    private String urgency;
    private String needType;
    private int location;
    private int catastrophe;
    private int task;

    public NeedDTO(Need need) {
        id = need.getId();
        affected = need.getAffected().getDNI();
        description = need.getDescription();
        urgency = need.getUrgency().name();
        needType = need.getNeedType().name();
        location = need.getLocation().getId();
        catastrophe = need.getCatastrophe().getID();
        task = need.getTask().getId();
    }
}
