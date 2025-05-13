package org.pinguweb.frontend.singleton.observableList;

import org.yaml.snakeyaml.util.Tuple;

import java.util.LinkedList;
import java.util.List;

public class ConcreteSubject implements Subject {
    protected final List<Tuple<Observer, ObserverChange>> subscribers = new LinkedList<>();
    @Override
    public void attach(Observer o, ObserverChange... filtros) {
        for (ObserverChange filtro : filtros){
            subscribers.add(new Tuple<>(o, filtro));
        }
    }

    @Override
    public void detach(Observer o, ObserverChange... filtros) {
        for (ObserverChange filtro : filtros){
            subscribers.remove(new Tuple<>(o, filtro));
        }
    }

    @Override
    public void notifyObservers(ObserverChange change) {
        throw new RuntimeException("No notify was specify");
    }
}
