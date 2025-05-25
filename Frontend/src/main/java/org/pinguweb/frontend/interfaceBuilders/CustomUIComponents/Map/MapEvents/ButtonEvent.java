package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents;

import lombok.Getter;
import org.pinguweb.frontend.utils.Mediador.Event;
import org.pinguweb.frontend.utils.Mediador.EventType;

@Getter
public class ButtonEvent<ButtonNames> extends Event<ButtonNames> {
    public ButtonEvent(EventType type, ButtonNames payload) {
        super(type, payload, null);
    }
}