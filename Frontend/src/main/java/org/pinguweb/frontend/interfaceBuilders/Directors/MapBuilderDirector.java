package org.pinguweb.frontend.interfaceBuilders.Directors;

import com.vaadin.flow.component.Component;
import org.pinguweb.frontend.interfaceBuilders.Builders.MapBuilder;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Map;

public class MapBuilderDirector {

    MapBuilder builder = new MapBuilder();

    public Component createFullMap(){
        Map map = Map.builder()
                .canCreateNeeds(true)
                .canSeeNeeds(true)
                .canCreateRoutes(true)
                .canSeeRoutes(true)
                .canCreateStorages(true)
                .canSeeStorages(true)
                .canCreateZones(true)
                .canSeeNeeds(true)
                .build();

        builder.reset();
        builder.addBelow(map);
        return builder.build().getInterface();
    }
}
