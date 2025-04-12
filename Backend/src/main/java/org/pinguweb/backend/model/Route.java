package org.pinguweb.backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.pinguweb.enums.RouteType;

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
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    @Setter
    @NonNull
    private String name;

    @Setter
    private String description;

    @Setter
    @Enumerated(EnumType.STRING)
    @NonNull
    private RouteType routeType;

    @Setter
    @ManyToOne
    @NonNull
    private Catastrophe catastrophe;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "points")
    @Setter
    @NonNull
    private List<RoutePoint> points;

    public Route(@NonNull String name, String description, @NonNull RouteType routeType, @NonNull Catastrophe catastrophe) {
        this.catastrophe = catastrophe;
        this.name = name;
        this.description = description;
        this.routeType = routeType;
        points = new ArrayList<>();
    }
}