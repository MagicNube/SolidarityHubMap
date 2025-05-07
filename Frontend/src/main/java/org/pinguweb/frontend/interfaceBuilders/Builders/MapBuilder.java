package org.pinguweb.frontend.interfaceBuilders.Builders;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Map;

import java.util.List;

@NoArgsConstructor
public class MapBuilder implements InterfaceBuilder{

    private final Interface interfaz = new Interface();

    @Override
    public void reset() {
        interfaz.reset();
    }

    @Override
    public Interface build() {
        return interfaz;
    }

    @Override
    public void setTile(@NonNull String title) {
        interfaz.addComponent(new H1(title));
    }

    @Override
    public void setSubtitle(@NonNull String subtitle) {
        interfaz.addComponent(new H2(subtitle));
    }

    @Override
    public void addBelow(@NonNull InterfaceComponent component) {
        interfaz.addComponent(getComponent(component));
    }

    @Override
    public void addSide(@NonNull  List<InterfaceComponent> component) {

    }

    private Component[] getComponent(InterfaceComponent component){
        if (component instanceof Map m){
            m.loadView();
            return new Component[]{m.getComponent()};
        }
        else {
            return new Component[]{component.getComponent()};
        }
    }
}