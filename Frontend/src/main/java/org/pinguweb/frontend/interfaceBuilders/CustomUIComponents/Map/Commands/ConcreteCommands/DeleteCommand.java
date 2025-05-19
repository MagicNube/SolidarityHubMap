package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import com.vaadin.flow.component.notification.Notification;
import lombok.Getter;
import lombok.Setter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapActions;
import org.pinguweb.frontend.mapObjects.*;

import java.util.List;

public class DeleteCommand implements Command {
    MapActions buttonReceiver;

    @Setter
    private MapObject element;

    @Setter
    @Getter
    private List<RoutePoint> points;

    public DeleteCommand(MapActions receiver){
        buttonReceiver = receiver;
    }

    @Override
    public void execute() {
        buttonReceiver.toggleDelete(this);
        buttonReceiver.addExecutedCommand(this);
        buttonReceiver.getService().setTempDeleteCommand(null);

        Notification notification = new Notification("Elemento eliminado exitosamente", 3000);
        notification.open();
    }

    @Override
    public void undo() {
        if(element instanceof Route){
            buttonReceiver.getService().createRoute(((Route) element).toDto(), points);
        }
        else if(element instanceof Zone){
            buttonReceiver.getService().createZone(((Zone) element).toDto());
        }
        else if(element instanceof Storage){
            buttonReceiver.getService().createStorage(((Storage) element).toDto());
        }

        Notification notification = new Notification("Borrado deshecho exitosamente", 3000);
        notification.open();
    }

    @Override
    public void redo() {
        if(element instanceof Route){
            buttonReceiver.deleteRoute((Route) element);
        }
        else if(element instanceof Zone){
            buttonReceiver.deleteZone((Zone) element);
        }
        else if(element instanceof Storage){
            buttonReceiver.deleteStorage((Storage) element);
        }

        Notification notification = new Notification("Elemento eliminado exitosamente", 3000);
        notification.open();
    }
}
