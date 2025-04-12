package org.pinguweb.backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pinguweb.backend.model.enums.DayMoment;
import org.pinguweb.backend.model.enums.WeekDay;

@Getter
@Entity
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "ID"
)
public class ScheduleAvailability {

    @ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne
    @JoinColumn(name = "volunteer_dni")
    @Setter
    private Volunteer volunteer;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayMoment dayMoment;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WeekDay weekDay;

    public ScheduleAvailability(DayMoment dayMoment, WeekDay weekDay) {
        this.dayMoment = dayMoment;
        this.weekDay = weekDay;
    }
}
