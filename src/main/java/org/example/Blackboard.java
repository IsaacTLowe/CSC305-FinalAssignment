package org.example;

import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Vector;

/**
 * Stores data on the files, squares, url, selected file/folder, and is obserable
 * @author Amelia Harris and Isaac Lowe
 * @version 2.0
 */

public class Blackboard extends PropertyChangeSupport {

    private static Blackboard instance;
    private Vector<Square> squares;
    private int size;
    private int complexity;
    private boolean ready = false;
    private String url;
    private String selected;
    private String statusBarText;
    private String umlSource;

    private Blackboard() {
        super(new Object());
        squares = new Vector<>();
        umlSource = "";
    }

    public static Blackboard getInstance() {
        if (instance == null) {
            instance = new Blackboard();
        }
        return instance;
    }

    /**
     * Attempts to load UML data from URL and reports detailed failures
     */
    public void loadFromUrl(String url) {
        try {
            this.url = url;

            // Attempt to construct the driver — if URL is malformed or null, this will fail.
            Driver driver = new Driver(url);
            Thread t = new Thread(driver);
            t.start();

            // UI feedback: begin loading
            setLoading(true);

        } catch (IllegalArgumentException malformed) {
            // Driver likely threw due to malformed URL
            notifyUrlError("Malformed URL: " + malformed.getMessage());
        } catch (Exception e) {
            // Could be network, token, 401, timeout, etc
            notifyUrlError("Unexpected load error: " + e.getMessage());
        }
    }

    // --------------------------
    // URL ERROR REPORTING
    // --------------------------

    /**
     * Sends status bar + property events for URL failures.
     */
    private void notifyUrlError(String message) {
        this.ready = false;
        setStatus("URL Error — " + message);
        firePropertyChange("errorURL", false, true);
    }

    // --------------------------
    // STATUS BAR MANAGEMENT
    // --------------------------

    /**
     * Updates the status bar with any text.
     */
    public void setStatus(String newStatus) {
        String old = this.statusBarText;
        this.statusBarText = newStatus;
        firePropertyChange("statusBar", old, newStatus);
    }

    public String getStatus() {
        return statusBarText;
    }

    // --------------------------
    // EXISTING METHODS
    // --------------------------

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
        setStatus("Load complete.");
    }

    public void setLoading(boolean loading) {
        this.ready = !loading;
        firePropertyChange("blackboardLoading", false, loading);
        if (loading) {
            setStatus("Loading from URL...");
        }
    }

    public void setErrorURL() {
        ready = false;
        firePropertyChange("errorURL", false, true);
        setStatus("URL Error.");
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
        umlSource = "";
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

    public void addUmlSource(String umlSource) {
        if (!this.umlSource.contains(umlSource)) {
            this.umlSource += umlSource;
        }
    }

    public String getUmlSource() {
        return umlSource;
    }

    public int getComplexity() { return complexity; }

    public void setComplexity(int complexity) { this.complexity = complexity; }

}
