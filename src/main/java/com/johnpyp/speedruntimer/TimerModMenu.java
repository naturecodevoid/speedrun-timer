package com.johnpyp.speedruntimer;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.Arrays;

public class TimerModMenu implements ModMenuApi {
    public static final Config config = SpeedrunTimer.config;

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return this::getConfigScreenByCloth;
    }

    public Screen getConfigScreenByCloth(Screen parent) {
        ConfigBuilder builder =
                ConfigBuilder.create().setParentScreen(parent).setTitle(new LiteralText("Speedrun Timer"));

        ConfigEntryBuilder eb = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(new LiteralText("General"));
        ConfigCategory personalBest = builder.getOrCreateCategory(new LiteralText("Personal Best Run"));
        ConfigCategory splits = builder.getOrCreateCategory(new LiteralText("Automatic Splits"));
        ConfigCategory colors = builder.getOrCreateCategory(new LiteralText("Colors"));
        ConfigCategory other = builder.getOrCreateCategory(new LiteralText("Other"));

        //// GENERAL
        general.addEntry(
                eb.startBooleanToggle(
                        new LiteralText("Enabled?"), config.data.enabled)
                        .setDefaultValue(true)
                        .setSaveConsumer(val -> config.data.enabled = val)
                        .build());
        general.addEntry(
                eb.startIntField(new LiteralText("X Offset"), config.data.xOffset)
                        .setDefaultValue(5)
                        .setSaveConsumer(integer -> config.data.xOffset = integer)
                        .build());
        general.addEntry(
                eb.startIntField(new LiteralText("Y Offset"), config.data.yOffset)
                        .setDefaultValue(5)
                        .setSaveConsumer(val -> config.data.yOffset = val)
                        .build());
        general.addEntry(
                eb.startDoubleField(
                        new LiteralText("Background transparency"), config.data.backgroundTransparency)
                        .setDefaultValue(0.5)
                        .setMin(0.0)
                        .setMax(1.0)
                        .setSaveConsumer(val -> config.data.backgroundTransparency = val)
                        .build());

        //// PERSONAL BEST RUN
        personalBest.addEntry(
                eb.startBooleanToggle(
                        new LiteralText(
                                "Show personal best run?"),
                        config.data.showPersonalBest)
                        .setDefaultValue(true)
                        .setSaveConsumer(val -> config.data.showPersonalBest = val)
                        .build());
        personalBest.addEntry(
                eb.startBooleanToggle(
                        new LiteralText(
                                "Show personal best run after run is finished?"),
                        config.data.showPersonalBestAtEnd)
                        .setDefaultValue(false)
                        .setSaveConsumer(val -> config.data.showPersonalBestAtEnd = val)
                        .build());
        personalBest.addEntry(
                eb.startBooleanToggle(
                        new LiteralText(
                                "Show space when using personal best above game time or below real time?"),
                        config.data.includeSpaceWhenAboveOrBelow)
                        .setDefaultValue(true)
                        .setSaveConsumer(val -> config.data.includeSpaceWhenAboveOrBelow = val)
                        .build());
        personalBest.addEntry(
                eb.startDropdownMenu(
                        new LiteralText(
                                "Personal best position (remove all text to change)"),
                        DropdownMenuBuilder.TopCellElementBuilder.of(config.personalBestPos.name, (str) -> {
                            config.personalBestPos = Config.PersonalBestPos.of(str);
                            return config.data.personalBestPos;
                        }, (ignored) -> {
                            return new LiteralText(config.personalBestPos.name);
                        }),
                        DropdownMenuBuilder.CellCreatorBuilder.of()
                )
                        .setDefaultValue(Config.PersonalBestPos.BELOW_EVERYTHING.name)
                        //.setSaveConsumer(val -> config.personalBestPos = Config.PersonalBestPos.of(String.valueOf(val)))
                        //.setSuggestionMode(false)
                        .setSelections(new ArrayList<>(Arrays.asList(
                                Config.PersonalBestPos.ABOVE_GAME_TIME.name,
                                Config.PersonalBestPos.BELOW_REAL_TIME.name,
                                Config.PersonalBestPos.BETWEEN_REAL_TIME_AND_SPLITS.name,
                                Config.PersonalBestPos.BELOW_EVERYTHING.name
                        )))
                        .build()
        );

        //// SPLITS
        splits.addEntry(
                eb.startBooleanToggle(
                        new LiteralText("Show automatic splits?"), config.data.showSplits)
                        .setDefaultValue(true)
                        .setSaveConsumer(val -> config.data.showSplits = val)
                        .build());
        splits.addEntry(
                eb.startBooleanToggle(
                        new LiteralText("Show comparison best times?"), config.data.showCompareSplits)
                        .setDefaultValue(false)
                        .setSaveConsumer(val -> config.data.showCompareSplits = val)
                        .build());
        splits.addEntry(
                eb.startBooleanToggle(
                        new LiteralText(
                                "Compare using individual best splits instead of personal best run?"),
                        config.data.useBestSplits)
                        .setDefaultValue(false)
                        .setSaveConsumer(val -> config.data.useBestSplits = val)
                        .build());

        //// COLORS
        colors.addEntry(
                eb.startColorField(
                        new LiteralText("Personal best color"), config.data.personalBestColor)
                        .setDefaultValue(0xf2d204)
                        .setSaveConsumer(val -> config.data.personalBestColor = val)
                        .build());
        colors.addEntry(
                eb.startColorField(
                        new LiteralText("Game time color"), config.data.gameTimeColor)
                        .setDefaultValue(0x3afcef)
                        .setSaveConsumer(val -> config.data.gameTimeColor = val)
                        .build());
        colors.addEntry(
                eb.startColorField(
                        new LiteralText("Real time color"), config.data.realTimeColor)
                        .setDefaultValue(0x0f998f)
                        .setSaveConsumer(val -> config.data.realTimeColor = val)
                        .build());
        colors.addEntry(
                eb.startColorField(
                        new LiteralText("Overworld split color"), config.data.overworldSplitColor)
                        .setDefaultValue(0x01a00c)
                        .setSaveConsumer(val -> config.data.overworldSplitColor = val)
                        .build());
        colors.addEntry(
                eb.startColorField(
                        new LiteralText("Nether split color"), config.data.netherSplitColor)
                        .setDefaultValue(0x8e0101)
                        .setSaveConsumer(val -> config.data.netherSplitColor = val)
                        .build());
        colors.addEntry(
                eb.startColorField(
                        new LiteralText("Stronghold split color"), config.data.strongholdSplitColor)
                        .setDefaultValue(0x5944fc)
                        .setSaveConsumer(val -> config.data.strongholdSplitColor = val)
                        .build());
        colors.addEntry(
                eb.startColorField(
                        new LiteralText("Finished split color"), config.data.finishedSplitColor)
                        .setDefaultValue(0x24cc35)
                        .setSaveConsumer(val -> config.data.finishedSplitColor = val)
                        .build());
        colors.addEntry(
                eb.startColorField(
                        new LiteralText("Seed color"), config.data.seedColor)
                        .setDefaultValue(0x3bfc2d)
                        .setSaveConsumer(val -> config.data.seedColor = val)
                        .build());

        //// OTHER
        other.addEntry(
                eb.startBooleanToggle(
                        new LiteralText("Display seed on hud after run is finished?"), config.data.showSeed)
                        .setDefaultValue(false)
                        .setSaveConsumer(val -> config.data.showSeed = val)
                        .build());

        return builder
                .setSavingRunnable(
                        () -> {
                            try {
                                config.saveConfig();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            config.loadConfig();
                        })
                .build();
    }
}
