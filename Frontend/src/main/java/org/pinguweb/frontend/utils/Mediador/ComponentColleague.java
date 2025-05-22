package org.pinguweb.frontend.utils.Mediador;

import lombok.Getter;

@Getter
public abstract class ComponentColleague {
    protected Mediator mediator;

    public ComponentColleague(Mediator mediator) {
        this.mediator = mediator;
        register();
    }

    public abstract void register();

    public abstract <T> void receive(Event<T> event);
}
