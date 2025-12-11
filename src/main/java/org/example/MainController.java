package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Handles user clicking on Squares, as well as clicking on the okButton to start the program
 * @author Amelia Harris and Isaac Lowe
 * @version 2.3
 */
public class MainController implements ActionListener, PropertyChangeListener {
    private JTextField urlField;
    private JLabel statusBar;

    public MainController(JTextField field, JLabel statusBar) {
        this.urlField = field;
        this.statusBar = statusBar;

        MainSignaler.getInstance().addListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String url = urlField.getText().trim();
        if (!url.isEmpty()) {
            statusBar.setText("Status: Validating URL...");
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
                statusBar.setText("Status: Load complete — Ready");
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

            case "statusBar":
                statusBar.setText((String) evt.getNewValue());
                break;

        }
    }

    /**
     * Provides detailed feedback on WHAT caused the URL load failure.
     */
    private void handleUrlError(String errorType) {
        if (errorType == null) {
            statusBar.setText("Status: Unknown URL error");
            return;
        }

        switch (errorType) {
            case "INVALID_URL":
                statusBar.setText("Status: Invalid URL format");
                break;

            case "NETWORK_ERROR":
                statusBar.setText("Status: Network error — check connection");
                break;

            case "TIMEOUT":
                statusBar.setText("Status: Connection timed out");
                break;

            case "AUTH_ERROR":
                statusBar.setText("Status: Authentication/token error");
                break;

            case "HTTP_ERROR":
                statusBar.setText("Status: Server responded with an error (HTTP)");
                break;

            case "PARSING_ERROR":
                statusBar.setText("Status: Error parsing retrieved content");
                break;

            default:
                statusBar.setText("Status: Unknown error (" + errorType + ")");
        }
    }
}
