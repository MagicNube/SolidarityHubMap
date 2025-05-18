package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.Getter;
import lombok.Setter;

import com.vaadin.flow.component.icon.VaadinIcon;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.CommandButton;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.*;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Map;

@Setter
@Getter
public class MapButtons {
    private final CommandButton zone = new CommandButton("Crear zona");
    private final CommandButton route = new CommandButton("Crear ruta");
    private final CommandButton storage = new CommandButton("Crear Almacen");
    private final CommandButton edit = new CommandButton("Editar");
    private final CommandButton delete = new CommandButton("Borrar");
    private final Button undo = new Button(VaadinIcon.ARROW_BACKWARD.create());
    private final Button redo = new Button(VaadinIcon.ARROW_FORWARD.create());

    private final Map map;

    private final MapDialogs mapDialogs;
    private final MapBuild mapBuild;
    private final MapActions mapActions;

    public MapButtons(MapService service, Map map) {
        this.map = map;
        this.map.setState(MapState.IDLE);
        this.mapDialogs = new MapDialogs(service, this);
        this.mapBuild = new MapBuild(service);
        this.mapActions = new MapActions(service, this);

        zone.setCommand(new CreateZoneCommand(this.mapActions));
        route.setCommand(new CreateRouteCommand(this.mapActions));
        storage.setCommand(new CreateStorageCommand(this.mapActions));
        edit.setCommand(new EditCommand(this.mapActions));
        delete.setCommand(new DeleteCommand(this.mapActions));

        undo.addClickListener(e -> this.mapActions.undoCommand());
        redo.addClickListener(e -> this.mapActions.redoCommand());
        this.undo.setEnabled(!this.mapActions.getComandosRealizados().isEmpty());
        this.redo.setEnabled(!this.mapActions.getComandosDeshechos().isEmpty());
    }

    public void disableButtons(Button button) {
        this.zone.setEnabled(button == this.zone);
        this.route.setEnabled(button == this.route);
        this.storage.setEnabled(button == this.storage);
        this.edit.setEnabled(button == this.edit);
        this.delete.setEnabled(button == this.delete);
        this.undo.setEnabled(button == this.undo);
        this.redo.setEnabled(button == this.redo);
    }

    public void enableButtons() {
        this.zone.setEnabled(true);
        this.route.setEnabled(true);
        this.storage.setEnabled(true);
        this.edit.setEnabled(true);
        this.delete.setEnabled(true);
        this.undo.setEnabled(!this.mapActions.getComandosRealizados().isEmpty());
        this.redo.setEnabled(!this.mapActions.getComandosDeshechos().isEmpty());
    }

    public HorizontalLayout generateButtonRow() {
        HorizontalLayout hlayout = new HorizontalLayout();
        hlayout.add(this.zone,this.storage,this.route);

        if (hlayout.getChildren().findAny().isPresent()) {
            hlayout.add(this.edit, this.delete, undo, redo);
        }

        hlayout.setWidthFull();
        hlayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        return hlayout;
    }
}