package org.pinguweb.backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.pingu.domain.enums.EmergencyLevel;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@ToString
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "ID"
)
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    @Setter
    private String name;

    @Setter
    private String description;

    @Setter
    @Enumerated(EnumType.STRING)
    private EmergencyLevel emergencyLevel;

    @Setter
    @ManyToOne
    @JoinTable(name = "catastrophic_zones",
            joinColumns = @JoinColumn(name = "zone_id"),
            inverseJoinColumns = @JoinColumn(name = "catastrophe_id"))
    private Catastrophe catastrophe;

    @Setter
    @OneToMany(mappedBy = "zone")
    private List<Storage> storages;

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