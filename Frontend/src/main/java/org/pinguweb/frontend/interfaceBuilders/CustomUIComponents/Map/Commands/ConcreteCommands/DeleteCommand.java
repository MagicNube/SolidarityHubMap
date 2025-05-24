package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import com.vaadin.flow.component.notification.Notification;
import lombok.Getter;
import lombok.Setter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.ButtonNames;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.ClickedElement;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.MapButtons;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.ButtonEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.RequestClickEvent;
import org.pinguweb.frontend.mapObjects.*;
import org.pinguweb.frontend.utils.Mediador.EventType;

import java.util.List;

public class DeleteCommand implements Command {
    MapButtons buttonController;

    @Getter
    boolean working;

    public DeleteCommand(MapButtons receiver){
        buttonController = receiver;
    }

    @Override
    public void execute() {
        if (!working) {
            buttonController.getMediator().publish(new ButtonEvent<>(EventType.DISABLE_BUTTONS, ButtonNames.DELETE));
            buttonController.getMediator().publish(new RequestClickEvent<>(ClickedElement.DELETE,this));
            working = true;
            buttonController.getDelete().setText("Cancelar");
        }
        else{
            buttonController.getMediator().publish(new ButtonEvent<>(EventType.ENABLE_BUTTONS, ButtonNames.DELETE));
            buttonController.getMediator().publish(new RequestClickEvent<>(ClickedElement.DELETE, this));
            working = false;
            buttonController.getDelete().setText("Borrar");
        }
    }

    public void end(){
        buttonController.getMediator().publish(new ButtonEvent<>(EventType.ENABLE_BUTTONS, ButtonNames.DELETE));
        working = false;
        buttonController.getDelete().setText("Borrar");
    }

    @Override
    public void undo() {
    }

    @Override
    public void redo() {
    }
}
