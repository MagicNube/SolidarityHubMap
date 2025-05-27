package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents;

import lombok.Getter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.utils.Mediador.Event;
import org.pinguweb.frontend.utils.Mediador.EventType;

@Getter
public class GenericEvent<T> extends Event<T> {
    public GenericEvent(EventType type, T payload, Command command) {
        super(type, payload, command);
    }
}