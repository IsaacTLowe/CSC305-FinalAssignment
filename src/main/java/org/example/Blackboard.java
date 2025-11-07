package org.example;

import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Vector;

public class Blackboard extends PropertyChangeSupport {

    private static Blackboard instance;
    private Vector<Square> squares;
    private boolean ready = false;
    private boolean loading = false;
    private String filePath; //DummyValue for now

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
    public void loadFromUrl(String url){
        // implement when driver is implemented 
    }

    public List<Square> getSquares() {
        return squares;
    }
    //once driver is implemented; implement a way to acquire the FilePathing (for fileDisplay)
    public String getFilePath(){
        return filePath;
    }
}
