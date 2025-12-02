package org.example;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static void determineRelationship(String fileData, String compareName) {
        List<String> lines = List.of(fileData.split("\n"));
        java.util.List<Square> squares = Blackboard.getInstance().getSquares();

        StringBuilder relations = new StringBuilder();

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("//") || trimmed.startsWith("/*") || trimmed.startsWith("*")) {
                continue;
            }

            // 1️⃣ Inheritance
            if (trimmed.contains("extends ") && trimmed.contains(compareName)) {
                String[] tokens = trimmed.split("\\s+");
                int idx = java.util.Arrays.asList(tokens).indexOf("extends");
                if (idx >= 0 && idx + 1 < tokens.length) {
                    String parent = tokens[idx + 1].replace("{", "").trim();
                    if (!parent.isBlank() && !relations.toString().contains("Inheritance: " + parent)) {
                        relations.append("Inheritance: ").append(parent).append("\n");
                    }
                }
            }

            // 2️⃣ Interface Implementation
            if (trimmed.contains("implements ") && trimmed.contains(compareName)) {
                String[] tokens = trimmed.split("\\s+");
                int idx = java.util.Arrays.asList(tokens).indexOf("implements");
                if (idx >= 0 && idx + 1 < tokens.length) {
                    String iface = tokens[idx + 1].replace("{", "").trim();
                    // only include if it's a project class
                    boolean isProjectClass = false;
                    for (Square sq : squares) {
                        if (sq.getName().equals(iface)) {
                            isProjectClass = true;
                            break;
                        }
                    }
                    if (isProjectClass && !relations.toString().contains("Implements: " + iface)) {
                        relations.append("Implements: ").append(iface).append("\n");
                    }
                }
            }

            // 3️⃣ Composition / Aggregation
            for (Square cls : squares) {
                String clsName = cls.getName();
                if (clsName.isBlank() || clsName.equals(compareName)) continue;

                if (trimmed.contains(clsName)) {
                    if (trimmed.contains("= new " + clsName) && !relations.toString().contains("Composition: " + clsName)) {
                        relations.append("Composition: ").append(clsName).append("\n");
                    } else if (!relations.toString().contains("Aggregation: " + clsName)) {
                        relations.append("Aggregation: ").append(clsName).append("\n");
                    }
                }
            }
        }

        // Update Blackboard
        Blackboard.getInstance().setUmlSource(relations.toString());
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
                determineRelationship(fileContent, name);
            }
        }
        return count;
    }
}



