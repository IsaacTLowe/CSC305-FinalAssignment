package org.example;

import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Vector;

public class Blackboard extends PropertyChangeSupport {

    private static Blackboard instance;
    private Vector<Square> squares;
    private int size;
    private boolean ready = false;
    private boolean loading = false;
    private String url;
    private String selected;

    private Blackboard() {
        super(new Object());
        squares = new Vector<>();
    }

    public static Blackboard getInstance() {
        if (instance == null) {
            instance = new Blackboard();
        }
        return instance;
    }

    public void loadFromUrl(String url) {
        try {
            Driver driver = new Driver(url);
            Thread t = new Thread(driver);
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSize(int size){
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void addSquare(Square square) {
        squares.add(square);
    }

    public void setReady() {
        ready = true;
        firePropertyChange("blackboardReady", false, true);
    }

    public void setLoading(boolean loading) {
        ready = loading;
        firePropertyChange("blackboardLoading", false, true);
    }
    public void setUrl(String url){
        this.url=url;
    }

    public List<Square> getSquares() {
        return squares;
    }

    public List<Square> getSquaresDisplay() {
        Vector<Square> displaySquares = new Vector<>();
            for(Square currSquare : squares){
                System.out.println("Current Square Path: " + currSquare.getPath() + " Selected: "+selected);
                if(currSquare.getPath().contains(selected)){
                    displaySquares.add(currSquare);
                }

            }
        return displaySquares;
    }

    public String getUrl(){
        return url;
    }

    public void clear() {
        squares.clear();
        ready = false;
        loading = false;
        size = 0;
        url = "";
        selected = "";
    }

    public void setSelectedFile(String selected) {
        String old = this.selected;
        this.selected = selected;
        firePropertyChange("selectedFile", old, selected);
    }
    public void setSelectedFolder(String selected){
        String old = this.selected;
        this.selected = selected;
        firePropertyChange("selectedFolder", old, selected);
    }

    public String getSelected() {
        return selected;
    }
}
