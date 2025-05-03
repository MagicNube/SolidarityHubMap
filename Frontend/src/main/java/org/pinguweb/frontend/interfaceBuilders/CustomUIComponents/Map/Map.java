package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.experimental.SuperBuilder;
import org.pingu.persistence.model.GPSCoordinates;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;
import software.xdev.vaadin.maps.leaflet.MapContainer;
import software.xdev.vaadin.maps.leaflet.layer.LLayer;
import software.xdev.vaadin.maps.leaflet.layer.LLayerGroup;
import software.xdev.vaadin.maps.leaflet.layer.raster.LTileLayer;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LDefaultComponentManagementRegistry;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
public class Map extends InterfaceComponent {

    protected final boolean canCreateNeeds;
    protected final boolean canCreateZones;
    protected final boolean canCreateStorages;
    protected final boolean canCreateRoutes;

    protected final boolean canSeeNeeds;
    protected final boolean canSeeZones;
    protected final boolean canSeeStorages;
    protected final boolean canSeeRoutes;

    protected final GPSCoordinates startingPosition;

    private LDefaultComponentManagementRegistry reg;
    private LMap map;

    private VerticalLayout component;

    @Override
    public final Component getComponent() {
        return component;
    }

    public void loadView(){
        this.component = new VerticalLayout();
        this.component.setSizeFull();
        this.component.setId("MapView");

        this.reg = new LDefaultComponentManagementRegistry(this.component);
        MapContainer mapContainer = new MapContainer(reg);
        mapContainer.setSizeFull();
        this.map = mapContainer.getlMap();

        LLayer<LTileLayer> layer = LTileLayer.createDefaultForOpenStreetMapTileServer(reg);
        this.map.addLayer(layer);

        //TODO: Agregar layers en función de lo que se requiera + guardarlas
        generateLayers();

        this.component.add(mapContainer, generateButtonRow());
    }

    private void generateLayers(){
        List<LLayerGroup> layers = new ArrayList<>();

        if(this.canSeeNeeds){
            layers.add(new LLayerGroup(this.reg));
        }
        if(this.canSeeRoutes){
            layers.add(new LLayerGroup(this.reg));
        }
        if(this.canSeeStorages){
            layers.add(new LLayerGroup(this.reg));
        }
        if(this.canSeeRoutes){
            layers.add(new LLayerGroup(this.reg));
        }

        for(LLayerGroup layer : layers){
            this.map.addLayer(layer);
            //TODO: AddControls?
        }
    }

    private HorizontalLayout generateButtonRow(){
        HorizontalLayout hlayout = new HorizontalLayout();

        if (this.canCreateNeeds){
            hlayout.add(new Button("Crear Necesidad"));
        }
        if (this.canCreateZones){
            hlayout.add(new Button("Crear Zona"));
        }
        if (this.canCreateStorages){
            hlayout.add(new Button("Crear Almacen"));
        }
        if (this.canCreateRoutes){
            hlayout.add(new Button("Crear Ruta"));
        }

        if (hlayout.getChildren().findAny().isPresent()){
            hlayout.add(new Button("Editar"), new Button("Borrar"));
        }

        return hlayout;
    }
}
