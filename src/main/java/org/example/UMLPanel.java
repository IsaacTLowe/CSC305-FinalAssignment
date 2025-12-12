package org.example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

/**
 *
 * @author Amelia Harris and Isaac Lowe
 * @version 1.0
 */

public class UMLPanel extends JPanel implements PropertyChangeListener {
    

    private BufferedImage image;
    private String umlSource;
    private boolean loading = false;
    private boolean ready = false;

    public UMLPanel() {
        setBackground(Color.WHITE);
        MainSignaler.getInstance().addListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (image != null) {
            return new Dimension(image.getWidth(), image.getHeight());
        }
        return super.getPreferredSize();
        
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("blackboardLoading")) {
            loading = true;
            ready = false;
        } else if (evt.getPropertyName().equals("blackboardReady")) {
            loading = false;
            ready = true;
            umlSource = Blackboard.getInstance().getUmlSource();
            createImageFromUML(umlSource);
        }
        else if (evt.getPropertyName().equals("selectedFile")) {
            umlSource = Blackboard.getInstance().getUmlSource();
            umlSource = getCurrUml();
            createImageFromUML(umlSource);
        }
        repaint();
        
    }

    private void createImageFromUML(String umlSource) {
        String completeUML = "@startuml\n!pragma layout smetana\n" + umlSource + "@enduml\n";
        System.out.println("Complete UML Source:\n" + completeUML);
        try {
            if(umlSource.isEmpty()) {
                image = null;
                return;
            }
            SourceStringReader reader = new SourceStringReader(completeUML);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            reader.outputImage(os, new FileFormatOption(FileFormat.PNG));
            os.close();
            image = ImageIO.read(new ByteArrayInputStream(os.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String getCurrUml(){

        if (umlSource == null || umlSource.isBlank()) {
            return "";
        }

        java.util.List<Square> displaySquares = Blackboard.getInstance().getSquaresDisplay();
        List<String> visibleNames = new ArrayList<>();
        for (Square sq : displaySquares) {
            String name = sq.getName();
            if (name == null) continue;

            if (name.endsWith(".java")) {
                name = name.substring(0, name.length() - 5);
            }
            visibleNames.add(name);
        }
        System.out.println("Visible Names: " + visibleNames);

        // 2. Filter UML lines
        StringBuilder filtered = new StringBuilder();
        String[] lines = umlSource.split("\\R"); // split on any line break

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;

            // Check if this line mentions any visible class
            boolean keep = false;
            for (String cls : visibleNames) {
                if (trimmed.contains(cls+" ")) {
                    keep = true;
                    break;
                }
            }

            if (keep) {
                filtered.append(line).append("\n");
            }
            System.out.println("Filtered UML: " + filtered.toString());
            System.out.println("Blackboard UML: "+Blackboard.getInstance().getUmlSource());
        }

        return filtered.toString();
    }
}
