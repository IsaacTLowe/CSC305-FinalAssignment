package org.example;

import java.util.List;
import java.util.Vector;

/**
 * Pure data storage. All notifications are routed through MainSignaler.
 * @author Amelia Harris and Isaac Lowe
 * @version 1.1
 */
public class Blackboard {

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
        squares = new Vector<>();
        umlSource = "";
    }

    public static Blackboard getInstance() {
        if (instance == null) {
            instance = new Blackboard();
        }
        return instance;
    }



    public void loadFromUrl(String url) {
        try {
            this.url = url;

           
            Driver driver = new Driver(url);
            Thread t = new Thread(driver);
            t.start();

            setLoading(true);

        } catch (Exception e) {
            notifyUrlError("Unexpected load error: " + e.getMessage());
        }
    }

    private void notifyUrlError(String message) {
        ready = false;
        setStatus("URL Error — " + message);
        MainSignaler.getInstance().fire("errorURL", false, true);
    }

 

    public void setStatus(String newStatus) {
        String old = this.statusBarText;
        this.statusBarText = newStatus;
        MainSignaler.getInstance().fire("statusBar", old, newStatus);
    }

    public String getStatus() {
        return statusBarText;
    }



    public void addSquare(Square square) {
        squares.add(square);
    }

    public List<Square> getSquares() {
        return squares;
    }

    public List<Square> getSquaresDisplay() {
        Vector<Square> display = new Vector<>();

        for (Square curr : squares) {
            String filePath = selected + "/" + curr.getName();
            if (curr.getPath().equals(selected)) filePath = selected;

            if (curr.getPath().contains(filePath)) {
                display.add(curr);
            }
        }
        return display;
    }

    public void setReady() {
        ready = true;
        MainSignaler.getInstance().fire("blackboardReady", false, true);
        setStatus("Load complete.");
    }

    public void setLoading(boolean loading) {
        ready = !loading;
        MainSignaler.getInstance().fire("blackboardLoading", false, loading);

        if (loading) setStatus("Loading from URL...");
    }

    public void setSelectedFile(String selected) {
        String old = this.selected;
        this.selected = selected;

        MainSignaler.getInstance().fire("selectedFile", old, selected);

        setSelectedStatus(selected);
    }

    public void setSelectedStatus(String status) {
        String old = this.statusBarText;
        this.statusBarText = status;

        MainSignaler.getInstance().fire("selectedStatus", old, status);
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public void clear() {
        squares.clear();
        ready = false;
        size = 0;
        url = "";
        selected = "";
        umlSource = "";
    }

    public String getUrl() { return url; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public int getComplexity() { return complexity; }
    public void setComplexity(int complexity) { this.complexity = complexity; }

    public void addUmlSource(String umlSource) {
        if(!this.umlSource.contains(umlSource)){
            this.umlSource += umlSource;
        }
    }

    public String getUmlSource() {
        return umlSource;
    }


    public void setErrorURL() {
        ready = false;
        MainSignaler.getInstance().fire("errorURL", false, true);
        setStatus("URL Error.");
    }


}
