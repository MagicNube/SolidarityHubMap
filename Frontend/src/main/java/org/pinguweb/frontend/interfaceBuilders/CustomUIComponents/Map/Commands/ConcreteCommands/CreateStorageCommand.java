package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import com.vaadin.flow.component.notification.Notification;
import lombok.Setter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.enums.ClickedElement;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.MapButtons;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.ButtonEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.CreationEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.DeleteEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.RequestClickEvent;
import org.pinguweb.frontend.mapObjects.Storage;
import org.pinguweb.frontend.utils.Mediador.EventType;

public class CreateStorageCommand implements Command {
    MapButtons buttonController;

    private boolean first = true;
    @Setter
    Storage storage;
    public CreateStorageCommand(MapButtons receiver){
        buttonController = receiver;
    }

    @Override
    public void execute() {
        buttonController.getMediator().publish(new ButtonEvent<>(EventType.DISABLE_BUTTONS,null));
        buttonController.getMediator().publish(new RequestClickEvent<>(ClickedElement.STORAGE, this));
    }

    @Override
    public void endExecution(){
        if (first)
        {buttonController.addExecutedCommand(this); first = false;}
        buttonController.getMediator().publish(new ButtonEvent<>(EventType.ENABLE_BUTTONS,null));
        Notification notification = new Notification("Almacén creado exitosamente", 3000);
        notification.open();
    }

    @Override
    public void undo() {
        buttonController.getMediator().publish(new DeleteEvent<>(this.storage.toDto(), this));
        Notification notification = new Notification("Creación del almacén deshecha", 3000);
        notification.open();
    }

    @Override
    public void redo() {
        buttonController.getMediator().publish(new CreationEvent<>(EventType.CREATE, storage.toDto(), this, null));
        Notification notification = new Notification("Almacén creado exitosamente", 3000);
        notification.open();
    }
}
