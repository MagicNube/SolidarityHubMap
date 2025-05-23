package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import com.vaadin.flow.component.notification.Notification;
import lombok.Setter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.ClickedElement;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.DialogsNames;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.MapButtons;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.CreationEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.DeleteEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.RequestClickEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.ShowEvent;
import org.pinguweb.frontend.mapObjects.Storage;
import org.pinguweb.frontend.utils.Mediador.EventType;

public class CreateStorageCommand implements Command {
    MapButtons buttonController;

    @Setter
    Storage storage;
    public CreateStorageCommand(MapButtons receiver){
        buttonController = receiver;
    }

    @Override
    public void execute() {
        buttonController.getMediator().publish(new RequestClickEvent<>(ClickedElement.STORAGE));
        buttonController.addExecutedCommand(this);
    }

    @Override
    public void undo() {
        buttonController.getMediator().publish(new DeleteEvent<>(this.storage, this));
        Notification notification = new Notification("Creación del almacén deshecha", 3000);
        notification.open();
    }

    @Override
    public void redo() {
        buttonController.getMediator().publish(new CreationEvent<>(EventType.CREATE, storage, this, null));
        Notification notification = new Notification("Almacén creado exitosamente", 3000);
        notification.open();
    }
}
