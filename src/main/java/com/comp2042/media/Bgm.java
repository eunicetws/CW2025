package com.comp2042.media;

import com.comp2042.data.SaveData;
import com.comp2042.enums.KeyEventType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.util.Objects;

public class Bgm {

    private static MediaPlayer mediaPlayer;

    public static void init() {
        if (mediaPlayer != null) return;

        Media media = new Media(
                Objects.requireNonNull(Bgm.class.getResource("/audio/Soda Soda.mp3")).toExternalForm()
        );

        mediaPlayer = new MediaPlayer(media);
        setVolume();
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public static void play() {
        if (mediaPlayer != null)
            mediaPlayer.play();
    }

    public static void setVolume() {
        if (mediaPlayer != null) {
            try {
                int savedVolume = SaveData.ReadFileInt(SaveData.getKeyEvent(KeyEventType.MUSIC));
                mediaPlayer.setVolume(savedVolume / 100.0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
