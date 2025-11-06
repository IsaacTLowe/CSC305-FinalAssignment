package org.example;


//Isaac
public class Square {

    private String path;
    private int lines;

    public Square(String path, int lines) {
        this.path = path;
        this.lines = lines;
    }

    public int getLinesOfCode() {
        return lines;
    }

    public String getName() {
        return path.substring(path.lastIndexOf("/") + 1);
    }

}