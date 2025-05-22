package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import com.vaadin.flow.component.notification.Notification;
import lombok.Setter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.DialogsNames;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.MapButtons;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.CreationEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.DeleteEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.ShowEvent;
import org.pinguweb.frontend.mapObjects.Zone;
import org.pinguweb.frontend.utils.Mediador.EventType;

public class CreateZoneCommand implements Command {
    MapButtons buttonController;
    @Setter
    private Zone zone;

    public CreateZoneCommand(MapButtons receiver){
        buttonController = receiver;
    }

    @Override
    public void execute() {
        buttonController.getMediator().publish(new ShowEvent<>(EventType.SHOW, DialogsNames.ZONE));
//        buttonController.toggleZoneCreation(this);
        buttonController.addExecutedCommand(this);
    }

    @Override
    public void undo() {
        buttonController.getMediator().publish(new DeleteEvent<>(this.zone, this));
//        buttonController.deleteZone(zone);
        Notification notification = new Notification("Creación de la zona deshecha", 3000);
        notification.open();
    }

    @Override
    public void redo() {
        buttonController.getMediator().publish(new CreationEvent<>(EventType.BUILD, zone, this, null));
//        buttonController.getService().setTempZoneDTO(zone.toDto());
//        buttonController.getBuild().endZoneConstruction(this);
        Notification notification = new Notification("Zona creada exitosamente", 3000);
        notification.open();
    }
}
