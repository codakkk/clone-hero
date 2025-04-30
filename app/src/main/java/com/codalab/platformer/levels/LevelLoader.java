package com.codalab.platformer.levels;

import com.badlogic.androidgames.framework.math.Vector2;
import com.codalab.platformer.MainActivity;
import com.codalab.platformer.entities.GoldEntity;
import com.codalab.platformer.entities.KeyEntity;
import com.codalab.platformer.entities.MovingTileEntity;
import com.codalab.platformer.entities.PressurePlateEntity;
import com.codalab.platformer.entities.SpikeEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public final class LevelLoader {
    private LevelLoader() {}

    public static Level load(String name) {
        Level level = new Level();

        // Previously, width/height were in the level definition files
        // for easier map definition, I fixed the dimensions
        level.width = 49;
        level.height = 21;
        level.tiles = new int[level.width * level.height];
        Arrays.fill(level.tiles, 0);

        String row = null;
        BufferedReader buffer = null;

        boolean readingData = false;
        boolean readingEntities = false;

        int yPos = 0;

        try (InputStream in = MainActivity.fileIO.readAsset(name)) {
            buffer = new BufferedReader(new InputStreamReader(in));
            while ((row = buffer.readLine()) != null) {

                if(row.trim().isEmpty()) {
                    continue;
                }

                if (row.startsWith("#")) {
                    switch(row) {
                        case "#START_DATA#": readingData = true; break;
                        case "#END_DATA#": readingData = false; break;
                        case "#START_ENTITIES#": readingEntities = true; break;
                        case "#END_ENTITIES#": readingEntities = true; break;
                    }
                    continue;
                }

                if(readingData && readingEntities) {
                    throw new RuntimeException("Reading data while reading entities is not supported x)");
                }

                if (readingData) {
                    parseData(level, row);
                } else if(readingEntities) {
                    parseEntities(level, row);
                } else {
                    parseTilemap(level, yPos++, row);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '" + name + "'");
        }

        return level;
    }

    private static void parseTilemap(Level level, int yPos, String row) {
        String[] tiles = row.split("");
        // Skips the first because split adds an empty ""
        for(int x = 1; x < tiles.length; ++x) {
            String tile = tiles[x];

            // This is because x starts from 1 and not 0
            int xPos = x-1;

            int tileIndex = xPos + yPos * level.width;

            switch(tile) {
                case "0":
                case "1":
                case "2":
                    level.tiles[tileIndex] = Integer.parseInt(tile);
                    break;
                case "p":
                    level.spawnPoint = new Vector2(x, yPos);
                    break;
                case "f":
                    level.flagPoint = new Vector2(x, yPos);
                    break;
                // Just skip them, used as placeholders
                case "g":
                case "b":
                    break;
                default:
                    throw new UnsupportedOperationException("Invalid tile type: " + tile);
            }
        }
    }

    private static void parseData(Level level, String row) {
        String[] split = row.split("=");
        if(split.length > 1) {
            String key = split[0];
            String value = split[1];

            switch(key) {
                case "name":
                    level.name = value;
                    break;
                case "shots":
                    level.startingShells = Integer.parseInt(value);
                    break;
                case "width":
                    level.width = Integer.parseInt(value);
                    break;
                case "height":
                    level.height = Integer.parseInt(value);
                    break;
            }
        }
    }

    private static void parseEntities(Level level, String row) {
        String[] data = row.split(",");

        String name = data[0];

        Vector2 position = new Vector2(Integer.parseInt(data[1]), Integer.parseInt(data[2]));
        switch(name) {
            case "spike":
                level.entities.put(position, SpikeEntity.class);
                break;
            case "gold":
                level.entities.put(position, GoldEntity.class);
                break;
            case "mtile":
                level.entities.put(position, MovingTileEntity.class);
                break;
            case "pressure":
            case "key":
                Vector2[] pressureButtons = level.gates.get(position);
                if(pressureButtons == null) {
                    final int maxSize = (data.length - 2) / 2;
                    pressureButtons = new Vector2[maxSize];
                }

                int idx = 3;
                for(int i = 0; i < pressureButtons.length; ++i) {
                    pressureButtons[i] = new Vector2(Integer.parseInt(data[idx++]), Integer.parseInt(data[idx++]));
                }

                level.entities.put(position, name.equals("pressure") ? PressurePlateEntity.class : KeyEntity.class);
                level.gates.put(position, pressureButtons);
                break;
        }
    }

}
