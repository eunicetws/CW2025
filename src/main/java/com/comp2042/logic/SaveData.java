package com.comp2042.logic;

import javafx.scene.input.KeyCode;

import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;

public class SaveData {
    // Define the path for the saved file
    static String savePath = "src/main/resources/saveData.txt";
    static Path saveFilePath = Paths.get(savePath);

    // Method to create the save file if it doesn't exist
    public static void createSaveFile() {
        try {
            File saveFile = new File(savePath);
            if (!(saveFile.exists())) {
                if (saveFile.createNewFile()) {
                    FileWriter write = getFileWriter(saveFile);
                    write.close();
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static FileWriter getFileWriter(File saveFile) throws IOException {
        FileWriter write = new FileWriter(saveFile);
        write.write("0\n"); // highScore
        write.write(KeyCode.UP+"\n"); // rotate
        write.write(KeyCode.DOWN+"\n"); //down event
        write.write(KeyCode.LEFT+"\n"); // go left
        write.write(KeyCode.RIGHT+"\n"); // go right
        write.write(KeyCode.H+"\n"); // go Hold
        write.write(KeyCode.ESCAPE+"\n"); // Pause
        write.write(KeyCode.N+"\n"); // New Game
        return write;
    }

    public static void overWriteFile(int newData, int line) throws IOException {
        try{
            List<String> fileContent = new ArrayList<>(Files.readAllLines(saveFilePath, StandardCharsets.UTF_8));

            if (line >= 0 && line < fileContent.size()) {
                fileContent.set(line, Integer.toString(newData));
            } else {
                System.out.println("IndexOutOfBound: " + line);
                return;
            }
            Files.write(saveFilePath, fileContent, StandardCharsets.UTF_8);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static int ReadFile(int line) throws IOException {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(saveFilePath, StandardCharsets.UTF_8));

        String data = fileContent.get(line);
        return Integer.parseInt(data);

    }
}

