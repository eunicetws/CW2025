package com.comp2042.data;

import com.comp2042.enums.SaveDataType;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;

/**
 * The {@code SaveData} class manages reading and writing the game settings
 * and player saved data to a text file.
 *
 * <p>The save file is stored as a simple line-based text file. Each line
 * corresponds to a specific setting defined in {@link SaveDataType}.
 *
 * <p>This class is responsible for:
 * <ul>
 *     <li>Create the save file with default settings</li>
 *     <li>Overwrite individual settings by line</li>
 *     <li>Read integers, strings, booleans, key codes, and formatted lists</li>
 *     <li>Map {@code SaveDataType} values to their corresponding line number</li>
 * </ul>
 *
 * <p>All fields and methods are static because save data is shared globally.
 */
public class SaveData {
    /** Path to the save data text file. */
    static String savePath = "src/main/resources/saveData.txt";

    /** Representation of the save file  */
    static Path saveFilePath = Paths.get(savePath);

    /**
     * Creates a save file if it does not already exist.
     * <p>
     *     The save file is created with default game settings and controls.
     * </p>
     */
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

    /**
     * Writes the default save data content to a newly created file.
     *
     * @param saveFile the save file to write into
     * @return {@link FileWriter} after writing the initial content
     * @throws IOException if writing fails
     */
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
            "Retro1.mp3 : 0.2\n" +  // Buttons
            "coin.mp3 : 0.3\n" + // Clear Line
            "true\n" + // Toggle Hold
            "true\n" + // Toggle Next
            "true\n" + // Toggle GhostPiece
            "0\n" + // highScore 5min
            "0\n" + // highScore 10min
            "0\n" + // highScore 15min
            "0\n" + // highScore 20min
            "true\n" // Toggle show controls
        );
        return write;

    }

// rewrite the file
    /**
     * Overwrites a specific line in the save file with a new string value.
     *
     * @param newData the new string to write
     * @param line the line number to overwrite
     * @throws IOException if file access fails
     */
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

    /**
     * Overwrites a specific line in the save file with an integer value.
     *
     * @param newData the new integer to write
     * @param line the line number to overwrite
     * @throws IOException if file access fails
     */
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

    /**
     * Overwrites a value inside a list separated by colons on a specific line.
     *
     * @param newData the new double value to replace
     * @param index the position in the list to modify
     * @param line the file line to modify
     * @throws IOException if file access fails
     */
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

    /**
     * Maps a {@link SaveDataType} constant to its corresponding line number
     * in the save file.
     *
     * @param eventType the type of save data
     * @return the line number
     */
    public static int getKeyEvent(SaveDataType eventType) {
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
            case TOGGLE_HOLD -> 12;
            case TOGGLE_NEXT -> 13;
            case TOGGLE_GHOST -> 14;
            case HIGHSCORE_5 -> 15;
            case HIGHSCORE_10 -> 16;
            case HIGHSCORE_15 -> 17;
            case HIGHSCORE_20 -> 18;
            case TOGGLE_CONTROLS -> 19;

        };
    }

    /**
     * Reads an integer value from the specified line.
     *
     * @param line the line to read
     * @return the integer value stored on that line
     * @throws IOException if reading fails
     */
    public static int ReadFileInt(int line) throws IOException {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(saveFilePath, StandardCharsets.UTF_8));

        String data = fileContent.get(line);
        return Integer.parseInt(data);
    }

    /**
     * Reads a string from the specified line.
     *
     * @param line the line to read
     * @return the string located at the line
     * @throws IOException if reading fails
     */
    public static String ReadFileString(int line) throws IOException {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(saveFilePath, StandardCharsets.UTF_8));
        return fileContent.get(line);
    }

    /**
     * Reads a boolean value from the specified line.
     *
     * @param line the line to read
     * @return {@code true} or {@code false} from the file
     * @throws IOException if reading fails
     */
    public static boolean ReadBoolean(int line) throws IOException{
        List<String> fileContent = new ArrayList<>(Files.readAllLines(saveFilePath, StandardCharsets.UTF_8));

        String data = fileContent.get(line);
        return Boolean.parseBoolean(data);
    }

    /**
     * Reads a list separated by colons from the specified line.
     * If no colon is present, returns a single-element array.
     *
     * @param line the line to read
     * @return an array of values from the line
     * @throws IOException if reading fails
     */
    public static String[] ReadFileList(int line) throws IOException {
        String data = ReadFileString(line);
        if (data.contains(" : ")) {
            return data.split("\\s*:\\s*");
        } else {
            return new String[]{data};
        }
    }

    /**
     * Reads a saved keyboard shortcut from the specified line and converts it
     * * into a {@link KeyCode}.
     *
     * @param line the line containing the saved key code
     * @return the corresponding {@code KeyCode}
     * @throws IOException if reading fails
     */
    public static KeyCode ReadKeyCode(int line) throws IOException{
        String keyString = SaveData.ReadFileString(line);  // "!","@" etc.
        return KeyCode.valueOf(keyString);
    }
}

