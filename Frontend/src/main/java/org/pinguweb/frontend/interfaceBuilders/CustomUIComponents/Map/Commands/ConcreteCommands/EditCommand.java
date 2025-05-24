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
import org.pinguweb.frontend.mapObjects.MapObject;
import org.pinguweb.frontend.utils.Mediador.EventType;

public class EditCommand implements Command {
    MapButtons buttonController;

    @Setter
    MapObject originalObject;

    @Getter
    boolean working;

    @Setter
    MapObject resultObject;

    public EditCommand(MapButtons receiver){
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

    public void end(){
        buttonController.getMediator().publish(new ButtonEvent<>(EventType.ENABLE_BUTTONS, ButtonNames.EDIT));
        working = false;
        buttonController.getEdit().setText("Editar");
    }

    @Override
    public void undo() {
        Notification notification = new Notification("Edición deshecha", 3000);
        notification.open();
    }

    @Override
    public void redo() {
//        buttonController.editMapObject(resultObject);

        Notification notification = new Notification("Edición realizada exitosamente", 3000);
        notification.open();
    }
}
