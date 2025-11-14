package org.example;

import javax.swing.event.*;
import javax.swing.tree.*;
import java.io.File;

public class FileDisplayController implements TreeSelectionListener {

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        TreePath path = e.getPath();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (selectedNode == null) return;

        Object[] parts = path.getPath();
        String rootPath = Blackboard.getInstance().getUrl(); // your root folder

        StringBuilder sb = new StringBuilder(rootPath);
        for (int i = 1; i < parts.length; i++) { // skip root node
            sb.append("/").append(parts[i].toString());
        }

        String fullPath = sb.toString();
        File file = new File(fullPath);
        String relativePath = fullPath.replaceFirst("^" + rootPath + "/?", "");

        Blackboard.getInstance().setSelectedFile(relativePath);

        //System.out.println("Selected path: " + relativePath + " | isDirectory? " + file.isDirectory());
    }
}

