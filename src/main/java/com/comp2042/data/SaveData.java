package com.comp2042.data;

import com.comp2042.enums.KeyEventType;
import javafx.scene.input.KeyCode;

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

    // Create the save file if it doesn't exist
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
            throw new RuntimeException(e);
        }
    }

    private static FileWriter getFileWriter(File saveFile) throws IOException {
        FileWriter write = new FileWriter(saveFile);
        write.write(
            "0\n" + // highScore
            KeyCode.LEFT + "\n" + // go left
            KeyCode.RIGHT + "\n" +// go right
            KeyCode.UP + "\n" + // rotate
            KeyCode.DOWN + "\n" + // down event
            KeyCode.H + "\n" +  // Hold
            KeyCode.ESCAPE + "\n" + // Pause
            KeyCode.N + "\n" +  // Restart
            KeyCode.SPACE + "\n" +// HardDrop
            "30\n" +    // Music
            "Modern1.mp3 : 0.2\n" +  // Buttons
            "coin.mp3 : 0.3\n"// Clear Line
        );
        return write;

    }

    public static void overWriteFile(String newData, int line) throws IOException {
        try{
            List<String> fileContent = new ArrayList<>(Files.readAllLines(saveFilePath, StandardCharsets.UTF_8));

            if (line >= 0 && line < fileContent.size()) {
                fileContent.set(line, newData);
            } else {
                System.out.println("IndexOutOfBound: " + line);
                return;
            }
            Files.write(saveFilePath, fileContent, StandardCharsets.UTF_8);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
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
            throw new RuntimeException(e);
        }
    }

    public static void overWriteFile(double newData, int index, int line) throws IOException {
        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(saveFilePath, StandardCharsets.UTF_8));

            String[] data = fileContent.get(line).split("\\s*:\\s*");

            if (index < 0 || index >= data.length) {
                System.out.println("IndexOutOfBound");
                return;
            }

            data[index] = Double.toString(newData);

            fileContent.set(line, String.join(" : ", data));

            Files.write(saveFilePath, fileContent, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static int getKeyEvent(KeyEventType eventType) {
        return switch (eventType) {
            case HIGHSCORE -> 0;
            case LEFT -> 1;
            case RIGHT -> 2;
            case ROTATE -> 3;
            case DOWN -> 4;
            case HOLD -> 5;
            case PAUSE -> 6;
            case RESTART -> 7;
            case HARDDROP -> 8;
            case MUSIC -> 9;
            case BUTTONS -> 10;
            case CLEARLINES -> 11;
        };
    }

    // read file for integer
    public static int ReadFileInt(int line) throws IOException {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(saveFilePath, StandardCharsets.UTF_8));

        String data = fileContent.get(line);
        return Integer.parseInt(data);
    }

    // read file for string
    public static String ReadFileString(int line) throws IOException {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(saveFilePath, StandardCharsets.UTF_8));
        return fileContent.get(line);
    }

    public static String[] ReadFileList(int line) throws IOException {
        String data = ReadFileString(line);
        if (data.contains(" : ")) {
            return data.split("\\s*:\\s*");
        } else {
            return new String[]{data};
        }
    }

    // read file for key code
    public static KeyCode ReadKeyCode(int line) throws IOException{
        String keyString = SaveData.ReadFileString(line);  // "!","@" etc.
        return KeyCode.valueOf(keyString);
    }
}

