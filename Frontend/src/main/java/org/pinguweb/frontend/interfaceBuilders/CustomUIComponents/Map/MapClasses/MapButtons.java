package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.Getter;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Map;

public class MapButtons {
    private final Button zone = new Button("Zona");
    private final Button route = new Button("Ruta");
    private final Button edit = new Button("Editar");
    private final Button delete = new Button("Borrar");

    private final Map map;

    private final MapDialogs mapDialogs;

    public MapButtons(MapService service, Map map) {
        this.map = map;
        this.map.setState(MapState.IDLE);
        this.mapDialogs = new MapDialogs(service);

        zone.addClickListener(event -> toggleZoneCreation());
        route.addClickListener(event -> toggleRouteCreation());
        //edit.addClickListener(event -> toggleEdit());
        //delete.addClickListener(event -> toggleDelete());
    }

    private void toggleZoneCreation() {
        if (this.map.getState() == MapState.IDLE) {
            this.map.setState(MapState.CREATING_ZONE);
            mapDialogs.createDialogZona(this.map.getState());
            this.zone.setText("Terminar zona");
        } else {
            this.map.setState(MapState.IDLE);
            mapDialogs.createDialogZona(this.map.getState());
            this.zone.setText("Zona");
        }
    }

    private void toggleRouteCreation() {
        if (this.map.getState() == MapState.IDLE) {
            this.map.setState(MapState.CREATING_ROUTE);
            mapDialogs.createDialogRuta(this.map.getState());
            this.route.setText("Terminar ruta");
        } else {
            this.map.setState(MapState.IDLE);
            mapDialogs.createDialogRuta(this.map.getState());
            this.route.setText("Ruta");
        }
    }

    public HorizontalLayout generateButtonRow(){
        HorizontalLayout hlayout = new HorizontalLayout();

        if (this.map.isCanCreateNeeds()){
            hlayout.add(new Button("Crear Necesidad"));
        }
        if (this.map.isCanCreateZones()){
            hlayout.add(this.zone);
        }
        if (this.map.isCanCreateStorages()){
            hlayout.add(new Button("Crear Almacen"));
        }
        if (this.map.isCanCreateRoutes()){
            hlayout.add(this.route);
        }

        if (hlayout.getChildren().findAny().isPresent()){
            hlayout.add(this.edit, this.delete);
        }

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
