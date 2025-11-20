package org.example;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * FileDisplay uses JTree to display all the files on the left hand side of the screen for users to click through.
 * Observes Blackboard.
 * @author Amelia Harris and Isaac Lowe
 * @version 1.0
 */

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

            makeTree();
            
        }
        repaint();
    }

    private void makeTree(){
            removeAll();

            List<Square> squares = Blackboard.getInstance().getSquares();
            if(squares.isEmpty()){
                return;
            }
            List<String> paths = squares.stream()
                    .map(Square::getPath)
                    .toList();

            String repoUrl = Blackboard.getInstance().getUrl();
            String repoName = repoUrl.substring(repoUrl.lastIndexOf("/") + 1);

            DefaultMutableTreeNode root = buildTreeFromPaths(paths, repoName);
            JTree tree = new JTree(root);

            expandAllNodes(tree, 0, tree.getRowCount());

            tree.addTreeSelectionListener(new FileDisplayController());
            JScrollPane scrollPane = new JScrollPane(tree);
            scrollPane.setPreferredSize(new Dimension(175, 300));

            add(scrollPane, BorderLayout.CENTER);
            revalidate();
    }

}
