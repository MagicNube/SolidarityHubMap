package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.pingu.persistence.model.GPSCoordinates;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapButtons;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapService;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapState;
import org.pinguweb.frontend.mapObjects.RoutePoint;
import org.pinguweb.frontend.mapObjects.ZoneMarker;
import org.pinguweb.frontend.view.MapView;
import org.pinguweb.frontend.view.NavigationBar;
import org.yaml.snakeyaml.util.Tuple;
import software.xdev.vaadin.maps.leaflet.MapContainer;
import software.xdev.vaadin.maps.leaflet.layer.LLayer;
import software.xdev.vaadin.maps.leaflet.layer.LLayerGroup;
import software.xdev.vaadin.maps.leaflet.layer.raster.LTileLayer;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.map.LMapLocateOptions;
import software.xdev.vaadin.maps.leaflet.registry.LDefaultComponentManagementRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
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
    private MapService service;

    @Setter
    private MapState state;

    @Override
    public final Component getComponent() {
        return component;
    }

    public void loadView(){
        this.component = new VerticalLayout();
        this.component.setSizeFull();

        this.reg = new LDefaultComponentManagementRegistry(this.component);
        MapContainer mapContainer = new MapContainer(reg);
        mapContainer.setSizeFull();
        this.map = mapContainer.getlMap();

        LLayer<LTileLayer> layer = LTileLayer.createDefaultForOpenStreetMapTileServer(reg);
        this.map.addLayer(layer);

        //TODO: Agregar layers en función de lo que se requiera + guardarlas
        //generateLayers();

        this.map.locate(new LMapLocateOptions().withSetView(true).withMaxZoom(16));

        this.service = new MapService();
        this.service.setReg(reg);
        this.service.setMap(map);
        this.service.setID(MapView.getMapId());
        MapView.setMapService(this.service);

        component.add(mapContainer);
        component.add(new MapButtons(this.service, this).generateButtonRow());

        this.service.load();
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




}
