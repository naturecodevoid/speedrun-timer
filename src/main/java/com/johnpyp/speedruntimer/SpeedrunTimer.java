package com.johnpyp.speedruntimer;

import com.johnpyp.speedruntimer.datastorage.DataStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

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
                System.err.println(readLineByLineJava8(file.getPath()));
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

    // https://howtodoinjava.com/java/io/java-read-file-to-string-examples/
    private static String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }
}
