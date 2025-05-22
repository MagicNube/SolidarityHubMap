package org.pinguweb.frontend.utils.Mediador;

public interface Mediator {
    void subscribe(EventType eventType, Colleague colleague);

    <T> void publish(Event<T> event);
}