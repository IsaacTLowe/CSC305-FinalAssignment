package org.example;

import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Vector;

/**
 * Stores data on the files, squares, url, selected file, and statusBar
 * @author Amelia Harris and Isaac Lowe
 * @version 2.2
 */

public class Blackboard extends PropertyChangeSupport {

    private static Blackboard instance;
    private Vector<Square> squares;
    private int size;
    private boolean ready = false;
    private String url;
    private String selected;
    private String statusBarText;

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
    public void setErrorURL() {
        ready = false;
        firePropertyChange("errorURL", false, true);
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
                String filePath = selected + "/"+currSquare.getName();
                if(currSquare.getPath().equals(selected)){
                    filePath = selected;
                }
                if(currSquare.getPath().contains(filePath)){
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
        boolean loading = false;
        size = 0;
        url = "";
        selected = "";
    }

    public void setSelectedFile(String selected) {
        String old = this.selected;
        this.selected = selected;
        firePropertyChange("selectedFile", old, selected);

        setSelectedStatus(selected);
    }

    public void setSelectedStatus(String statusBarText) {
        String old = this.statusBarText;
        this.statusBarText = statusBarText;
        firePropertyChange("selectedStatus", old, statusBarText);
    }

}
