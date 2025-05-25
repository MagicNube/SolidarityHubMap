package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;

import java.util.Optional;

public class Utils {

    public static Optional<Component> findComponentById(Component rootComponent, String id) {
        if (rootComponent == null || id == null) {
            return Optional.empty();
        }

        for (Component child : rootComponent.getChildren().toList()) {
            if (id.equals(child.getId().orElse(null))) {
                return Optional.of(child);
            }

            if (child instanceof HasComponents) {
                Optional<Component> found = findComponentById(child, id);
                if (found.isPresent()) {
                    return found;
                }
            }
        }
        return Optional.empty();
    }
}
