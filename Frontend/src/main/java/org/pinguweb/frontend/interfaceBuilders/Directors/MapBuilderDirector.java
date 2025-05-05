package org.pinguweb.frontend.interfaceBuilders.Directors;

import com.vaadin.flow.component.Component;
import org.pinguweb.frontend.interfaceBuilders.Builders.MapBuilder;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Map;

public class MapBuilderDirector {

    MapBuilder builder = new MapBuilder();

    public Component createFullMap() {
        Map map = Map.builder()
                .build();

        builder.reset();
        builder.addBelow(map);
        return builder.build().getInterface();
    }
}
