package org.pinguweb.frontend.interfaceBuilders.Builders;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard.Dashboard;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;

import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
public class MapBuilder implements InterfaceBuilder{

    private String title = "Page Title";
    private String subtitle = "Page subtitle";
    private final List<InterfaceComponent> childrens = new LinkedList<>();

    @Override
    public InterfaceBuilder reset() {
        return new MapBuilder();
    }

    @Override
    public Component build() {
        return null;
    }

    @Override
    public InterfaceBuilder setTile(@NonNull String title) {
        this.title = title;
        return this;
    }

    @Override
    public InterfaceBuilder setSubtitle(@NonNull String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    @Override
    public InterfaceBuilder addBelow(@NonNull InterfaceComponent component) {
        this.childrens.add(component);
        return this;
    }

    @Override
    public InterfaceBuilder addSide(@NonNull  List<InterfaceComponent> component) {
        InterfaceComponent first = component.get(0);

        for (int i = 1; i < component.size(); i++){
            first.addSideComponent(component.get(i));
        }
        this.childrens.add(first);

        return this;
    }

    private Component[] getComponent(InterfaceComponent component){
        return new Component[]{component.getComponent()};
    }
}