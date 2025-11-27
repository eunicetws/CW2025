package com.comp2042.media;

import com.comp2042.data.SaveData;
import com.comp2042.data.SoundData;
import com.comp2042.enums.KeyEventType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.*;

public class Sfx {

    private static final Map<KeyEventType, SoundData> sfxMap = new HashMap<>();

    public static void loadMap(KeyEventType sfxType) {
        try {
            String[] raw = SaveData.ReadFileList(SaveData.getKeyEvent(sfxType));
            String fileName = raw[0].trim();
            double volume = Double.parseDouble(raw[1].trim());
            sfxMap.put(sfxType, new SoundData(fileName, volume));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void play(KeyEventType type) {
        SoundData sound = sfxMap.get(type);
        if (sound == null) return;

        Media media = new Media(Objects.requireNonNull(Sfx.class.getResource("/audio/" + sound.getFileName())).toExternalForm());
        MediaPlayer player = new MediaPlayer(media);

        player.setVolume(sound.getVolume());
        player.play();
    }

    public static void reset(KeyEventType sfxType) {
        try {
            String[] raw = SaveData.ReadFileList(SaveData.getKeyEvent(sfxType));

            List<String> lines = Arrays.asList(raw);
            sfxMap.put(sfxType, new SoundData(lines.get(0), Double.parseDouble(lines.get(1))));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
