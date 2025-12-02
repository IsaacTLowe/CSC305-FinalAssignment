package org.example;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

/**
 *
 * @author Amelia Harris and Isaac Lowe
 * @version 1.0
 */

public class UMLPanel extends JPanel implements PropertyChangeListener {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    private BufferedImage image;

    public UMLPanel(String umlSource) {
        setBackground(Color.WHITE);
        try {
            SourceStringReader reader = new SourceStringReader(umlSource);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            reader.outputImage(os, new FileFormatOption(FileFormat.PNG));
            os.close();
            image = ImageIO.read(new ByteArrayInputStream(os.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
