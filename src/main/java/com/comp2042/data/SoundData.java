package com.comp2042.data;

/**
 * Represents an individual sound configuration consisting of a file name
 * and volume. This class is used to store and
 * retrieve sound settings.
 */
public class SoundData {

    /** The name of the audio file. */
    private final String fileName;
    /** The volume level associated with the audio file. */
    private final double volume;

    /**
     * Creates a new {@code SoundData} instance with a file name and volume.
     *
     * @param fileName the name of the sound file
     * @param volume the volume level to play the sound at
     */
    public SoundData(String fileName, double volume) {
        this.fileName = fileName;
        this.volume = volume;
    }

    /**
     * Returns the name of the sound file.
     *
     * @return the audio file path
     */
    public String getFileName() { return fileName; }

    /**
     * Returns the volume of the sound.
     *
     * @return the volume
     */
    public double getVolume() { return volume; }
}

