package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents;

import lombok.Getter;
import org.pingu.domain.DTO.DTO;
import org.pingu.domain.DTO.RoutePointDTO;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.mapObjects.RoutePoint;
import org.pinguweb.frontend.utils.Mediador.Event;
import org.pinguweb.frontend.utils.Mediador.EventType;

import java.util.List;

@Getter
public class CreationEvent<T> extends Event<T> {
    private final List<RoutePoint> extraData;
    public CreationEvent(EventType type, T payload, Command command, List<RoutePoint> extraData) {
        super(type, payload, command);
        this.extraData = extraData;
    }
}