package org.example;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class FileDisplay extends JPanel implements PropertyChangeListener {

    private boolean loading;
    private boolean ready;

    public FileDisplay() {
        Blackboard.getInstance().addPropertyChangeListener(this);
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(200, 300));
        setLayout(new BorderLayout());
    }

    private DefaultMutableTreeNode buildTreeFromPaths(List<String> paths, String repoName) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(repoName);

        for (String path : paths) {
            String[] parts = path.split("/");
            DefaultMutableTreeNode current = root;

            for (String part : parts) {
                DefaultMutableTreeNode child = null;

                for (int i = 0; i < current.getChildCount(); i++) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) current.getChildAt(i);
                    if (node.getUserObject().equals(part)) {
                        child = node;
                        break;
                    }
                }

                if (child == null) {
                    child = new DefaultMutableTreeNode(part);
                    current.add(child);
                }

                current = child;
            }
        }

        return root;
    }

    private void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
        for (int i = startingIndex; i < rowCount; i++) {
            tree.expandRow(i);
        }

        if (tree.getRowCount() != rowCount) {
            expandAllNodes(tree, rowCount, tree.getRowCount());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals("blackboardLoading")) {
            loading = true;
            ready = false;

        } else if (evt.getPropertyName().equals("blackboardReady")) {
            loading = false;
            ready = true;

            removeAll();

            List<Square> squares = Blackboard.getInstance().getSquares();

            List<String> paths = squares.stream()
                    .map(Square::getPath)
                    .toList();

            String repoUrl = Blackboard.getInstance().getFilePath();
            String repoName = repoUrl.substring(repoUrl.lastIndexOf("/") + 1);

            DefaultMutableTreeNode root = buildTreeFromPaths(paths, repoName);
            JTree tree = new JTree(root);

            expandAllNodes(tree, 0, tree.getRowCount());

            JScrollPane scrollPane = new JScrollPane(tree);
            scrollPane.setPreferredSize(new Dimension(175, 300));

            add(scrollPane, BorderLayout.CENTER);
            revalidate();
            repaint();
        }
    }
}
