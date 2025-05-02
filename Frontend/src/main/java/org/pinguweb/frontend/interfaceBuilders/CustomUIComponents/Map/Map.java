package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map;

import com.vaadin.flow.component.Component;
import lombok.experimental.SuperBuilder;
import org.pingu.persistence.model.GPSCoordinates;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;

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

    @Override
    public Component getComponent() {
        return null;
    }
}
