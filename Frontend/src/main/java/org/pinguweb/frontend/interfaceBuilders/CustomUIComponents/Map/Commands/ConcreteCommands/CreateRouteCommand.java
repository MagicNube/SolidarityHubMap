package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import lombok.Setter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapActions;
import org.pinguweb.frontend.mapObjects.Route;
import org.pinguweb.frontend.mapObjects.RoutePoint;

import java.util.List;

public class CreateRouteCommand implements Command {
    MapActions buttonReceiver;

    @Setter
    Route route;

    public CreateRouteCommand(MapActions receiver){
        buttonReceiver = receiver;
    }

    @Override
    public void execute() {
        buttonReceiver.toggleRouteCreation(this);
        buttonReceiver.addExecutedCommand(this);
    }

    @Override
    public void undo() {
        buttonReceiver.deleteRoute(this.route);
    }
}
