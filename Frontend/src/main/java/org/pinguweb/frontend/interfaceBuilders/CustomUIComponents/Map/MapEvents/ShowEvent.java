package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents;

import lombok.Getter;
import org.pinguweb.frontend.utils.Mediador.Event;
import org.pinguweb.frontend.utils.Mediador.EventType;

@Getter
public class ShowEvent<DialogsNames> extends Event<DialogsNames> {
    public ShowEvent(EventType type, DialogsNames payload) {
        super(type, payload, null);
    }
}