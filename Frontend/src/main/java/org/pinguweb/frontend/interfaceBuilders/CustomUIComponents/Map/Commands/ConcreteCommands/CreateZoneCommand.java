package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import com.vaadin.flow.component.notification.Notification;
import lombok.Setter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapActions;
import org.pinguweb.frontend.mapObjects.Zone;

public class CreateZoneCommand implements Command {
    MapActions buttonReceiver;
    @Setter
    private Zone zone;

    public CreateZoneCommand(MapActions receiver){
        buttonReceiver = receiver;
    }

    @Override
    public void execute() {
        buttonReceiver.toggleZoneCreation(this);
        buttonReceiver.addExecutedCommand(this);
        Notification notification = new Notification("Zona creada exitosamente", 3000);
        notification.open();
    }

    @Override
    public void undo() {
        buttonReceiver.deleteZone(zone);
        Notification notification = new Notification("Creación de la zona deshecha", 3000);
        notification.open();
    }
}
