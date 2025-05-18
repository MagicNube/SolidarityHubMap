package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import com.vaadin.flow.component.notification.Notification;
import lombok.Setter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapActions;
import org.pinguweb.frontend.mapObjects.Storage;

public class CreateStorageCommand implements Command {
    MapActions buttonReceiver;

    @Setter
    Storage storage;
    public CreateStorageCommand(MapActions receiver){
        buttonReceiver = receiver;
    }

    @Override
    public void execute() {
        buttonReceiver.toggleStorageCreation(this);
        buttonReceiver.addExecutedCommand(this);
        Notification notification = new Notification("Almacén creado exitosamente", 3000);
        notification.open();
    }

    @Override
    public void undo() {
        buttonReceiver.deleteStorage(this.storage);
        Notification notification = new Notification("Creación del almacén deshecha", 3000);
        notification.open();
    }
}
