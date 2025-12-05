package com.comp2042.media;

import com.comp2042.data.SaveData;
import com.comp2042.data.SoundData;
import com.comp2042.enums.SaveDataType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.*;

/**
 * This class handles loading and playing sound effects (SFX)
 * <p>
 * Each sound effect is associated with a {@link SaveDataType} entry in the save file,
 * which stores both the file name and its volume level. These settings are loaded into
 * an internal map and used whenever sound effects are played.
 * </p>
 *
 * <p>This class is responsible for:</p>
 * <ul>
 *     <li>Loading SFX file paths and volume</li>
 *     <li>Storing SFX data in a map based on the SFX type</li>
 * </ul>
 */
public class Sfx {

    /**
     * Creates a {@link HashMap} that maps each sound type to its corresponding volume
     *
     * <p>
     *     This map is used to store and retrieve the sound type and volume settings for different
     *     SFX. The sound type and volume is retrieved from {@link SaveData} using {@link SaveDataType}
     * </p>
     */
    private static final Map<SaveDataType, SoundData> sfxMap = new HashMap<>();

    /**
     * Loads the saved SFX sound type and volume into {@link Map}.
     * @param sfxType - type of SFX
     */
    public static void loadMap(SaveDataType sfxType) {
        try {
            String[] raw = SaveData.ReadFileList(SaveData.getKeyEvent(sfxType));
            String fileName = raw[0].trim();
            double volume = Double.parseDouble(raw[1].trim());
            sfxMap.put(sfxType, new SoundData(fileName, volume));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Plays a sound effect (SFX).
     *
     * <p>This method retrieves the sound file path and its associated volume from
     * {@link Map} determined by {@code type}, and then plays the sound.</p>
     *
     * @param type the specific SFX type to play
     */
    public static void play(SaveDataType type) {
        SoundData sound = sfxMap.get(type);
        if (sound == null) return;

        Media media = new Media(Objects.requireNonNull(Sfx.class.getResource("/audio/" + sound.getFileName())).toExternalForm());
        MediaPlayer player = new MediaPlayer(media);

        player.setVolume(sound.getVolume());
        player.play();
    }

    /**
     * Changes the {@link Map} sound effect (SFX) associated with the SFX type.
     *
     * <p>
     *     This map is used to rewrite the SFX type specified by {@code sfxType}
     *     with the sound type and volume settings.
     *     The sound type and volume is retrieved from {@link SaveData} using {@link SaveDataType}.
     * </p>
     *
     */
    public static void reset(SaveDataType sfxType) {
        try {
            String[] raw = SaveData.ReadFileList(SaveData.getKeyEvent(sfxType));

            List<String> lines = Arrays.asList(raw);
            sfxMap.put(sfxType, new SoundData(lines.get(0), Double.parseDouble(lines.get(1))));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
