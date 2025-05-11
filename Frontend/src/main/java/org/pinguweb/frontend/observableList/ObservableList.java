package org.pinguweb.frontend.observableList;

import java.util.*;

public class ObservableList<T> implements List<T> {
    private final List<T> delegate = new ArrayList<>();
    private final List<ListChangeListener<T>> listeners = new ArrayList<>();

    public void addListener(ListChangeListener<T> listener) {
        listeners.add(listener);
    }

    public void removeListener(ListChangeListener<T> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(ListChangeListener.ChangeType type, int index, T oldValue, T newValue) {
        for (ListChangeListener<T> l : listeners) {
            l.onChanged(type, index, oldValue, newValue);
        }
    }

    // List interface methods with notifications

    @Override
    public boolean add(T element) {
        delegate.add(element);
        notifyListeners(ListChangeListener.ChangeType.ADD, delegate.size() - 1, null, element);
        return true;
    }

    @Override
    public void add(int index, T element) {
        delegate.add(index, element);
        notifyListeners(ListChangeListener.ChangeType.ADD, index, null, element);
    }

    @Override
    public T remove(int index) {
        T old = delegate.remove(index);
        notifyListeners(ListChangeListener.ChangeType.REMOVE, index, old, null);
        return old;
    }

    @Override
    public boolean remove(Object o) {
        int index = delegate.indexOf(o);
        if (index >= 0 && index < delegate.size()) {
            T old = delegate.remove(index);
            notifyListeners(ListChangeListener.ChangeType.REMOVE, index, old, null);
            return true;
        }
        return false;
    }

    @Override
    public T set(int index, T element) {
        T old = delegate.set(index, element);
        notifyListeners(ListChangeListener.ChangeType.UPDATE, index, old, element);
        return old;
    }

    @Override
    public void clear() {
        delegate.clear();
        notifyListeners(ListChangeListener.ChangeType.CLEAR, -1, null, null);
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
        int init = delegate.size() - 1;
        for (T e : c) changed |= add(e);
        notifyListeners(ListChangeListener.ChangeType.ADD_ALL, init, null, null);
        return changed;
    }
    @Override public boolean addAll(int index, Collection<? extends T> c) {
        boolean changed = false;
        int idx = index;
        for (T e : c) {
            add(idx++, e);
            changed = true;
        }
        notifyListeners(ListChangeListener.ChangeType.ADD_ALL, index, null, null);
        return changed;
    }
    @Override public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) changed |= remove(o);
        notifyListeners(ListChangeListener.ChangeType.REMOVE_ALL, 0, null, null);
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
