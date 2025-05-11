package org.pinguweb.frontend.observableList;

/**
 * Listener interface for observing changes in the ObservableList.
 */
public interface ListChangeListener<T> {
    enum ChangeType { ADD, ADD_ALL, REMOVE, REMOVE_ALL, UPDATE, CLEAR }

    void onChanged(ChangeType type, int index, T oldValue, T newValue);
}
