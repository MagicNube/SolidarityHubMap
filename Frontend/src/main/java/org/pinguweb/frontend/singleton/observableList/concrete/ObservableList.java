package org.pinguweb.frontend.singleton.observableList.concrete;

import lombok.extern.slf4j.Slf4j;
import org.pinguweb.frontend.singleton.observableList.Observer;
import org.pinguweb.frontend.singleton.observableList.ObserverChange;
import org.pinguweb.frontend.singleton.observableList.ConcreteSubject;

import java.util.*;

@Slf4j
public class ObservableList<T> extends ConcreteSubject implements List<T> {
    protected final List<T> delegate = new ArrayList<>();

    @Override
    public void notifyObservers(ObserverChange change) {
        log.info("Notifcando");
        for (Observer o : this.subscribers){
            o.update(change);
        }
    }

    @Override
    public boolean add(T element) {
        delegate.add(element);
        notifyObservers(ObserverChange.ADD);
        return true;
    }

    @Override
    public void add(int index, T element) {
        delegate.add(index, element);
        notifyObservers(ObserverChange.ADD);
    }

    @Override
    public T remove(int index) {
        T old = delegate.remove(index);
        notifyObservers(ObserverChange.REMOVE);
        return old;
    }

    @Override
    public boolean remove(Object o) {
        int index = delegate.indexOf(o);
        if (index >= 0 && index < delegate.size()) {
            delegate.remove(index);
            notifyObservers(ObserverChange.REMOVE);
            return true;
        }
        return false;
    }

    @Override
    public T set(int index, T element) {
        T old = delegate.set(index, element);
        notifyObservers(ObserverChange.SET);
        return old;
    }

    @Override
    public void clear() {
        delegate.clear();
        notifyObservers(ObserverChange.CLEAR);
    }

    @Override
    public T get(int index) {
        if (index >= 0 && index < delegate.size()) {
            return delegate.get(index);
        }
        return null;
    }

    // Delegated read-only methods
    @Override public int size() { return delegate.size(); }
    @Override public boolean isEmpty() { return delegate.isEmpty(); }
    @Override public boolean contains(Object o) { return delegate.contains(o); }
    @Override public Iterator<T> iterator() { return delegate.iterator(); }
    @Override public Object[] toArray() { return delegate.toArray(); }
    @Override public <U> U[] toArray(U[] a) { return delegate.toArray(a); }
    @Override public boolean containsAll(Collection<?> c) { return delegate.containsAll(c); }
    @Override public boolean addAll(Collection<? extends T> c) {
        boolean changed = false;
        for (T e : c) changed |= add(e);
        notifyObservers(ObserverChange.ADD_ALL);
        return changed;
    }
    @Override public boolean addAll(int index, Collection<? extends T> c) {
        boolean changed = false;
        int idx = index;
        for (T e : c) {
            add(idx++, e);
            changed = true;
        }
        notifyObservers(ObserverChange.ADD_ALL);
        return changed;
    }
    @Override public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) changed |= remove(o);
        notifyObservers(ObserverChange.REMOVE_ALL);
        return changed;
    }
    @Override public boolean retainAll(Collection<?> c) {
        List<T> toRemove = new ArrayList<>();
        for (T e : delegate) if (!c.contains(e)) toRemove.add(e);
        return removeAll(toRemove);
    }
    @Override public int indexOf(Object o) { return delegate.indexOf(o); }
    @Override public int lastIndexOf(Object o) { return delegate.lastIndexOf(o); }
    @Override public ListIterator<T> listIterator() { return delegate.listIterator(); }
    @Override public ListIterator<T> listIterator(int index) { return delegate.listIterator(index); }
    @Override public List<T> subList(int fromIndex, int toIndex) { return delegate.subList(fromIndex, toIndex); }
}
