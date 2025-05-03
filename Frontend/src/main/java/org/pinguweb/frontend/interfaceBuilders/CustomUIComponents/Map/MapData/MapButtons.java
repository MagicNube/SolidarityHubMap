package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapData;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.Getter;

public class MapButtons {
    private final Button zone = new Button("Zona");
    private final Button route = new Button("Ruta");
    private final Button edit = new Button("Editar");
    private final Button delete = new Button("Borrar");

    @Getter
    private MapState mapState;
    private final MapDialogs mapDialogs;
    private final MapService controller;

    public MapButtons(HorizontalLayout layout, MapService controller) {
        layout.add(zone, route, edit, delete);
        this.controller = controller;

        this.mapState = MapState.IDLE;
        this.mapDialogs = new MapDialogs(this.controller);

        zone.addClickListener(event -> toggleZoneCreation());
        route.addClickListener(event -> toggleRouteCreation());
        //edit.addClickListener(event -> toggleEdit());
        //delete.addClickListener(event -> toggleDelete());
    }

    private void toggleZoneCreation() {
        if (mapState == MapState.IDLE) {
            mapState = MapState.CREATING_ZONE;
            mapDialogs.createDialogZona(mapState);
            this.zone.setText("Terminar zona");
        } else {
            mapState = MapState.IDLE;
            mapDialogs.createDialogZona(mapState);
            this.zone.setText("Zona");
        }
    }

    private void toggleRouteCreation() {
        if (mapState == MapState.IDLE) {
            mapState = MapState.CREATING_ROUTE;
            mapDialogs.createDialogRuta(mapState);
            this.route.setText("Terminar ruta");
        } else {
            mapState = MapState.IDLE;
            mapDialogs.createDialogRuta(mapState);
            this.route.setText("Ruta");
        }
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
