package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
