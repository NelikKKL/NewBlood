package me.newblood;

import me.newblood.module.ModuleManager;
import me.newblood.module.Module;
import me.newblood.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class NewBloodClient implements ClientModInitializer {
    public static NewBloodClient INSTANCE;
    public MinecraftClient mc;
    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private final java.util.Map<Module, Boolean> keyStates = new java.util.HashMap<>();

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        mc = MinecraftClient.getInstance();
        moduleManager = new ModuleManager();
        configManager = new ConfigManager();
        
        // Load config after manager is initialized
        configManager.load();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && client.world != null) {
                for (Module m : moduleManager.getModules()) {
                    if (m.isEnabled()) {
                        m.onTick();
                    }
                }
            }
        });

        // Keybind handling
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;
            if (client.currentScreen != null) return;

            for (Module m : moduleManager.getModules()) {
                if (m.getKey() == 0) continue;
                
                boolean isPressed = org.lwjgl.glfw.GLFW.glfwGetKey(client.getWindow().getHandle(), m.getKey()) == org.lwjgl.glfw.GLFW.GLFW_PRESS;
                boolean wasPressed = keyStates.getOrDefault(m, false);

                if (isPressed && !wasPressed) {
                    m.toggle();
                }
                keyStates.put(m, isPressed);
            }
        });
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
