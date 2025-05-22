package org.pinguweb.frontend.utils.Mediador;

public interface Colleague {

        void register();

        <T> void receive(Event<T> event);
}
