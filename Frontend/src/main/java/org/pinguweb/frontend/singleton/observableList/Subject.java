package org.pinguweb.frontend.singleton.observableList;

public interface Subject {

    /*
    NOTA PARA NICO: Cambio en el patrón. Acepta filtrar por un tipo de evento
     */

    void attach(Observer o, ObserverChange... filtros);
    void detach(Observer o, ObserverChange... filtros);
    void notifyObservers(ObserverChange change);
}
