package org.pinguweb.backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.pingu.domain.enums.RoutePointType;

@Getter
@Entity
@NoArgsConstructor
@ToString
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "ID"
)
public class RoutePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "location_id")
    @Setter
    private GPSCoordinates location;

    @Setter
    @Enumerated(EnumType.STRING)
    @NonNull
    private RoutePointType routePointType;

    @Setter
    @ManyToOne
    @NonNull
    private Route route;

    public RoutePoint(@NonNull GPSCoordinates location, @NonNull RoutePointType type, @NonNull Route route) {
        this.location = location;
        this.routePointType = type;
        this.route = route;
    }
}