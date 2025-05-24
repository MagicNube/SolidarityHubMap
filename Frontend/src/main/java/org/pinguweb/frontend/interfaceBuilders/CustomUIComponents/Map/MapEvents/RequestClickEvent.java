package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents;

import lombok.Getter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.ClickedElement;
import org.pinguweb.frontend.utils.Mediador.Event;
import org.pinguweb.frontend.utils.Mediador.EventType;

@Getter
public class RequestClickEvent<ClickedElement> extends Event<ClickedElement> {
    public RequestClickEvent(ClickedElement element) {
        super(EventType.REQUEST_CLICK, element, null);
    }
    public RequestClickEvent(ClickedElement element, Command c) {
        super(EventType.REQUEST_CLICK, element, c);
    }
}