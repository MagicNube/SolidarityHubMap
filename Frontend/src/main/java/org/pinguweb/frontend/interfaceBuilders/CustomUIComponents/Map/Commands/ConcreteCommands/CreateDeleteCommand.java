package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapActions;

public class CreateDeleteCommand implements Command {
    MapActions buttonReceiver;

    public CreateDeleteCommand(MapActions receiver){
        buttonReceiver = receiver;
    }

    @Override
    public void execute() {
        buttonReceiver.addExecutedCommand(this);
        buttonReceiver.toggleDelete();
    }

    @Override
    public void undo() {
    }
}
