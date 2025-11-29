package com.comp2042.data;

import com.comp2042.enums.KeyEventType;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class SaveDataTest {

    private static Path tempFile;

    @BeforeEach
    void setup() throws Exception {
        // Correct test file location
        tempFile = Paths.get("src/test/resources/testSaveData.txt");

        // Ensure parent directory exists
        Files.createDirectories(tempFile.getParent());

        // Remove old test file if present
        Files.deleteIfExists(tempFile);

        // Override SaveData paths
        SaveData.savePath = tempFile.toString();
        SaveData.saveFilePath = tempFile;

        // Create save file with defaults
        SaveData.createSaveFile();
    }


    @Test
    void testCreateSaveFile() {
        File file = new File(SaveData.savePath);
        assertTrue(file.exists(), "SaveData file should be created");
        assertTrue(file.length() > 0, "SaveData file should contain default values");
    }

    @Test
    void testOverwriteString() throws Exception {
        SaveData.overWriteFile("TEST_STRING", 0);
        assertEquals("TEST_STRING", SaveData.ReadFileString(0));
    }

    @Test
    void testOverwriteInt() throws Exception {
        SaveData.overWriteFile(12345, 1);
        assertEquals(12345, SaveData.ReadFileInt(1));
    }

    @Test
    void testOverwriteDoubleInList() throws Exception {
        // Line 10: "Retro1.mp3 : 0.2"
        SaveData.overWriteFile(0.9, 1, 10);

        String[] list = SaveData.ReadFileList(10);
        assertEquals("0.9", list[1]);
    }

    @Test
    void testReadBoolean() throws Exception {
        SaveData.overWriteFile("false", 12);
        assertFalse(SaveData.ReadBoolean(12));
    }

    @Test
    void testReadKeyCode() throws Exception {
        SaveData.overWriteFile("A", 1);
        KeyCode code = SaveData.ReadKeyCode(1);
        assertEquals(KeyCode.A, code);
    }

    @Test
    void testGetKeyEvent() {
        assertEquals(0, SaveData.getKeyEvent(KeyEventType.HIGHSCORE));
        assertEquals(3, SaveData.getKeyEvent(KeyEventType.ROTATE));
        assertEquals(8, SaveData.getKeyEvent(KeyEventType.HARDDROP));
    }
}
