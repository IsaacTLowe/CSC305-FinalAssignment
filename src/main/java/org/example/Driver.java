package org.example;


import javiergs.tulip.GitHubHandler;

import java.util.List;

/**
 * Driver enables us to get files from the GitHub repository and stores in Blackboard
 * @author Amelia Harris and Isaac Lowe
 * @version 2.2
 */

public class Driver implements Runnable {

    private String url;

    public Driver(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        Blackboard.getInstance().clear();
        try {
            String token = "TOKEN";
            GitHubHandler gh = new GitHubHandler(token);
            List<String> allFromUrl = gh.listFilesRecursive(url);
            Blackboard.getInstance().setUrl(url);
            for (String path : allFromUrl) {
                if (path.endsWith(".java")) {
                    String content = gh.getFileContentFromUrl(convertToBlobUrl(url, path));
                    int lines = countLines(content);
                    boolean abstraction = FileAnalyzer.isAbstract(content);
                    Square square = new Square(path, lines, abstraction, content);
                    Blackboard.getInstance().addSquare(square);
                }
            }
            java.util.List<Square> totalSquares = Blackboard.getInstance().getSquares();
            for(Square currSquare : totalSquares){
                FileAnalyzer.countOccurrences(currSquare);
            }
            Blackboard.getInstance().setReady();
            Thread.sleep(1000);
        } catch (Exception e) {
            Blackboard.getInstance().setErrorURL();
            e.printStackTrace();
        }
    }

    private int countLines(String content) {
        return (int) content.lines().count();
    }

    private String convertToBlobUrl(String url, String path) {
        if (url.contains("/tree/")) {
            String[] parts = url.split("/tree/");
            return parts[0] + "/blob/" + parts[1].split("/")[0] + "/" + path;
        } else {
            return url.replace("/tree/", "/blob/") + "/" + path;
        }
    }
}
