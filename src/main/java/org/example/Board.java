package org.example;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

//Isaac
public class Board extends JPanel implements PropertyChangeListener {
    private boolean loading = false;
    private boolean ready = false;
    private int maxLines = 0;
    private String selectedFile = null;
    private java.util.List<Square> squares;

    public Board() {
        setBackground(Color.WHITE);
        Blackboard.getInstance().addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(evt.getPropertyName());
        if (evt.getPropertyName().equals("blackboardLoading")) {
            loading = true;
            ready = false;
        } else if (evt.getPropertyName().equals("blackboardReady")) {
            loading = false;
            ready = true;
            squares = Blackboard.getInstance().getSquares();
        } else if (evt.getPropertyName().equals("selectedFile")) {
            squares = Blackboard.getInstance().getSquaresDisplay();
            //getSquares("file", Blackboard.getInstance().getSelected());
        }else if (evt.getPropertyName().equals("selectedFolder")){
            //getSquares("folder", Blackboard.getInstance().getSelected());
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (loading) {
            drawLoading(g);
        } else if (ready) {
            drawSquares(g);
        }
    }

    private void drawSquares(Graphics g) {
        if(squares.isEmpty()){
            return;
        }
        for (Square s : squares) {
            if (s.getLinesOfCode() > maxLines) {
                maxLines = s.getLinesOfCode();
            }
        }
        
        int cols = (int) Math.ceil(Math.sqrt(squares.size()));
        int rows = (int) Math.ceil((double) squares.size() / cols);
        int squareWidth = getWidth() / cols;
        int squareHeight = getHeight() / rows;
        for (int i = 0; i < squares.size(); i++) {
            Square square = squares.get(i);
            int row = i / cols;
            int col = i % cols;
            int x = col * squareWidth;
            int y = row * squareHeight;

            Color color = calculateColor(square.getLinesOfCode());
            g.setColor(color);
            g.fillRect(x, y, squareWidth - 2, squareHeight - 2);

            g.setColor(Color.BLACK);
            g.drawRect(x, y, squareWidth - 2, squareHeight - 2);

            setFont(new Font("Arial", Font.PLAIN, 6));
            String text = square.getName() + "(" + square.getLinesOfCode() + ")";
            g.drawString(text, x + 5, y + 15);
        }
    }

    private void drawLoading(Graphics g) {
        g.setColor(Color.BLACK);
        setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Loading...", getWidth() / 2 - 30, getHeight() / 2);
    }

    private Color calculateColor(int lines) {
        double ratio = (double) lines / maxLines;

        if (ratio <= 1.0 / 3.0) {
            return new Color(180, 240, 180);   // green
        } else if (ratio <= 2.0 / 3.0) {
            return new Color(255, 245, 150);   // yellow
        } else {
            return new Color(240, 140, 140);   // red
        }
    }
    public void getSquares(String type, String selected){
        squares = Blackboard.getInstance().getSquares();
        switch(type){
            case "file":
                    if(selected != null && !selected.isEmpty()) {
                        squares = squares.stream()
                        .filter(s -> s.getPath().endsWith(selected))
                        .toList();
                    }
                    break;
            case "folder":
                    if(selected != null && !selected.isEmpty()) {
                        squares = squares.stream()
                        .filter(s -> s.getPath().contains(selected))
                        .toList();
                    }
                    break;
        }
    }
}
