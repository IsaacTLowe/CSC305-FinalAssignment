package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Draws the board and squares, observes MainSignaler events
 * @author Amelia Harris and Isaac Lowe
 * @version 1.1
 */
public class Board extends JPanel implements PropertyChangeListener {
    private boolean loading = false;
    private boolean ready = false;
    private int maxLines = 0;
    private List<Square> squares;

    public Board() {
        setBackground(Color.WHITE);
        MainSignaler.getInstance().addListener(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Square clicked = getSquareAt(e.getX(), e.getY());
                if (clicked != null) {
                    // Clicking only updates the status bar
                    Blackboard.getInstance().setSelectedStatus(clicked.getPath());
                }
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "blackboardLoading":
                loading = true;
                ready = false;
                break;
            case "blackboardReady":
                loading = false;
                ready = true;
                squares = Blackboard.getInstance().getSquares();
                break;
            case "selectedFile":
                squares = Blackboard.getInstance().getSquaresDisplay();
                break;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (ready && squares != null) {
            drawSquares(g);
        }
    }

    private void drawSquares(Graphics g) {
        if (squares.isEmpty()) return;

        // Compute max lines for color ratio
        maxLines = squares.stream().mapToInt(Square::getLinesOfCode).max().orElse(1);
        int complexity = Blackboard.getInstance().getComplexity();

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

            Color color = calculateColor(square.getLinesOfCode(), complexity);
            g.setColor(color);
            g.fillRect(x, y, squareWidth - 2, squareHeight - 2);

            g.setColor(Color.BLACK);
            g.drawRect(x, y, squareWidth - 2, squareHeight - 2);

            g.setFont(new Font("Arial", Font.PLAIN, 6));
            g.drawString(square.getName() + "(" + square.getLinesOfCode() + ")", x + 5, y + 15);
        }
    }

    private Color calculateColor(int lines, int complexity) {
        double ratio = (double) lines / maxLines;

        int r, g, b;
        if (ratio <= 1.0 / 3.0) {
            r = 180; g = 240; b = 180;
        } else if (ratio <= 2.0 / 3.0) {
            r = 255; g = 245; b = 150;
        } else {
            r = 240; g = 140; b = 140;
        }

        // Map complexity (0–100) → alpha (50–255)
        int alpha = Math.min(255, Math.max(50, (int)((complexity / 100.0) * 255)));
        return new Color(r, g, b, alpha);
    }

    private Square getSquareAt(int x, int y) {
        if (squares == null || squares.isEmpty()) return null;

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
