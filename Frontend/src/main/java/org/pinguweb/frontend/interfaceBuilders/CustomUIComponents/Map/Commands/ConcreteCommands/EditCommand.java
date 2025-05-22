package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import com.vaadin.flow.component.notification.Notification;
import lombok.Setter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.MapButtons;
import org.pinguweb.frontend.mapObjects.MapObject;

public class EditCommand implements Command {
    MapButtons buttonController;

    @Setter
    MapObject originalObject;

    @Setter
    MapObject resultObject;

    public EditCommand(MapButtons receiver){
        buttonController = receiver;
    }

    @Override
    public void execute() {
//        buttonController.toggleEdit(this);
//        //buttonReceiver.addExecutedCommand(this);
    }

    @Override
    public void undo() {
//        buttonController.editMapObject(originalObject);
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
