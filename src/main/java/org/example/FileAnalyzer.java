package org.example;

import java.util.List;

/**
 * FileAnalyzer analyzes the files to see for abstractness, size, dependencies, etc
 * @author Amelia Harris and Isaac Lowe
 * @version 1.2
 */

public class FileAnalyzer {

    public int analyzeSize(String fileData) {
        List<String> lines = List.of(fileData.split("\n"));
        int size = lines.size();
        return size;
    }

    public int computeComplexity(String fileData) {
        int complexity = 1;
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

            complexity += countMatches(line, "&&");
            complexity += countMatches(line, "||");
        }

        return complexity;
    }

    private int countMatches(String line, String token) {
        int count = 0;
        int idx = 0;
        while ((idx = line.indexOf(token, idx)) != -1) {
            count++;
            idx += token.length();
        }
        return count;
    }

    public boolean isAbstract(String fileData) {
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

    public String determineRelationship(String fileData, String relationName, String dependentClass) {
        List<String> lines = List.of(fileData.split("\n"));
        

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("//") || trimmed.startsWith("/*") || trimmed.startsWith("*")) {
                continue;
            }

            if (trimmed.contains("extends ") && trimmed.contains(relationName)) {
                return(dependentClass+" --|> "+relationName +"\n");
            
                //Blackboard.getInstance().addUmlSource(dependentClass+" --|> "+relationName +"\n");

            }

            else if (trimmed.contains("implements ") && trimmed.contains(relationName)) {
                
                return(dependentClass+" ..|> "+relationName +"\n");
            }

            else if (trimmed.contains(relationName) && (trimmed.contains("private ") || trimmed.contains("protected ") || trimmed.contains("public "))) {
                if (trimmed.contains("= new " + relationName)) {
                    
                        return(dependentClass+" *-- "+relationName +"\n");

                } else {
                    
                        return(dependentClass+" o-- "+relationName +"\n");

                }

            }

            else{
                
                if(trimmed.contains(relationName)){
                    return(dependentClass+" --> "+relationName +"\n");
                }
                    
            }
            
        }
        
        return "";
    }


    public String countOccurrences(Square currSquare, java.util.List<Square> squares) {
        
        String relations = "";

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
                String newRelation = determineRelationship(fileContent, name, currSquareName);
                if(!relations.contains(newRelation)){
                    relations += newRelation;
                }
                
            }
        }
        return relations;
    }
}