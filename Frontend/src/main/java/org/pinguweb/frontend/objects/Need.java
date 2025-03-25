package org.pinguweb.frontend.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pinguweb.frontend.objects.enums.NeedType;
import org.pinguweb.frontend.objects.enums.UrgencyLevel;

@Getter
@NoArgsConstructor
public class Need {

    private int id;

    private Affected affected;

    @Setter
    private String description;

    @Setter
    private UrgencyLevel urgency;

    @Setter
    private NeedType needType;

    @Setter
    private GPSCoordinates location;

    @Setter
    private Catastrophe catastrophe;

    @Setter
    private Task task;


    public Need(Affected affected, String description, UrgencyLevel urgency, NeedType needType, GPSCoordinates location, Catastrophe catastrophe) {
        this.affected = affected;
        this.description = description;
        this.urgency = urgency;
        this.needType = needType;
        this.location = location;
        this.affected.addNeed(this);
        this.catastrophe = catastrophe;
    }
}
