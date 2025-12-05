package com.comp2042.media;

import com.comp2042.data.SaveData;
import com.comp2042.enums.SaveDataType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.util.Objects;

/**
 * This class handles the background music for the game.
 * <p>
 * This class loads the volume from {@link SaveDataType}, ensures the background music is played on an
 * infinite loop.
 * </p>
 * <p>
 *     This class is responsible for:
 * </p>
 * <ul>
 *     <li>Initializing the background music file</li>
 *     <li>Applying the volume settings</li>
 *     <li>Playing the music on loop</li>
 *     <li>Starting or changing the music volume</li>
 * </ul>
 */
public class Bgm {

    /** The MediaPlayer used to store the background music. */
    private static MediaPlayer mediaPlayer;

    /**
     * Initializes the background music player, loads the default audio file, set the audio on loop
     * and sets the volume.
     */
    public static void init() {
        if (mediaPlayer != null) return;

        Media media = new Media(
                Objects.requireNonNull(Bgm.class.getResource("/audio/Soda Soda.mp3")).toExternalForm()
        );

        mediaPlayer = new MediaPlayer(media);
        setVolume();
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }
    /**
     * Starts playing the background music.
     * If the player has not been initialized, this method does nothing.
     */
    public static void play() {
        if (mediaPlayer != null)
            mediaPlayer.play();
    }

    /**
     * Sets the volume of the background music based on the user's saved preference.
     * <p>
     * The volume is read from {@link SaveData} using the line indicated by {@link SaveDataType}.
     * The saved value is stored as an integer and converted to a decimal between 0 and 1,
     * as required by {@link MediaPlayer}.
     * </p>
     *
     * @throws RuntimeException if reading the saved volume from {@link SaveDataType}.
     */

    public static void setVolume() {
        if (mediaPlayer != null) {
            try {
                int savedVolume = SaveData.ReadFileInt(SaveData.getKeyEvent(SaveDataType.MUSIC));
                mediaPlayer.setVolume(savedVolume / 100.0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
