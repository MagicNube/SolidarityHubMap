package org.pinguweb.frontend.objects;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.pinguweb.frontend.objects.enums.EmergencyLevel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter

@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Catastrophe {
    @JsonProperty("id")
    private int iD;

    @Setter
    @NonNull
    private String name;

    @Setter
        @NonNull
    private String description;
    @Setter
    @NonNull
    private GPSCoordinates location;

    @Setter
    private LocalDate startDate;

    @Setter
    @NonNull
    private EmergencyLevel emergencyLevel;

    @Setter
    
    private List<Need> needs;

    @Setter
    private List<Zone> zones;

    public Catastrophe(@NonNull String name, @NonNull String description, @NonNull GPSCoordinates location, LocalDate startDate , @NonNull EmergencyLevel emergencyLevel) {
        this.zones = new ArrayList<>();
        this.needs = new ArrayList<>();
        this.name = name;
        this.description = description;
        this.location = location;
        this.startDate = startDate;
        this.emergencyLevel = emergencyLevel;
    }
}
