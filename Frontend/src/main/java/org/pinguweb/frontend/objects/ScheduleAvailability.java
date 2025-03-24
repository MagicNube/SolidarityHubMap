package org.pinguweb.frontend.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pinguweb.frontend.objects.enums.DayMoment;
import org.pinguweb.frontend.objects.enums.WeekDay;


@Getter
@NoArgsConstructor
public class ScheduleAvailability {

    private Long id;

    @Setter
    private Volunteer volunteer;

    @Setter
    private DayMoment dayMoment;

    @Setter
    private WeekDay weekDay;

    public ScheduleAvailability(DayMoment dayMoment, WeekDay weekDay) {
        this.dayMoment = dayMoment;
        this.weekDay = weekDay;
    }
}
