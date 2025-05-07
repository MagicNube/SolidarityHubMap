package org.pinguweb.frontend.interfaceBuilders.Builders;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.pinguweb.frontend.view.NavigationBar;

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

    private HorizontalLayout twoColumns(Component left, Component right) {
        HorizontalLayout row = new HorizontalLayout(left, right);
        row.setWidthFull();
        row.setSpacing(true);
        row.setPadding(false);
        row.expand(left, right);
        return row;
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
        hl.add(NavigationBar.createNavBar(), layout);
        return hl;
    }
}
