package org.example;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.File;

public class FileDisplay extends JPanel {
    public FileDisplay(){
    //Once Driver is implemented; implement feature that File calls for filepath from BlackBoard
    File rootFile = new File(Blackboard.getInstance().getFilePath());
    DefaultMutableTreeNode rootNode = createNodes(rootFile);

    JTree tree = new JTree(rootNode);

    
    add(new JScrollPane(tree), BorderLayout.CENTER);
    }

    private static DefaultMutableTreeNode createNodes(File fileRoot) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileRoot.getName());

        File[] files = fileRoot.listFiles();
        if (files == null) return node;

        for (File file : files) {
            if (file.isDirectory()) {
                node.add(createNodes(file));
            } else {
                node.add(new DefaultMutableTreeNode(file.getName()));
            }
        }
        return node;
    }

}
