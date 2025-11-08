package org.example;

import java.util.List;

//Isaac
public class FileAnalyzer {

    public static void analyze(String fileData) {

        List<String> lines = List.of(fileData.split("\n"));
        int size = lines.size();

        Blackboard.getInstance().setSize(size);
    }
}
