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

        // *** ADDED: complexity calculation ***
        int complexity = computeComplexity(fileData);
        Blackboard.getInstance().setComplexity(complexity);
    }

    // *** ADDED: Compute complexity (decision points +1) ***
    public static int computeComplexity(String fileData) {
        int complexity = 1; // base complexity
        List<String> lines = List.of(fileData.split("\n"));

        for (String rawLine : lines) {
            String line = rawLine.trim();

            if (line.startsWith("//") || line.startsWith("/*") || line.startsWith("*"))
                continue;

            if (line.contains(" if ")) complexity++;
            if (line.contains(" else if ")) complexity++;
            if (line.contains(" for ")) complexity++;
            if (line.contains(" while ")) complexity++;
            if (line.contains(" do ")) complexity++;
            if (line.contains(" switch ")) complexity++;
            if (line.contains(" case ")) complexity++;
            if (line.contains(" catch ")) complexity++;

            // logical operators increase complexity
            complexity += countMatches(line, "&&");
            complexity += countMatches(line, "||");
        }

        return complexity;
    }

    // Helper for logical operator counting
    private static int countMatches(String line, String token) {
        int count = 0;
        int idx = 0;
        while ((idx = line.indexOf(token, idx)) != -1) {
            count++;
            idx += token.length();
        }
        return count;
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

    public static void determineRelationship(String fileData, String relationName, String dependentClass) {
        List<String> lines = List.of(fileData.split("\n"));

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("//") || trimmed.startsWith("/*") || trimmed.startsWith("*")) {
                continue;
            }

            //  Inheritance
            if (trimmed.contains("extends ") && trimmed.contains(relationName)) {

                Blackboard.getInstance().addUmlSource(dependentClass+" --|> "+relationName +"\n");

            }

            //  Interface Implementation
            else if (trimmed.contains("implements ") && trimmed.contains(relationName)) {
                Blackboard.getInstance().addUmlSource(dependentClass+" ..|> "+relationName +"\n");

            }



            //  Composition / Aggregation


            else if (trimmed.contains(relationName) && (trimmed.contains("private ") || trimmed.contains("protected ") || trimmed.contains("public "))) {
                if (trimmed.contains("= new " + relationName)) {
                    Blackboard.getInstance().addUmlSource(dependentClass+" *-- "+relationName +"\n");

                } else {
                    Blackboard.getInstance().addUmlSource(dependentClass+" o-- "+relationName +"\n");

                }


            }
            // Association
            else{
                Blackboard.getInstance().addUmlSource(dependentClass+" --> "+relationName +"\n");
            }
        }
        System.out.println("Final UML: " + Blackboard.getInstance().getUmlSource());
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
                String currSquareName = currSquare.getName();
                if (currSquareName.endsWith(".java")) {
                    currSquareName = currSquareName.substring(0, currSquareName.length() - 5);
                }
                determineRelationship(fileContent, name, currSquareName);
            }
        }
        return count;
    }
}
