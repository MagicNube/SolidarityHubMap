package org.pinguweb.backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.pinguweb.DTO.ZoneDTO;
import org.pinguweb.backend.model.enums.EmergencyLevel;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@ToString
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter
    private String name;

    @Setter
    private String description;

    @Setter
    @Enumerated(EnumType.STRING)
    private EmergencyLevel emergencyLevel;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "location_id")
    @Setter
    @NonNull
    private List<GPSCoordinates> points;

    public Zone(String name, String description, EmergencyLevel emergencyLevel) {
        this.name = name;
        this.description = description;
        this.emergencyLevel = emergencyLevel;
        points = new ArrayList<>();
    }
}