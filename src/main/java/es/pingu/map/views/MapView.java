package es.pingu.map.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.pingu.map.commons.NavigationBar;
import es.pingu.map.controllers.MapController;
import software.xdev.vaadin.maps.leaflet.MapContainer;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;
import software.xdev.vaadin.maps.leaflet.registry.LDefaultComponentManagementRegistry;

@Route("map")
@PageTitle("Visor del mapa")
public class MapView extends HorizontalLayout {

    MapController controller;

    public MapView() {
        this.setSizeFull();
        this.add(NavigationBar.createNavBar());

        VerticalLayout MapVerticalLayout = new VerticalLayout();
        HorizontalLayout ButtonLayout = new HorizontalLayout();

        this.add(MapVerticalLayout);

        final LComponentManagementRegistry reg = new LDefaultComponentManagementRegistry(this);
        final MapContainer mapContainer = new MapContainer(reg);
        mapContainer.setSizeFull();
        final LMap map = mapContainer.getlMap();

        MapVerticalLayout.add(mapContainer);
        MapVerticalLayout.add(ButtonLayout);

        Button tarea = new Button("Tarea");
        Button zona = new Button("Zona");
        ButtonLayout.add(tarea, zona);

        controller = new MapController(reg, map, 39.47, -0.42);

        tarea.addClickListener(e -> controller.createTask());
        zona.addClickListener(e -> controller.createZone());
    }
}
