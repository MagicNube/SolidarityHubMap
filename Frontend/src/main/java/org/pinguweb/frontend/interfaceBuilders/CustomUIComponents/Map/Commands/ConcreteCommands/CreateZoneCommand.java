package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import com.vaadin.flow.component.notification.Notification;
import lombok.Setter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.enums.ButtonNames;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.enums.ClickedElement;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.enums.DialogsNames;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.Buttons;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.*;
import org.pinguweb.frontend.mapObjects.Zone;
import org.pinguweb.frontend.utils.Mediador.EventType;

public class CreateZoneCommand implements Command {
    Buttons buttonController;

    private boolean working = false;
    private boolean first = true;

    @Setter
    private Zone zone;

    public CreateZoneCommand(Buttons receiver){
        buttonController = receiver;
    }

    @Override
    public void execute() {
        if (!working) {
            buttonController.getZone().setText("Terminar zona");
            buttonController.getMediator().publish(new ButtonEvent<>(EventType.DISABLE_BUTTONS, ButtonNames.ZONE));
            buttonController.getMediator().publish(new RequestClickEvent<>(ClickedElement.ZONE));
            working = true;
        }
        else{
            buttonController.getZone().setText("Crear zona");
            buttonController.getMediator().publish(new ButtonEvent<>(EventType.ENABLE_BUTTONS,null));
            buttonController.getMediator().publish(new ShowEvent<>(EventType.SHOW_DIALOG, null, DialogsNames.ZONE, this));
            working = false;
        }
    }

    @Override
    public void endExecution(){
        if (first)
        {buttonController.addExecutedCommand(this); first = false;}
        buttonController.getMediator().publish(new ButtonEvent<>(EventType.ENABLE_BUTTONS,null));
        Notification notification = new Notification("Zona creada exitosamente", 3000);
        notification.open();
    }

    @Override
    public void undo() {
        buttonController.getMediator().publish(new DeleteEvent<>(this.zone.toDto(), this));
        Notification notification = new Notification("Creación de la zona deshecha", 3000);
        notification.open();
    }

    @Override
    public void redo() {
        buttonController.getMediator().publish(new CreationEvent<>(EventType.CREATE, zone.toDto(), this, null));
    }
}
