package org.example;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author Amelia Harris and Isaac Lowe
 * @version 1.0
 */

public class StatusBar implements PropertyChangeListener {
    private JLabel statusBar;

    public StatusBar(JLabel statusBar) {
        this.statusBar = statusBar;

        Blackboard.getInstance().addPropertyChangeListener(this);
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
            case "errorURL":
                statusBar.setText("Status: ERROR invalid URL");
                break;
        }
    }
}
