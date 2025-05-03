package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import software.xdev.vaadin.maps.leaflet.MapContainer;
import software.xdev.vaadin.maps.leaflet.layer.LLayer;
import software.xdev.vaadin.maps.leaflet.layer.LLayerGroup;
import software.xdev.vaadin.maps.leaflet.layer.raster.LTileLayer;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LDefaultComponentManagementRegistry;

import java.util.ArrayList;
import java.util.List;

public class MapVisualizer extends VerticalLayout {

    private Map mapObject;
    private LDefaultComponentManagementRegistry reg;
    private LMap map;

    public MapVisualizer(Map mapObject){
      this.mapObject = mapObject;
    }

    public void loadView(){
        this.setSizeFull();
        this.setId("MapView");

        this.reg = new LDefaultComponentManagementRegistry(this);
        MapContainer mapContainer = new MapContainer(reg);
        mapContainer.setSizeFull();
        this.map = mapContainer.getlMap();

        LLayer<LTileLayer> layer = LTileLayer.createDefaultForOpenStreetMapTileServer(reg);
        this.map.addLayer(layer);

        //TODO: Agregar layers en función de lo que se requiera + guardarlas
        generateLayers();

        this.add(mapContainer, generateButtonRow());
    }

    private void generateLayers(){
        List<LLayerGroup> layers = new ArrayList<>();

        if(mapObject.canSeeNeeds){
            layers.add(new LLayerGroup(this.reg));
        }
        if(mapObject.canSeeRoutes){
            layers.add(new LLayerGroup(this.reg));
        }
        if(mapObject.canSeeStorages){
            layers.add(new LLayerGroup(this.reg));
        }
        if(mapObject.canSeeRoutes){
            layers.add(new LLayerGroup(this.reg));
        }


        for(LLayerGroup layer : layers){
            this.map.addLayer(layer);
            //TODO: AddControls?
        }
    }

    private HorizontalLayout generateButtonRow(){
        HorizontalLayout hlayout = new HorizontalLayout();

        if (mapObject.canCreateNeeds){
            hlayout.add(new Button("Crear Necesidad"));
        }
        if (mapObject.canCreateZones){
            hlayout.add(new Button("Crear Zona"));
        }
        if (mapObject.canCreateStorages){
            hlayout.add(new Button("Crear Almacen"));
        }
        if (mapObject.canCreateRoutes){
            hlayout.add(new Button("Crear Ruta"));
        }

        if (hlayout.getChildren().findAny().isPresent()){
            hlayout.add(new Button("Editar"), new Button("Borrar"));
        }

        return hlayout;
    }
}
