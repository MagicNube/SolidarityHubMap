package org.pinguweb.frontend.interfaceBuilders.Directors;

import com.vaadin.flow.component.Component;
import org.pinguweb.frontend.interfaceBuilders.Builders.MapBuilder;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.GenericComponent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Map;
import org.pinguweb.frontend.view.InfoPopup;
import org.pinguweb.frontend.view.MapView;

public class MapBuilderDirector {

    MapBuilder builder = new MapBuilder();

    public Component createFullMap(MapView vista) {
        Map map = Map.builder()
                .build();

        vista.setMap(map);
        vista.setMediator(map);

        builder.reset();
        builder.addBelow(map);
        builder.addBelow(GenericComponent.builder().component(InfoPopup.MapPopup()).build());
        return builder.build().getInterface();
    }
}
