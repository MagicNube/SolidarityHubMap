package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapActions;

public class CreateZoneCommand implements Command {
    MapActions buttonReceiver;

    public CreateZoneCommand(MapActions receiver){
        buttonReceiver = receiver;
    }

    @Override
    public void execute() {
        buttonReceiver.addExecutedCommand(this);
        buttonReceiver.toggleZoneCreation();
    }

    @Override
    public void undo() {
        buttonReceiver.undoCommand();
    }
}
