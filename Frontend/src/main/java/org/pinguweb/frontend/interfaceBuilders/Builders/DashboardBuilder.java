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
public class DashboardBuilder implements InterfaceBuilder{

    private Interface interfaz = new Interface();

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

        if (component instanceof Dashboard){
           Hr separator = new Hr();
           separator.setWidthFull();
           separator.addClassName("separator");
           interfaz.addComponent(separator);
        }
    }

    @Override
    public void addSide(@NonNull  List<InterfaceComponent> component) {
        InterfaceComponent first = component.get(0);

        for (int i = 1; i < component.size(); i++){
            first.addSideComponent(component.get(i));
        }

        interfaz.addComponent(getHorizontalLayout(first));

        if (component instanceof Dashboard){
           Hr separator = new Hr();
           separator.setWidthFull();
           separator.addClassName("separator");
           interfaz.addComponent(separator);
        }
    }

    private Component[] getComponent(InterfaceComponent component){
        if (component instanceof Dashboard){
            return new Component[]{new H3(component.getName()), component.getComponent()};
        }
        else{
            return new Component[]{component.getComponent()};
        }
    }

    private HorizontalLayout getHorizontalLayout(InterfaceComponent component) {
        HorizontalLayout side = new HorizontalLayout();
        VerticalLayout mainDown = new VerticalLayout();

        side.setSizeFull();
        mainDown.setSizeFull();
        mainDown.add(getComponent(component));

        for (InterfaceComponent insideComponent : component.getBelowComponents()){
            mainDown.add(this.getComponent(insideComponent));
        }

        side.add(mainDown);

        for (InterfaceComponent sizeComponents : component.getSideComponents()){
            VerticalLayout compDown = new VerticalLayout();
            compDown.add(getComponent(sizeComponents));
            for (InterfaceComponent insideComponent : sizeComponents.getBelowComponents()){
                compDown.add(this.getComponent(insideComponent));
            }
            side.add(compDown);
        }

        return side;
    }
}