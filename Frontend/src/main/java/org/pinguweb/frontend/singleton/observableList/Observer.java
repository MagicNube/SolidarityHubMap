package org.pinguweb.frontend.singleton.observableList;

public interface Observer {

    // CHANGE ES UN CAMBIO DE LO QUE PONE EL LIBRO, SIRVE PARA TENER CONTEXTO DEL CAMBIO
    void update(ObserverChange change);
}
