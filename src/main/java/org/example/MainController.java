package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainController implements ActionListener, PropertyChangeListener {
    private JTextField urlField;
    private JLabel statusBar;

    public MainController(JTextField field, JLabel statusBar) {
        this.urlField = field;
        this.statusBar = statusBar;

        Blackboard.getInstance().addPropertyChangeListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String url = urlField.getText().trim();
        if (!url.isEmpty()) {
            Blackboard.getInstance().setLoading(true);
            Blackboard.getInstance().loadFromUrl(url);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        switch (evt.getPropertyName()) {

            case "blackboardLoading":
                statusBar.setText("Status: Loading...");
                break;

            case "blackboardReady":
                statusBar.setText("Status: Ready");
                break;

            case "selectedStatus":
                String file = (String) evt.getNewValue();
                if (file != null && !file.isEmpty()) {
                    statusBar.setText("Selected File: " + file);
                } else {
                    statusBar.setText("Selected File: (none)");
                }
                break;
        }
    }
}
