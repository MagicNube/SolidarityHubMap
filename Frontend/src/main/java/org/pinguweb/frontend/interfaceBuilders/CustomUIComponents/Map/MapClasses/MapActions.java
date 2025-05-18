package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses;

import lombok.Getter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.EditCommand;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.CreateRouteCommand;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.CreateStorageCommand;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.CreateZoneCommand;
import org.pinguweb.frontend.mapObjects.Route;
import org.pinguweb.frontend.mapObjects.Storage;
import org.pinguweb.frontend.mapObjects.Zone;

import java.util.LinkedList;


public class MapActions {
    @Getter
    private final MapService service;
    private final MapButtons buttons;
    @Getter
    private final LinkedList<Command> comandosRealizados = new LinkedList<>();
    @Getter
    private final LinkedList<Command> comandosDeshechos = new LinkedList<>();

    public MapActions(MapService service, MapButtons buttons){
        this.buttons = buttons;
        this.service = service;
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
    }

    public void redoCommand(){
        Command c = comandosDeshechos.poll();
        if (c == null) {return;}
        c.redo();
        comandosRealizados.push(c);
    }

    public void toggleZoneCreation(CreateZoneCommand c) {
        this.buttons.disableButtons(this.buttons.getZone());

        if (buttons.getMap().getState() == MapState.IDLE) {
            buttons.getMap().setState(MapState.CREATING_ZONE);
            this.buttons.getMapDialogs().createDialogZona(buttons.getMap().getState(), c);
            this.buttons.getZone().setText("Terminar zona");
        } else {
            buttons.getMap().setState(MapState.IDLE);
            this.buttons.getMapDialogs().createDialogZona(buttons.getMap().getState(), c);
            this.buttons.getZone().setText("Crear zona");
        }

        if (this.buttons.getMap().getState() == MapState.IDLE) {
            this.buttons.enableButtons();
        }
    }

    public void cancelZoneCreation() {
        buttons.getMap().setState(MapState.IDLE);
        System.out.println("Cancelando zona");
        this.buttons.getZone().setText("Crear zona");
        this.buttons.enableButtons();
    }

    public void toggleRouteCreation(CreateRouteCommand c) {
        this.buttons.disableButtons(this.buttons.getRoute());

        if (buttons.getMap().getState() == MapState.IDLE) {
            buttons.getMap().setState(MapState.CREATING_ROUTE);
            this.buttons.getMapDialogs().createDialogRuta(buttons.getMap().getState(), c);
            this.buttons.getRoute().setText("Terminar ruta");
        } else {
            buttons.getMap().setState(MapState.IDLE);
            this.buttons.getMapDialogs().createDialogRuta(buttons.getMap().getState(), c);
            this.buttons.getRoute().setText("Crear ruta");
        }

        if (this.buttons.getMap().getState() == MapState.IDLE) {
            this.buttons.enableButtons();
        }
    }

    public void cancelRouteCreation() {
        buttons.getMap().setState(MapState.IDLE);
        this.buttons.getRoute().setText("Crear ruta");
        this.buttons.enableButtons();
    }

    public void toggleStorageCreation(CreateStorageCommand c) {
        this.buttons.disableButtons(null);

        if (buttons.getMap().getState() == MapState.IDLE) {
            buttons.getMap().setState(MapState.CREATING_STORAGE);
            this.buttons.getMapDialogs().createDialogAlmacen(buttons.getMap().getState(), this.buttons, c);
        }
    }

    public void cancelStorageCreation() {
        buttons.getMap().setState(MapState.IDLE);
        this.buttons.getStorage().setText("Crear almacen");
        this.buttons.enableButtons();
    }

    public void toggleEdit(EditCommand c) {
        this.buttons.disableButtons(this.buttons.getEdit());

        if (buttons.getMap().getState() == MapState.IDLE) {
            buttons.getMap().setState(MapState.EDITING);
            this.buttons.getMapBuild().startEdit();
            this.buttons.getEdit().setText("Terminar edición");
        } else {
            buttons.getMap().setState(MapState.IDLE);
            this.buttons.getMapBuild().endEdit(c);
            this.buttons.getEdit().setText("Editar");
        }

        if (this.buttons.getMap().getState() == MapState.IDLE) {
            this.buttons.enableButtons();
        }
    }

    public void toggleDelete() {
        this.buttons.disableButtons(this.buttons.getDelete());

        if (buttons.getMap().getState() == MapState.IDLE) {
            buttons.getMap().setState(MapState.DELETING);
            this.buttons.getMapBuild().startDelete();
            this.buttons.getDelete().setText("Terminar borrado");
        } else {
            buttons.getMap().setState(MapState.IDLE);
            this.buttons.getMapBuild().endDelete();
            this.buttons.getDelete().setText("Borrar");
        }

        if (this.buttons.getMap().getState() == MapState.IDLE) {
            this.buttons.enableButtons();
        }
    }

    public void deleteZone(Zone zone){
        service.deleteZone(zone.getID());
    }

    public void deleteStorage(Storage storage){
        service.deleteZone(storage.getID());
    }

    public void deleteRoute(Route route){
        service.deleteRoute(route.getID());
    }
}
