package org.example;

import javax.swing.event.*;
import javax.swing.tree.*;

public class FileDisplayController implements TreeSelectionListener {

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        String path = getRelativePath(e.getPath());
        Blackboard.getInstance().setSelectedFile(path);

    }

    private String getRelativePath(TreePath path){
        Object[] parts = path.getPath();
        String rootPath = Blackboard.getInstance().getUrl();
        StringBuilder sb = new StringBuilder(rootPath);
        for (int i = 1; i < parts.length; i++) {
            sb.append("/").append(parts[i].toString());
        }
        String fullPath = sb.toString();
        String relativePath = fullPath.replaceFirst("^" + rootPath + "/?", "");
        return relativePath;
    }
}

