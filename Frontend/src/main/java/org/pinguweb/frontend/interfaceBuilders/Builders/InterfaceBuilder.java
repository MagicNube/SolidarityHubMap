package org.pinguweb.frontend.interfaceBuilders.Builders;

import com.vaadin.flow.component.Component;
import lombok.NonNull;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;

import java.util.List;

public interface InterfaceBuilder {
    InterfaceBuilder reset();
    Component build();
    InterfaceBuilder setTile(@NonNull String title);
    InterfaceBuilder setSubtitle(@NonNull String subtitle);
    InterfaceBuilder addBelow(@NonNull InterfaceComponent component);
    InterfaceBuilder addSide(@NonNull List<InterfaceComponent> component);
}
