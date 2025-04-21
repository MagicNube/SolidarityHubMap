package org.pinguweb.frontend.interfaceBuilders.customUIComponents;

import com.vaadin.flow.component.Component;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class InterfaceComponent extends Component {
    private final List<InterfaceComponent> sideComponents = new ArrayList<>();

    private final List<InterfaceComponent> belowComponents = new ArrayList<>();

    public void addSideComponent(InterfaceComponent component){
        sideComponents.add(component);
    }

    public void addBelowComponent(InterfaceComponent component){
        belowComponents.add(component);
    }
}
