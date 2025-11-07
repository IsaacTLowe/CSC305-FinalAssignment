package org.example;


import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public Main() {
        Board board = new Board();
        Metrics metric = new Metrics();
        MainController controller = new MainController();
        //FileDisplay fileDisplay = new FileDisplay();

        JPanel topPanel = new JPanel(new BorderLayout());
        JTextField field = new JTextField("");
        JButton okButton = new JButton("OK");
        JLabel statusBar = new JLabel("Status Bar",  SwingConstants.CENTER);

        setLayout(new BorderLayout());
        topPanel.add(new JLabel("URL:"), BorderLayout.WEST);
        topPanel.add(okButton, BorderLayout.EAST);
        topPanel.add(field, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(statusBar, BorderLayout.SOUTH);
        //add(fileDisplay, BorderLayout.WEST);
        add(metric, BorderLayout.CENTER);


    }

    public static void main(String[] args) {
        Main main = new Main();
        main.setTitle("Assignment 3");
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setSize(800, 600);
        main.setVisible(true);
    }

}
