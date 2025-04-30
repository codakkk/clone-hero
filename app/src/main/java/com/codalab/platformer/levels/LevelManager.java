package com.codalab.platformer.levels;

public final class LevelManager {

    private int currentLevel = 0;
    private final Level[] levels;

    public LevelManager() {
        this.levels = new Level[5];

        this.levels[0] = LevelLoader.load("level1.txt");
        this.levels[1] = LevelLoader.load("level2.txt");
        this.levels[2] = LevelLoader.load("level3.txt");
        this.levels[3] = LevelLoader.load("level4.txt");
        this.levels[4] = LevelLoader.load("level5.txt");
    }

    public Level nextLevel() {
        return this.levels[++currentLevel];
    }

    public Level getCurrentLevel() {
        return this.levels[currentLevel];
    }

    public int getCurrentLevelIndex() {
        return this.currentLevel;
    }

    public boolean isLastLevel() {
        return this.currentLevel == levels.length-1;
    }
}
