package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Getter
public abstract class InterfaceComponent {
    private final List<InterfaceComponent> sideComponents = new ArrayList<>();
    private final List<InterfaceComponent> belowComponents = new ArrayList<>();
    protected String name;

    public abstract Component getComponent();

    public void addSideComponent(InterfaceComponent component){
        sideComponents.add(component);
    }

    public void addBelowComponent(InterfaceComponent component){
        belowComponents.add(component);
    }
}
