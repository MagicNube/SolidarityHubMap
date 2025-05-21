package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import com.vaadin.flow.component.notification.Notification;
import lombok.Setter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapActions;
import org.pinguweb.frontend.mapObjects.MapObject;

public class EditCommand implements Command {
    MapActions buttonReceiver;

    @Setter
    MapObject originalObject;

    @Setter
    MapObject resultObject;

    public EditCommand(MapActions receiver){
        buttonReceiver = receiver;
    }

    @Override
    public void execute() {
        buttonReceiver.toggleEdit(this);
        //buttonReceiver.addExecutedCommand(this);
    }

    @Override
    public void undo() {
        buttonReceiver.editMapObject(originalObject);
        Notification notification = new Notification("Edición deshecha", 3000);
        notification.open();
    }

    @Override
    public void redo() {
        buttonReceiver.editMapObject(resultObject);

        Notification notification = new Notification("Edición realizada exitosamente", 3000);
        notification.open();
    }
}
