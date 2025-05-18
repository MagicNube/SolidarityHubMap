package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapActions;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapButtons;

public class CreateRouteCommand implements Command {
    MapActions buttonReceiver;

    public CreateRouteCommand(MapActions receiver){
        buttonReceiver = receiver;
    }

    @Override
    public void execute() {
        buttonReceiver.executedCommand(this);
        buttonReceiver.toggleRouteCreation();
    }

    @Override
    public void undo() {
    }
}
