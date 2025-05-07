package org.pinguweb.frontend.interfaceBuilders.Builders;

import lombok.NonNull;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;

import java.util.List;

public interface InterfaceBuilder {
    void reset();
    Interface build();
    void setTile(@NonNull String title);
    void setSubtitle(@NonNull String subtitle);
    void addBelow(@NonNull InterfaceComponent component);
    void addSide(@NonNull List<InterfaceComponent> component);
}
