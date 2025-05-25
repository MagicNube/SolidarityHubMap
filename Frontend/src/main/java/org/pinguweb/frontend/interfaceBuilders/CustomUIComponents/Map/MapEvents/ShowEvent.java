package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents;

import lombok.Getter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.DialogsNames;
import org.pinguweb.frontend.utils.Mediador.Event;
import org.pinguweb.frontend.utils.Mediador.EventType;

@Getter
public class ShowEvent<T> extends Event<T> {

    private DialogsNames name;
    public ShowEvent(EventType type, T payload, DialogsNames name) {
        super(type, payload, null);
        this.name = name;
    }
    public ShowEvent(EventType type, T payload, DialogsNames name, Command c) {
        super(type, payload, c);
        this.name = name;
    }
}