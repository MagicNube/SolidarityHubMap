package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents;

import com.vaadin.flow.component.Component;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class GenericComponent extends InterfaceComponent{

    private Component component;

    @Override
    public final Component getComponent() {
        return component;
    }
}
