package org.example;
import java.beans.*;
import javax.swing.*;
import java.awt.*;


public class Metrics extends JPanel implements PropertyChangeListener{
    
    private boolean loading = false;
	private boolean ready = true;
    private String selectedFile = null;

    public Metrics(){
        setBackground(Color.WHITE);
        Blackboard.getInstance().addPropertyChangeListener(this);
    }
    @Override
    protected void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 3;
        super.paintComponent(g);
        if(loading){
            drawLoading(g, width, height);
        }else if (ready){
            drawUseless(g, radius, width);
            drawPainful(g, radius, height);
            g.setColor(Color.BLACK);
            g.drawLine(0, 0, width, height);
            drawDots(g);
        }
    
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(evt.getPropertyName());
        if(evt.getPropertyName().equals("blackboardLoading")) {
            loading = true;
            ready = false;
        } else if (evt.getPropertyName().equals("blackboardReady")) {
            loading = false;
            ready = true;
        } else if (evt.getPropertyName().equals("selectedFile")) {
            selectedFile = Blackboard.getInstance().getSelectedFile();
        }
        repaint();
    }

    public void drawUseless(Graphics g, int radius, int width){
        g.setColor(Color.BLUE);
        g.fillArc(width-radius, 0-radius, radius * 2, radius * 2, 180, 90);
        g.setColor(Color.WHITE);
        g.drawString("useless", width - radius+60, radius / 2);
    }

    public void drawPainful(Graphics g, int radius, int height){
        g.setColor(Color.RED);
        g.fillArc(0-radius, height - radius, radius * 2, radius * 2, 0, 90);
        g.setColor(Color.WHITE);
        g.drawString("painful", radius / 3, height - radius / 2);
    }
    public void drawLoading(Graphics g, int width, int height){
		g.setColor(Color.BLACK);
		setFont(new Font("Arial", Font.PLAIN, 12));
		g.drawString("Loading...", width / 2 - 30, height / 2);
    }
    public void drawDots(Graphics g) {
        java.util.List<Square> squares = Blackboard.getInstance().getSquares();
        if (squares == null || squares.isEmpty()) return;

        if(selectedFile != null && !selectedFile.isEmpty()) {
            squares = squares.stream()
                    .filter(s -> s.getPath().endsWith(selectedFile))
                    .toList();
        }

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int dotRadius = 3;
        int margin = dotRadius + 2;

        g.setColor(Color.BLACK);

        for (Square s : squares) {
            double instability = s.getInstability();
            boolean abstraction = s.getAbstraction();

            int abs = abstraction ? 0 : 1;

            int x = (int) (instability * (panelWidth - 2 * margin)) + margin;
            int y = (int) (abs * (panelHeight - 2 * margin)) + margin;
            FontMetrics fm = g.getFontMetrics();

            g.fillOval(x - dotRadius, y - dotRadius, dotRadius * 2, dotRadius * 2);
            String name = s.getName();
            int textWidth = fm.stringWidth(name);
            int textX = x - textWidth / 2;
            int textY = y - dotRadius - 4;
            g.drawString(name, textX, textY);
        }
    }

}
