package com.comp2042.data;

public class SoundData {
    private final String fileName;
    private double volume;

    public SoundData(String fileName, double volume) {
        this.fileName = fileName;
        this.volume = volume;
    }

    public String getFileName() { return fileName; }
    public double getVolume() { return volume; }
}

