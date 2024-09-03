package xyz.hewkawar.survival_light_block;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.LiteralMessage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static xyz.hewkawar.survival_light_block.SurvivalLightBlock.MOD_ID;

public class SurvivalLightBlockClient implements ClientModInitializer {
    private boolean wasKeyDown = false;

    @Override
    public void onInitializeClient() {
        // Client-side initialization
        ModKeyBindings.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean isKeyDown = ModKeyBindings.TOGGLE_LIGHT_BLOCK.isDown();
            if (isKeyDown && !wasKeyDown) {
                ModConfig.toggleLightBlockOption();
                if (client.player != null) {
                    client.player.displayClientMessage(
                            new TranslatableComponent("config." + MOD_ID + ".toggle_light_block_" + ModConfig.getToggleLightBlockOption()),true
                    );
                }
            }
            wasKeyDown = isKeyDown;
        });
    }

    public static class ModConfig {
        private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
        private static final Path CONFIG_PATH = Paths.get("config", MOD_ID + ".json");
        private static Config config;

        public static void loadConfig() {
            if (Files.exists(CONFIG_PATH)) {
                try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                    config = GSON.fromJson(reader, Config.class);
                } catch (IOException e) {
                    System.err.println("Failed to load config: " + e.getMessage());
                    config = new Config();
                }
            } else {
                config = new Config();
                saveConfig();
            }
        }

        public static void saveConfig() {
            try {
                Files.createDirectories(CONFIG_PATH.getParent());
                try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                    GSON.toJson(config, writer);
                }
            } catch (IOException e) {
                System.err.println("Failed to save config: " + e.getMessage());
            }
        }

        public static boolean getToggleLightBlockOption() {
            return config.toggleLightBlockOption;
        }

        public static void toggleLightBlockOption() {
            config.toggleLightBlockOption = !config.toggleLightBlockOption;
            saveConfig();
        }

        private static class Config {
            boolean toggleLightBlockOption = false;
        }
    }

    public static class ModKeyBindings {
        private static final String CATEGORY = "key.categories." + MOD_ID;
        public static final KeyMapping TOGGLE_EXAMPLE = new KeyMapping(
                "key." + MOD_ID + ".toggle_example",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                CATEGORY
        );

        public static final KeyMapping TOGGLE_LIGHT_BLOCK = new KeyMapping(
                "key." + MOD_ID + ".toggle_light_block",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_F7,
                CATEGORY
        );

        public static void register() {
            KeyBindingHelper.registerKeyBinding(TOGGLE_LIGHT_BLOCK);
        }
    }
}