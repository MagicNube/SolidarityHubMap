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
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Dashboard;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;

import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
public class DashboardBuilder implements InterfaceBuilder{

    private String title = "Page Title";
    private String subtitle = "Page subtitle";
    private final List<InterfaceComponent> childrens = new LinkedList<>();

    @Override
    public InterfaceBuilder reset() {
        return new DashboardBuilder();
    }

    @Override
    public Component build() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        H1 Title = new H1(this.title);
        H2 Subtitle = new H2(this.subtitle);

        layout.add(Title, Subtitle);

        for (InterfaceComponent component : childrens){

            if (!component.getSideComponents().isEmpty()){
                VerticalLayout vmain = new VerticalLayout();
                HorizontalLayout side = getHorizontalLayout(component);
                vmain.add(side);
                layout.add(vmain);
            }
            else{
                layout.add(getComponent(component));
            }

            Hr separator = new Hr();
            separator.setWidthFull();
            separator.addClassName("separator");
            layout.add(separator);
        }

        return layout;
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