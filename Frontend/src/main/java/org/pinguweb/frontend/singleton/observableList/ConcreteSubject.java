package org.pinguweb.frontend.singleton.observableList;

import java.util.LinkedList;
import java.util.List;

public class ConcreteSubject implements Subject {
    protected final List<Observer> subscribers = new LinkedList<>();
    @Override
    public void attach(Observer o) {
        subscribers.add(o);
    }

    @Override
    public void detach(Observer o) {
        subscribers.remove(o);
    }

    @Override
    public void notifyObservers(ObserverChange change) {
        throw new RuntimeException("No notify was specify");
    }
}
