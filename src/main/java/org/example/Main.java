package org.example;


import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public Main() {
        JPanel topPanel = new JPanel(new BorderLayout());
        JTextField urlField = new JTextField("");
        JButton okButton = new JButton("OK");
        JLabel statusBar = new JLabel("Status Bar",  SwingConstants.CENTER);

        Board board = new Board();
        Metrics metric = new Metrics();
        MainController controller = new MainController(urlField);
        FileDisplay fileDisplay = new FileDisplay();

        setLayout(new BorderLayout());
        topPanel.add(new JLabel("URL:"), BorderLayout.WEST);
        topPanel.add(okButton, BorderLayout.EAST);
        topPanel.add(urlField, BorderLayout.CENTER);

        JTabbedPane panel = new JTabbedPane();
        add(topPanel, BorderLayout.NORTH);
        add(statusBar, BorderLayout.SOUTH);
        //add(board, BorderLayout.CENTER);
        panel.add("Grid",board);
        panel.add("Metrics",metric);
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
