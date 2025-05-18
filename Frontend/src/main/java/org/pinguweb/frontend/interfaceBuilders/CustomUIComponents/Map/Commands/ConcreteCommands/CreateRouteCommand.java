package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import com.vaadin.flow.component.notification.Notification;
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

    @Setter
    List<RoutePoint> points;
    public CreateRouteCommand(MapActions receiver){
        buttonReceiver = receiver;
    }

    @Override
    public void execute() {
        buttonReceiver.toggleRouteCreation(this);
        buttonReceiver.addExecutedCommand(this);
        Notification notification = new Notification("Ruta creada exitosamente", 3000);
        notification.open();
    }

    @Override
    public void undo() {
        buttonReceiver.deleteRoute(this.route);
        Notification notification = new Notification("Creación de la ruta deshecha", 3000);
        notification.open();
    }

    @Override
    public void redo() {
        buttonReceiver.getService().createRoute(route.toDto(), points);
        Notification notification = new Notification("Ruta creada exitosamente", 3000);
        notification.open();
    }
}
