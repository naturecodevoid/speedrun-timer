package com.johnpyp.speedruntimer;

import com.johnpyp.speedruntimer.datastorage.DataStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Environment(EnvType.CLIENT)
public class SpeedrunTimer implements ModInitializer {
    public static final Config config =
            Config.of(
                    new File(
                            FabricLoader.getInstance().getConfigDir().toFile(), "speedrun-timer.config.json"));

    @Override
    public void onInitialize() {
        config.loadConfig();
        MinecraftClient client = MinecraftClient.getInstance();
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        DataStorage store;
        File file = new File(configDir, "speedrun-timer.data.json");
        try {
            store = DataStorage.of(file);
            store.refreshBests("");
        } catch (NullPointerException e) {
            System.err.println("Deleting data file (speedrun-timer.data.json) because there was an error. File data:");
            try {
                System.err.println(Files.readString(Path.of(file.getPath())));
            } catch (Exception ignored) {
                System.err.println("Failed to print file data.");
            }
            try {
                file.delete();
            } catch (Exception ignored) {
                System.err.println("Failed to delete data file (speedrun-timer.data.json).");
            }
            store = DataStorage.of(file);
            store.refreshBests("");
        }
        TickHandler tickHandler = new TickHandler(client, store, config);
        HudRenderCallback.EVENT.register((__, ___) -> tickHandler.tick());
    }
}
