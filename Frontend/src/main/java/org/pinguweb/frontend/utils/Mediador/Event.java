package org.pinguweb.frontend.utils.Mediador;

import lombok.Getter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;

@Getter
public abstract class Event<T> {
    private final EventType type;
    private final T payload;
    private final Command command;

    public Event(EventType type, T payload, Command command) {
        this.type = type;
        this.payload = payload;
        this.command = command;
    }
}