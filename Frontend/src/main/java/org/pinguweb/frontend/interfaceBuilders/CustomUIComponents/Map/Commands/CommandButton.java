package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands;

import com.vaadin.flow.component.button.Button;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Slf4j
public class CommandButton extends Button{
    private Command command;

    public CommandButton(){
        super();
        this.addClickListener(event -> {
            invoke();
        });
    }

    public CommandButton(String text){
        super(text);
        this.addClickListener(event -> {
            invoke();
        });
    }

    public void invoke() {
        if (command != null) {
            command.execute();
        } else {
            log.error("No hay ningún comando asignado.");
        }
    }
}
