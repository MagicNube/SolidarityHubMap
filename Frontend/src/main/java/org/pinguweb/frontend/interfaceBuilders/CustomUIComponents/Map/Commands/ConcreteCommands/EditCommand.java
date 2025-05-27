package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import com.vaadin.flow.component.notification.Notification;
import lombok.Getter;
import lombok.Setter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.Buttons;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.enums.ButtonNames;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.enums.ClickedElement;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.ButtonEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.GenericEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.RequestClickEvent;
import org.pinguweb.frontend.mapObjects.MapObject;
import org.pinguweb.frontend.utils.Mediador.EventType;

public class EditCommand implements Command {
    Buttons buttonController;
    private boolean first = true;
    @Getter
    boolean working;

    @Setter
    MapObject originalObject;

    @Setter
    MapObject resultObject;

    public EditCommand(Buttons receiver){
        buttonController = receiver;
    }

    @Override
    public void execute() {
        if (!working) {
            buttonController.getMediator().publish(new ButtonEvent<>(EventType.DISABLE_BUTTONS, ButtonNames.EDIT));
            buttonController.getMediator().publish(new RequestClickEvent<>(ClickedElement.EDIT,this));
            working = true;
            buttonController.getEdit().setText("Cancelar");
        }
        else{
            buttonController.getMediator().publish(new ButtonEvent<>(EventType.ENABLE_BUTTONS, ButtonNames.EDIT));
            buttonController.getMediator().publish(new RequestClickEvent<>(ClickedElement.EDIT, this));
            working = false;
            buttonController.getEdit().setText("Editar");
        }
    }

    @Override
    public void endExecution(){
        if (first)
        {buttonController.addExecutedCommand(this); first = false;}
        buttonController.getMediator().publish(new ButtonEvent<>(EventType.ENABLE_BUTTONS, ButtonNames.EDIT));
        working = false;
        buttonController.getEdit().setText("Editar");
        Notification notification = new Notification("Edición realizada exitosamente", 3000);
        notification.open();
    }

    @Override
    public void undo() {
        buttonController.getMediator().publish(new GenericEvent<>(EventType.EDIT, originalObject, null));
        Notification notification = new Notification("Edición deshecha", 3000);
        notification.open();
    }

    @Override
    public void redo() {
        buttonController.getMediator().publish(new GenericEvent<>(EventType.EDIT, resultObject, null));
        Notification notification = new Notification("Edición realizada exitosamente", 3000);
        notification.open();
    }
}
