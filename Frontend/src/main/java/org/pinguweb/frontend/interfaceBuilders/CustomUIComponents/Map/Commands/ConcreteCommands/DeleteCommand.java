package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import com.vaadin.flow.component.notification.Notification;
import lombok.Getter;
import lombok.Setter;
import org.pingu.domain.DTO.StorageDTO;
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
        //buttonReceiver.addExecutedCommand(this);
        buttonReceiver.getService().setTempDeleteCommand(null);
        Notification notification = new Notification("Elemento eliminado exitosamente", 3000);
        notification.open();
    }

    @Override
    public void undo() {
        if(element instanceof Route){
            buttonReceiver.getService().setTempRouteDTO(((Route) element).toDto());
            buttonReceiver.getService().setRoutePoint(points);
            buttonReceiver.getBuild().endRouteConstruction(null);
        }
        else if(element instanceof Zone){
            buttonReceiver.getService().setTempZoneDTO(((Zone) element).toDto());
            buttonReceiver.getBuild().endZoneConstruction(null);
        }
        else if(element instanceof Storage){
            buttonReceiver.getService().setTempStorageDTO(((Storage) element).toDto());
            buttonReceiver.getService().setTempStorageCommand(null);
            buttonReceiver.getBuild().endStorageConstruction();
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
    }
}
