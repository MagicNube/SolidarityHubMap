package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapActions;

public class CreateEditCommand implements Command {
    MapActions buttonReceiver;

    public CreateEditCommand(MapActions receiver){
        buttonReceiver = receiver;
    }

    @Override
    public void execute() {
        buttonReceiver.addExecutedCommand(this);
        buttonReceiver.toggleEdit();
    }

    @Override
    public void undo() {
    }
}
