package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.Getter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Map;

public class MapButtons {
    private final Button zone = new Button("Crear zona");
    private final Button route = new Button("Crear ruta");
    private final Button storage = new Button("Crear Almacen");
    private final Button edit = new Button("Editar");
    private final Button delete = new Button("Borrar");

    private final Map map;

    private final MapDialogs mapDialogs;
    private final MapBuild mapBuild;

    public MapButtons(MapService service, Map map) {
        this.map = map;
        this.map.setState(MapState.IDLE);
        this.mapDialogs = new MapDialogs(service, this);
        this.mapBuild = new MapBuild(service);

        zone.addClickListener(event -> {
            disableButtons(zone);
            toggleZoneCreation();
            if (map.getState() == MapState.IDLE) {
                enableButtons();
            }
        });

        route.addClickListener(event -> {
            disableButtons(route);
            toggleRouteCreation();
            if (map.getState() == MapState.IDLE) {
                enableButtons();
            }
        });

        edit.addClickListener(event -> {
            disableButtons(edit);
            toggleEdit();
            if (map.getState() == MapState.IDLE) {
                enableButtons();
            }
        });

        delete.addClickListener(event -> {
            disableButtons(delete);
            toggleDelete();
            if (map.getState() == MapState.IDLE) {
                enableButtons();
            }
        });
    }


    private void toggleZoneCreation() {
        if (this.map.getState() == MapState.IDLE) {
            this.map.setState(MapState.CREATING_ZONE);
            mapDialogs.createDialogZona(this.map.getState());
            this.zone.setText("Terminar zona");
        } else {
            this.map.setState(MapState.IDLE);
            mapDialogs.createDialogZona(this.map.getState());
            this.zone.setText("Crear zona");
        }
    }

    public void cancelZoneCreation() {
        this.map.setState(MapState.IDLE);
        System.out.println("Cancelando zona");
        this.zone.setText("Crear zona");
        enableButtons();
    }

    private void toggleRouteCreation() {
        if (this.map.getState() == MapState.IDLE) {
            this.map.setState(MapState.CREATING_ROUTE);
            mapDialogs.createDialogRuta(this.map.getState());
            this.route.setText("Terminar ruta");
        } else {
            this.map.setState(MapState.IDLE);
            mapDialogs.createDialogRuta(this.map.getState());
            this.route.setText("Crear ruta");
        }
    }

    public void cancelRouteCreation() {
        this.map.setState(MapState.IDLE);
        this.route.setText("Crear ruta");
        enableButtons();
    }

    private void toggleEdit() {
        if (this.map.getState() == MapState.IDLE) {
            this.map.setState(MapState.EDITING);
            mapBuild.startEdit();
            this.edit.setText("Terminar edición");
        } else {
            this.map.setState(MapState.IDLE);
            mapBuild.endEdit();
            this.edit.setText("Editar");
        }
    }

    private void toggleDelete() {
        if (this.map.getState() == MapState.IDLE) {
            this.map.setState(MapState.DELETING);
            mapBuild.startDelete();
            this.delete.setText("Terminar borrado");
        } else {
            this.map.setState(MapState.IDLE);
            mapBuild.endDelete();
            this.delete.setText("Borrar");
        }
    }

    public void disableButtons(Button button) {
        this.zone.setEnabled(button == this.zone);
        this.route.setEnabled(button == this.route);
        this.storage.setEnabled(button == this.storage);
        this.edit.setEnabled(button == this.edit);
        this.delete.setEnabled(button == this.delete);
    }

    public void enableButtons() {
        this.zone.setEnabled(true);
        this.route.setEnabled(true);
        this.storage.setEnabled(true);
        this.edit.setEnabled(true);
        this.delete.setEnabled(true);
    }


    public HorizontalLayout generateButtonRow(){
        HorizontalLayout hlayout = new HorizontalLayout();

        if (this.map.isCanCreateZones()){
            hlayout.add(this.zone);
        }
        if (this.map.isCanCreateStorages()){
            hlayout.add(this.storage);
        }
        if (this.map.isCanCreateRoutes()){
            hlayout.add(this.route);
        }

        if (hlayout.getChildren().findAny().isPresent()){
            hlayout.add(this.edit, this.delete);
        }

        hlayout.setWidthFull();
        hlayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        return hlayout;
    }

    /*private void toggleEdit() {
        if (mapState == MapState.IDLE) {
            mapState = MapState.EDITING;
            mapDialogs.createDialogEditar();
        } else {
            mapState = MapState.IDLE;
            mapDialogs.createDialogEditar();
        }
    }*/

    /*private void toggleDelete() {
        if (mapState == MapState.IDLE) {
            mapState = MapState.DELETING;
            mapDialogs.createDialogBorrar();
        } else {
            mapState = MapState.IDLE;
            mapDialogs.createDialogBorrar();
        }
    }*/
}
