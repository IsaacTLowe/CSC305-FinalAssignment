package org.example;


import javax.swing.*;
import java.awt.*;

/**
 * Puts everything together as well as creating the overall GUI for the program
 * @author Amelia Harris and Isaac Lowe
 * @version 2.2
 */

public class Main extends JFrame {
    public Main() {
        JPanel topPanel = new JPanel(new BorderLayout());
        JTextField urlField = new JTextField("");
        JButton okButton = new JButton("OK");
        JLabel statusBar = new JLabel("Status: ",  SwingConstants.CENTER);

        Board board = new Board();
        Metrics metric = new Metrics();
        JPanel diagram = new JPanel();
        MainController controller = new MainController(urlField, statusBar);
        FileDisplay fileDisplay = new FileDisplay();

        setLayout(new BorderLayout());
        topPanel.add(new JLabel("URL:"), BorderLayout.WEST);
        topPanel.add(okButton, BorderLayout.EAST);
        topPanel.add(urlField, BorderLayout.CENTER);

        JTabbedPane panel = new JTabbedPane();
        add(topPanel, BorderLayout.NORTH);
        add(statusBar, BorderLayout.SOUTH);
        panel.add("Grid",board);
        panel.add("Metrics",metric);
        panel.add("Diagram",diagram);
        add(fileDisplay, BorderLayout.WEST);
        add(panel, BorderLayout.CENTER);

        okButton.addActionListener(controller);

    }

    public static void main(String[] args) {
        Main main = new Main();
        main.setTitle("Assignment 3");
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setSize(800, 600);
        main.setVisible(true);
    }

}
