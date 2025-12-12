package org.example;


import java.util.List;

import javiergs.tulip.GitHubHandler;

/**
 * Driver enables us to get files from the GitHub repository and stores in Blackboard
 * @author Amelia Harris and Isaac Lowe
 * @version 1.0
 */

public class Driver implements Runnable {

    private String url;

    public Driver(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        try {
            
            new java.net.URL(url);

            
            Blackboard.getInstance().clear();

            String token = "TOKEN";
            GitHubHandler gh = new GitHubHandler(token);
            FileAnalyzer fileAnalyzer = new FileAnalyzer();
            List<String> allFromUrl = gh.listFilesRecursive(url);
            Blackboard.getInstance().setUrl(url);

            for (String path : allFromUrl) {
                if (path.endsWith(".java")) {
                    String content = gh.getFileContentFromUrl(convertToBlobUrl(url, path));
                    int lines = countLines(content);
                    boolean abstraction = fileAnalyzer.isAbstract(content);
                    Square square = new Square(path, lines, abstraction, content);
                    Blackboard.getInstance().addSquare(square);
                }
            }

            java.util.List<Square> totalSquares = Blackboard.getInstance().getSquares();
            for(Square currSquare : totalSquares){
                String resultUml = fileAnalyzer.countOccurrences(currSquare, totalSquares);
                Blackboard.getInstance().addUmlSource(resultUml);
            }

            Blackboard.getInstance().setReady();
            Thread.sleep(1000);

        } catch (java.net.MalformedURLException e) {
            
            Blackboard.getInstance().setErrorURL();
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
