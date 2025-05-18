package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

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
    }

    @Override
    public void undo() {
        buttonReceiver.deleteStorage(this.storage);
    }
}
