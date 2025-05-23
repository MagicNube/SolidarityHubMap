package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import com.vaadin.flow.component.notification.Notification;
import lombok.Setter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.ButtonNames;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.ClickedElement;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.DialogsNames;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.MapButtons;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.*;
import org.pinguweb.frontend.mapObjects.Zone;
import org.pinguweb.frontend.utils.Mediador.EventType;

public class CreateZoneCommand implements Command {
    MapButtons buttonController;

    private boolean working = false;

    @Setter
    private Zone zone;

    public CreateZoneCommand(MapButtons receiver){
        buttonController = receiver;
    }

    @Override
    public void execute() {
        if (!working) {
            buttonController.getZone().setText("Terminar Zona");
            buttonController.getMediator().publish(new ButtonEvent<>(EventType.DISABLE_BUTTONS, ButtonNames.ZONE));
            buttonController.getMediator().publish(new RequestClickEvent<>(ClickedElement.ZONE));
        }
        else{
            buttonController.getZone().setText("Crear zona");
            buttonController.getMediator().publish(new ButtonEvent<>(EventType.ENABLE_BUTTONS,null));
            buttonController.getMediator().publish(new GenericEvent<>(EventType.END_CLICK, ButtonNames.ZONE, this));
            buttonController.addExecutedCommand(this);
        }
    }

    @Override
    public void undo() {
        buttonController.getMediator().publish(new DeleteEvent<>(this.zone, this));
        Notification notification = new Notification("Creación de la zona deshecha", 3000);
        notification.open();
    }

    @Override
    public void redo() {
        buttonController.getMediator().publish(new CreationEvent<>(EventType.CREATE, zone, this, null));
        Notification notification = new Notification("Zona creada exitosamente", 3000);
        notification.open();
    }
}
