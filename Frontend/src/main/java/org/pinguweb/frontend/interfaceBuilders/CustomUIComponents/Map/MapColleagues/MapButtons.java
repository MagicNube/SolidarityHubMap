package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.Getter;
import lombok.Setter;

import com.vaadin.flow.component.icon.VaadinIcon;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.CommandButton;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.*;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Map;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.ButtonEvent;
import org.pinguweb.frontend.utils.Mediador.Colleague;
import org.pinguweb.frontend.utils.Mediador.ComponentColleague;
import org.pinguweb.frontend.utils.Mediador.Event;
import org.pinguweb.frontend.utils.Mediador.EventType;

import java.util.LinkedList;

@Setter
@Getter
public class MapButtons extends ComponentColleague {
    private final CommandButton zone = new CommandButton("Crear zona");
    private final CommandButton route = new CommandButton("Crear ruta");
    private final CommandButton storage = new CommandButton("Crear Almacen");
    private final CommandButton edit = new CommandButton("Editar");
    private final CommandButton delete = new CommandButton("Borrar");
    private final Button undo = new Button(VaadinIcon.ARROW_BACKWARD.create());
    private final Button redo = new Button(VaadinIcon.ARROW_FORWARD.create());

    @Getter
    private final LinkedList<Command> comandosRealizados = new LinkedList<>();
    @Getter
    private final LinkedList<Command> comandosDeshechos = new LinkedList<>();

    private Map map;

    public MapButtons(Map map) {
        super(map);

        this.map = map;
        zone.setCommand(new CreateZoneCommand(this));
        route.setCommand(new CreateRouteCommand(this));
        storage.setCommand(new CreateStorageCommand(this));
        edit.setCommand(new EditCommand(this));
        delete.setCommand(new DeleteCommand(this));

        undo.addClickListener(e -> this.undoCommand());
        redo.addClickListener(e -> this.redoCommand());
        this.undo.setEnabled(!this.getComandosRealizados().isEmpty());
        this.redo.setEnabled(!this.getComandosDeshechos().isEmpty());
    }

    @Override
    public void register() {
        mediator.subscribe(EventType.ENABLE_BUTTONS, this);
        mediator.subscribe(EventType.DISABLE_BUTTONS, this);
        mediator.subscribe(EventType.EXIT, this);
    }

    @Override
    public <T> void receive(Event<T> event) {
        if (event.getType() == EventType.ENABLE_BUTTONS){
            enableButtons();
        }
        else if (event.getType() == EventType.DISABLE_BUTTONS){
            disableButtons((ButtonNames) event.getPayload());
        }
        else if (event.getType() == EventType.EXIT){
            map.setState(MapState.IDLE);
            enableButtons();
        }
    }

    public void disableButtons(ButtonNames name) {
        this.zone.setEnabled(name == ButtonNames.ZONE);
        this.route.setEnabled(name == ButtonNames.ROUTE);
        this.storage.setEnabled(name == ButtonNames.STORAGE);
        this.edit.setEnabled(name == ButtonNames.EDIT);
        this.delete.setEnabled(name == ButtonNames.DELETE);
        this.undo.setEnabled(name == ButtonNames.UNDO);
        this.redo.setEnabled(name == ButtonNames.REDO);
    }

    public void enableButtons() {
        this.zone.setEnabled(true);
        this.route.setEnabled(true);
        this.storage.setEnabled(true);
        this.edit.setEnabled(true);
        this.delete.setEnabled(true);
        this.undo.setEnabled(!this.getComandosRealizados().isEmpty());
        this.redo.setEnabled(!this.getComandosDeshechos().isEmpty());
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

    public void addExecutedCommand(Command c){
        comandosDeshechos.clear();
        comandosRealizados.push(c);
    }

    public void undoCommand(){
        Command c = comandosRealizados.poll();
        if (c == null) {return;}
        c.undo();
        comandosDeshechos.push(c);
        enableButtons();
    }

    public void redoCommand(){
        Command c = comandosDeshechos.poll();
        if (c == null) {return;}
        c.redo();
        comandosRealizados.push(c);
        enableButtons();
    }

}