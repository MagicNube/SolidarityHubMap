package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapActions;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapButtons;

public class CreateZoneCommand implements Command {
    MapActions buttonReceiver;

    public CreateZoneCommand(MapActions receiver){
        buttonReceiver = receiver;
    }

    @Override
    public void execute() {
        buttonReceiver.executedCommand(this);
        buttonReceiver.toggleZoneCreation();
    }

    @Override
    public void undo() {
        buttonReceiver.undoCommand();
    }
}
