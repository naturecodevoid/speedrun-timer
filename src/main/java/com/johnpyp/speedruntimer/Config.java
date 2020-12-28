package com.johnpyp.speedruntimer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public final File configFile;
    public ConfigData data;
    public PersonalBestPos personalBestPos;

    Config(File configFile) {
        this.configFile = configFile;
        data = getDefaultConfigData();
    }

    public static Config of(File configFile) {
        Config config = new Config(configFile);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                config.data = config.getDefaultConfigData();
                config.saveConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }

    public ConfigData getDefaultConfigData() {
        personalBestPos = PersonalBestPos.BELOW_EVERYTHING;
        return new ConfigData(
                true,
                5,
                5,
                0.5,
                false,
                false,
                false,
                true,
                personalBestPos.val,
                true,
                true,
                false,
                0xf2d204,
                0x3afcef,
                0x0f998f,
                0x01a00c,
                0x8e0101,
                0x5944fc,
                0x24cc35,
                0x3bfc2d);
    }

    public void saveConfig() {
        data.personalBestPos = personalBestPos.val;
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(GSON.toJson(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        try (FileReader reader = new FileReader(configFile)) {
            ConfigData nextData = GSON.fromJson(reader, ConfigData.class);
            if (nextData == null) {
                data = getDefaultConfigData();
                saveConfig();
                return;
            }
            data = nextData;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final class ConfigData {
        public boolean enabled;
        public int xOffset;
        public int yOffset;
        public double backgroundTransparency;
        public boolean showSeed;
        public boolean showCompareSplits;
        public boolean useBestSplits;
        public boolean showPersonalBest;
        public int personalBestPos;
        public boolean includeSpaceWhenAboveOrBelow;
        public boolean showSplits;
        public boolean showPersonalBestAtEnd;
        public int personalBestColor;
        public int gameTimeColor;
        public int realTimeColor;
        public int overworldSplitColor;
        public int netherSplitColor;
        public int strongholdSplitColor;
        public int finishedSplitColor;
        public int seedColor;

        ConfigData(boolean enabled,
                   int xOffset,
                   int yOffset,
                   double backgroundTransparency,
                   boolean showSeed,
                   boolean showCompareSplits,
                   boolean useBestSplits,
                   boolean showPersonalBest,
                   int personalBestPos,
                   boolean includeSpaceWhenAboveOrBelow,
                   boolean showSplits,
                   boolean showPersonalBestAtEnd,
                   int personalBestColor,
                   int gameTimeColor,
                   int realTimeColor,
                   int overworldSplitColor,
                   int netherSplitColor,
                   int strongholdSplitColor,
                   int finishedSplitColor,
                   int seedColor) {
            this.enabled = enabled;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.backgroundTransparency = backgroundTransparency;
            this.showSeed = showSeed;
            this.showCompareSplits = showCompareSplits;
            this.useBestSplits = useBestSplits;
            this.showPersonalBest = showPersonalBest;
            this.personalBestPos = personalBestPos;
            this.includeSpaceWhenAboveOrBelow = includeSpaceWhenAboveOrBelow;
            this.showSplits = showSplits;
            this.showPersonalBestAtEnd = showPersonalBestAtEnd;
            this.personalBestColor = personalBestColor;
            this.gameTimeColor = gameTimeColor;
            this.realTimeColor = realTimeColor;
            this.overworldSplitColor = overworldSplitColor;
            this.netherSplitColor = netherSplitColor;
            this.strongholdSplitColor = strongholdSplitColor;
            this.finishedSplitColor = finishedSplitColor;
            this.seedColor = seedColor;
        }
    }

    public static class PersonalBestPos {
        public final static PersonalBestPos ABOVE_GAME_TIME = new PersonalBestPos(0, "Above game time");
        public final static PersonalBestPos BELOW_REAL_TIME = new PersonalBestPos(1, "Below real time");
        public final static PersonalBestPos BETWEEN_REAL_TIME_AND_SPLITS = new PersonalBestPos(2, "Between real time and splits");
        public final static PersonalBestPos BELOW_EVERYTHING = new PersonalBestPos(3, "Below everything");

        public int val;
        public String name;

        public PersonalBestPos(int val, String name) {
            this.val = val;
            this.name = name;
        }

        public static PersonalBestPos of(int val) {
            switch (val) {
                case 0:
                    // Above game time
                    return ABOVE_GAME_TIME;
                // return new PersonalBestPos(val, "Above game time");
                case 1:
                    // Below real time
                    return BELOW_REAL_TIME;
                // return new PersonalBestPos(val, "Below real time");
                case 2:
                    // Between real time and splits
                    return BETWEEN_REAL_TIME_AND_SPLITS;
                // return new PersonalBestPos(val, "Between real time and splits");
                // case 3:
                //     // Below everything
                //     return new PersonalBestPos(val, "Below everything");
                default:
                    // Default to below everything
                    return BELOW_EVERYTHING;
                // return new PersonalBestPos(3, "Below everything");
            }
        }

        public static PersonalBestPos of(String name) {
            name = name.trim();
            if (name.contains("Above game time") || name.contains("Above")) {
                // Above game time
                return ABOVE_GAME_TIME;
            } else if (name.contains("Below real time") || name.contains("Below r")) {
                // Below real time
                return BELOW_REAL_TIME;
            } else if (name.contains("Between real time and splits") || name.contains("Between")) {
                // Between real time and splits
                return BETWEEN_REAL_TIME_AND_SPLITS;
            } else if (name.contains("0")) {
                // Above game time
                return ABOVE_GAME_TIME;
            } else if (name.contains("1")) {
                // Below real time
                return BELOW_REAL_TIME;
            } else if (name.contains("2")) {
                // Between real time and splits
                return BETWEEN_REAL_TIME_AND_SPLITS;
            }
            // Default to below everything
            return BELOW_EVERYTHING;
        }
    }
}
