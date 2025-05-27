package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import lombok.Getter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.enums.ButtonNames;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.enums.ClickedElement;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.Buttons;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.ButtonEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.RequestClickEvent;
import org.pinguweb.frontend.utils.Mediador.EventType;

public class DeleteCommand implements Command {
    Buttons buttonController;

    @Getter
    boolean working;

    public DeleteCommand(Buttons receiver){
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

    @Override
    public void endExecution(){
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
