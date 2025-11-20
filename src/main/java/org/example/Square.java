package org.example;


/**
 * Square controls all aspects related to the Square's that are drawn on the screen
 * @author Amelia Harris and Isaac Lowe
 * @version 1.0
 */

public class Square {

    private String path;
    private int lines;
    private boolean abstraction;
    private int in;
    private int out;
    String fileContent;

    public Square(String path, int lines, boolean abstraction, String fileContent) {
        this.path = path;
        this.lines = lines;
        this.abstraction = abstraction;
        this. in= 0;
        this.out = 0;
        this.fileContent = fileContent;
    }

    public int getLinesOfCode() {
        return lines;
    }

    public String getName() {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public boolean getAbstraction(){
        return abstraction;
    }
    public void incIn(){
        in++;
    }
    public void incOut(){
        out++;
    }
    public String getFileContent(){
        return fileContent;
    }
    public double getInstability(){
        if(out+in == 0){
            return 0;
        }
        return (out/(in*1.0+out));
    }

    public String getPath() {
        return this.path;
    }

}