package org.pinguweb.frontend.interfaceBuilders.Builders;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Interface {

    private VerticalLayout layout;

    protected Interface(){
        reset();
    }

    protected void reset(){
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        this.layout = layout;
    }

    protected void addComponent(Component[] component){
        this.layout.add(component);
    }

    protected void addComponent(Component component){
        this.layout.add(component);
    }

    public HorizontalLayout getInterface(){
        HorizontalLayout hl = new HorizontalLayout();
        hl.setSizeFull();
        hl.add(layout);
        return hl;
    }
}
