package org.example;

import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Vector;

public class Blackboard extends PropertyChangeSupport {

    private static Blackboard instance;
    private Vector<Square> squares;
    private boolean ready = false;
    private boolean loading = false;

    private Blackboard() {
        super(new Object());
        squares = new Vector<>();
    }

    public static Blackboard getInstance() {
        if(instance == null) {
            instance = new Blackboard();
        }
        return instance;
    }


    public List<Square> getSquares() {
        return squares;
    }
}
