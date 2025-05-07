package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.pingu.persistence.model.GPSCoordinates;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapButtons;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapService;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses.MapState;
import org.pinguweb.frontend.view.MapView;
import software.xdev.vaadin.maps.leaflet.MapContainer;
import software.xdev.vaadin.maps.leaflet.layer.LLayer;
import software.xdev.vaadin.maps.leaflet.layer.raster.LTileLayer;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.map.LMapLocateOptions;
import software.xdev.vaadin.maps.leaflet.registry.LDefaultComponentManagementRegistry;

@Getter
@SuperBuilder
public class Map extends InterfaceComponent {

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

    public void loadView() {
        this.component = new VerticalLayout();
        this.component.setSizeFull();

        this.reg = new LDefaultComponentManagementRegistry(this.component);
        MapContainer mapContainer = new MapContainer(reg);
        mapContainer.setSizeFull();
        this.map = mapContainer.getlMap();

        LLayer<LTileLayer> layer = LTileLayer.createDefaultForOpenStreetMapTileServer(reg);
        this.map.addLayer(layer);

        this.map.locate(new LMapLocateOptions().withSetView(true).withMaxZoom(16));

        this.service = new MapService();
        this.service.setReg(reg);
        this.service.setMap(map);
        this.service.setID(MapView.getMapId());
        MapView.setMapService(this.service);

        component.add(mapContainer);
        component.add(new MapButtons(this.service, this).generateButtonRow());

    }







}
