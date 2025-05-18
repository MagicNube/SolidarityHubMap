package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import com.vaadin.flow.component.notification.Notification;
import lombok.Setter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapActions;
import org.pinguweb.frontend.mapObjects.MapObject;

public class EditCommand implements Command {
    MapActions buttonReceiver;

    @Setter
    MapObject lastObject;

    public EditCommand(MapActions receiver){
        buttonReceiver = receiver;
    }

    @Override
    public void execute() {
        buttonReceiver.toggleEdit(this);
        buttonReceiver.addExecutedCommand(this);

        Notification notification = new Notification("Edición realizada exitosamente", 3000);
        notification.open();
    }

    @Override
    public void undo() {
        Notification notification = new Notification("Edición deshecha", 3000);
        notification.open();
    }

    @Override
    public void redo() {
    }
}
