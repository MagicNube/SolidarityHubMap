package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents;

import lombok.Getter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.enums.ClickedElement;
import org.pinguweb.frontend.utils.Mediador.Event;
import org.pinguweb.frontend.utils.Mediador.EventType;

@Getter
public class ClickedEvent<T> extends Event<T> {
    private ClickedElement clickedElement;
    public ClickedEvent(EventType type, T payload, Command c, ClickedElement element) {
        super(type, payload, null);
        clickedElement = element;
    }
}