package org.example;

import java.util.List;

//Isaac
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
            //System.out.println("Current Square: "+name+ " the fileContent: "+fileContent);
            if (fileContent.contains(name)) {
                //System.out.println("CurrSquare: "+currSquare.getName()+ "Compare: "+compare.getName());
                compare.incIn(); // Count each name only once, even if it appears multiple times
                currSquare.incOut();
            }
        }
        return count;
    }
}



