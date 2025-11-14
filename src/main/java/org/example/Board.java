package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Draws the board and squares
 * @author Amelia Harris and Isaac Lowe
 * @version 2.2
 */

public class Board extends JPanel implements PropertyChangeListener {
    private boolean loading = false;
    private boolean ready = false;
    private int maxLines = 0;
    private java.util.List<Square> squares;

    public Board() {
        setBackground(Color.WHITE);
        Blackboard.getInstance().addPropertyChangeListener(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Square clicked = getSquareAt(e.getX(), e.getY());
                if (clicked != null) {
                    Blackboard.getInstance().setSelectedStatus(clicked.getPath());
                }
            }
        });
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
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (ready) {
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


    private Color calculateColor(int lines) {
        double ratio = (double) lines / maxLines;

        if (ratio <= 1.0 / 3.0) {
            return new Color(180, 240, 180);
        } else if (ratio <= 2.0 / 3.0) {
            return new Color(255, 245, 150);
        } else {
            return new Color(240, 140, 140);
        }
    }

    private Square getSquareAt(int x, int y) {
        java.util.List<Square> squares = Blackboard.getInstance().getSquares();
        if (squares.isEmpty()) return null;

        int cols = (int) Math.ceil(Math.sqrt(squares.size()));
        int rows = (int) Math.ceil((double) squares.size() / cols);
        int w = getWidth() / cols;
        int h = getHeight() / rows;

        for (int i = 0; i < squares.size(); i++) {
            int row = i / cols;
            int col = i % cols;
            int sx = col * w;
            int sy = row * h;

            if (x >= sx && x < sx + w && y >= sy && y < sy + h) {
                return squares.get(i);
            }
        }
        return null;
    }
}
