package org.pinguweb.frontend.singleton.observableList;

public interface Subject {

    void attach(Observer o);
    void detach(Observer o);
    void notifyObservers(ObserverChange change);
}
