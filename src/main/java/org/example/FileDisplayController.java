package org.example;

import javax.swing.event.*;
import javax.swing.tree.*;

public class FileDisplayController implements TreeSelectionListener {
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        TreePath path = e.getPath();
        DefaultMutableTreeNode selectedNode =
                (DefaultMutableTreeNode) path.getLastPathComponent();
        if (selectedNode == null) return;

        String selectedFile = selectedNode.toString();

        Blackboard.getInstance().setSelectedFile(selectedFile);
    }
}
