package org.example;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Global event dispatcher for the entire application.
 * All UI components listen to this instead of Blackboard.
 */
public class MainSignaler {

    private static MainSignaler instance;
    private final PropertyChangeSupport pcs;

    private MainSignaler() {
        pcs = new PropertyChangeSupport(this);
    }

    public static MainSignaler getInstance() {
        if (instance == null) {
            instance = new MainSignaler();
        }
        return instance;
    }

    public void addListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void removeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    public void fire(String eventName, Object oldVal, Object newVal) {
        pcs.firePropertyChange(eventName, oldVal, newVal);
    }
}
