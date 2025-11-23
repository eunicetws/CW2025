package com.comp2042.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Level {

    private final IntegerProperty level = new SimpleIntegerProperty(1);

    public IntegerProperty propertiesLevel() {
        return level;
    }

    public void levelUp(){
        level.setValue(level.getValue() + 1);
    }

    public void reset() {
        level.setValue(0);
    }

    public boolean reachLevelRequirement(int lines){
        int level = this.level.getValue()-1;
        return lines >= (5 * (level * level)) + (5 * level) && level != 20;
            // Cleared lines >= level Up Requirement (lvlUp for each 5n) and stop increasing after lvl20
    }

    public int calcSpeed(){
        int x = this.level.getValue() - 1;
        return (int) (1065.0 / (x + 3) + 45); //start at 400ms stops at (lvl 20) at ~90 ms
    }
}
