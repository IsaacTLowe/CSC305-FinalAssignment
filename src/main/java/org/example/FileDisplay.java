package org.example;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class FileDisplay extends JPanel implements PropertyChangeListener {
    private boolean loading;
    private boolean ready;
    public FileDisplay(){
    Blackboard.getInstance().addPropertyChangeListener(this);
    //Once Driver is implemented; implement feature that File calls for filepath from BlackBoard
    setBackground(Color.LIGHT_GRAY);
    setPreferredSize(new Dimension(200, 300));
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("blackboardLoading")) {
            loading = true;
            ready = false;
        } else if (evt.getPropertyName().equals("blackboardReady")) {
            loading = false;
            ready = true;
            File rootFile = new File(Blackboard.getInstance().getFilePath());
            System.out.println("This is rootFile: "+rootFile);
            DefaultMutableTreeNode rootNode = createNodes(rootFile);
            JTree tree = new JTree(rootNode);
            JScrollPane scrollPane = new JScrollPane(tree);
            scrollPane.setPreferredSize(new Dimension(175,300));
            add(scrollPane, BorderLayout.CENTER);
        }
    }

}
