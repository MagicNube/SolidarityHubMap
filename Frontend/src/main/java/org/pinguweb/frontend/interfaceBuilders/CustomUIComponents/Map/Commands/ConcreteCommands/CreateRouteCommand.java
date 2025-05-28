package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands;

import com.vaadin.flow.component.notification.Notification;
import lombok.Setter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.Buttons;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.enums.ButtonNames;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.enums.ClickedElement;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.enums.DialogsNames;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.*;
import org.pinguweb.frontend.mapObjects.Route;
import org.pinguweb.frontend.mapObjects.RoutePoint;
import org.pinguweb.frontend.utils.Mediador.EventType;

import java.util.List;

public class CreateRouteCommand implements Command{
    Buttons buttonController;
    private boolean working = false;
    private boolean first = true;

    @Setter
    Route route;

    @Setter
    List<RoutePoint> points;
    public CreateRouteCommand(Buttons receiver){
        buttonController = receiver;
    }

    @Override
    public void execute() {
        if (!working) {
            buttonController.getRoute().setText("Terminar ruta");
            buttonController.getMediator().publish(new ButtonEvent<>(EventType.DISABLE_BUTTONS, ButtonNames.ROUTE));
            buttonController.getMediator().publish(new RequestClickEvent<>(ClickedElement.ROUTE_POINT));
            working = true;
        }
        else{
            buttonController.getRoute().setText("Crear ruta");
            buttonController.getMediator().publish(new ButtonEvent<>(EventType.ENABLE_BUTTONS,null));
            buttonController.getMediator().publish(new ShowEvent<>(EventType.SHOW_DIALOG, null, DialogsNames.ROUTE, this));
            working = false;
        }
    }

    @Override
    public void endExecution(){
        if (first)
        {buttonController.addExecutedCommand(this); first = false;}
        buttonController.getMediator().publish(new ButtonEvent<>(EventType.ENABLE_BUTTONS,null));
        Notification notification = new Notification("Ruta creada exitosamente", 3000);
        notification.open();
    }

    @Override
    public void undo() {
        buttonController.getMediator().publish(new DeleteEvent<>(this.route.toDto(), this));
        Notification notification = new Notification("Creación de la ruta deshecha", 3000);
        notification.open();
    }

    @Override
    public void redo() {
        buttonController.getMediator().publish(new CreationEvent<>(EventType.CREATE, route.toDto(), this, points));
        Notification notification = new Notification("Ruta creada exitosamente", 3000);
        notification.open();
    }
}
