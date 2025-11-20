package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Handles user clicking on Squares, clicking on the okButton to start the program, and the status bar's status. 
 * Observes Blackboard.
 * @author Amelia Harris and Isaac Lowe
 * @version 1.0
 */

public class MainController implements ActionListener {
    private JTextField urlField;

    public MainController(JTextField field) {
        this.urlField = field;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String url = urlField.getText().trim();
        if (!url.isEmpty()) {
            Blackboard.getInstance().setLoading(true);
            Blackboard.getInstance().loadFromUrl(url);
        }
    }

}
