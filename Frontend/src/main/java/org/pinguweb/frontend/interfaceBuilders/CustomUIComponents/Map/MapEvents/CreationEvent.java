package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents;

import lombok.Getter;
import lombok.Setter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.enums.ClickedElement;
import org.pinguweb.frontend.mapObjects.RoutePoint;
import org.pinguweb.frontend.utils.Mediador.Event;
import org.pinguweb.frontend.utils.Mediador.EventType;

import java.util.List;

@Getter
public class CreationEvent<T> extends Event<T> {
    private final List<RoutePoint> extraData;
    @Setter
    private ClickedElement element;
    public CreationEvent(EventType type, T payload, Command command, List<RoutePoint> extraData) {
        super(type, payload, command);
        this.extraData = extraData;
    }
}