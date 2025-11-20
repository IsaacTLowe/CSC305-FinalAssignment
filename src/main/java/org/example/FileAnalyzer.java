package org.example;

import java.util.List;

/**
 * FileAnalyzer analyzes the files to see for abstractness, size, dependencies, etc
 * @author Amelia Harris and Isaac Lowe
 * @version 1.0
 */

public class FileAnalyzer {

    public static void analyze(String fileData) {

        List<String> lines = List.of(fileData.split("\n"));
        int size = lines.size();

        Blackboard.getInstance().setSize(size);
    }

    public static boolean isAbstract(String fileData) {
        List<String> lines = List.of(fileData.split("\n"));

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("//") || trimmed.startsWith("/*") || trimmed.startsWith("*")) continue;

            if (trimmed.contains("abstract class ")) {
                return true;
            } else if (trimmed.contains("interface ")) {
                return true;
            }
        }

        return false;
    }
    public static int countOccurrences(Square currSquare) {
        java.util.List<Square> squares = Blackboard.getInstance().getSquares();
        int count = 0;

        for (Square compare: squares) {
            String fileContent = currSquare.getFileContent();
            String name = compare.getName();
            if(name.equals(currSquare.getName())){
                break;
            }
            if (name.endsWith(".java")) {
                name = name.substring(0, name.length() - 5);
            }
            
            if (fileContent.contains(name)) {
                compare.incIn();
                currSquare.incOut();
            }
        }
        return count;
    }
}



